package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;
import infinitespire.powers.DeenergizedPower;

public class Cupcake extends Relic {
	public static final String ID = InfiniteSpire.createID("Cupcake");
	public static final String NAME = "Cupcake";
	public boolean firstTurn = false;
	public boolean tryToAddPower = false;
	
	public Cupcake() {
		super(ID, "cupcake", RelicTier.BOSS, LandingSound.MAGICAL);
		this.counter = 3;
	}

	@Override
	public void onEquip() {
		EnergyManager energy = AbstractDungeon.player.energy;
		++energy.energyMaster;
	}

	@Override
	public void onUnequip() {
		EnergyManager energy = AbstractDungeon.player.energy;
		--energy.energyMaster;
	}

	@Override
	public void atBattleStart() {
		counter = 3;
		tryToAddPower = true;
	}

	@Override
	public void onVictory() {
		counter = 3;
	}

	@Override
	public void onPlayerEndTurn() {
		if(counter > 0) counter --;
		if(counter == 0){
			if(!AbstractDungeon.player.hasPower(DeenergizedPower.powerID) && tryToAddPower) {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new DeenergizedPower(AbstractDungeon.player, 1),
					1));
			}
			tryToAddPower = false;
		}
	}
}
