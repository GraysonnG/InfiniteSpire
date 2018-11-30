package infinitespire.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class DeenergizedPower extends AbstractPower {

	public static final String powerID = "is_Deenergized";

	public DeenergizedPower(AbstractPlayer owner, int amount) {
		this.name = "De-Energized";
		this.ID = powerID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/energydown.png");
		this.type = PowerType.DEBUFF;
	}

	@Override
	public void updateDescription() {
		this.description = "At the start of each turn, lose #b" + amount + " energy.";
	}

	@Override
	public void onEnergyRecharge() {
		this.flash();
		AbstractDungeon.player.loseEnergy(this.amount);
	}
}
