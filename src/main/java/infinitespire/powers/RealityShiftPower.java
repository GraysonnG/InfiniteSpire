package infinitespire.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.Nightmare;

public class RealityShiftPower extends AbstractPower {
	public Nightmare nightmare;
	public boolean hasTriggered = false;

	public static final String powerID = InfiniteSpire.createID("RealityShiftPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);
	public final int maxAmount;

	public RealityShiftPower(Nightmare nightmare, int amount) {
		this.owner = nightmare;
		this.nightmare = nightmare;
		this.amount = amount;
		this.maxAmount = amount;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("realityshift.png");
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.priority = -99999;
		this.updateDescription();
	}
	
	
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if(!isPlayer){
			this.hasTriggered = false;
			this.amount = this.maxAmount;
			this.updateDescription();
		}
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		this.amount -= damageAmount;
		if(this.amount <= 0 && !hasTriggered){
			nightmare.onEnoughDamageTaken();
			this.amount = 0;
			this.hasTriggered = true;
		}
		return super.onAttacked(info, damageAmount);
	}

	public int getAttackStr() {
		DamageInfo dmg = nightmare.damage.get(0);
		dmg.applyPowers(nightmare, AbstractDungeon.player);

		return dmg.output;
	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0]
				+ amount
				+ strings.DESCRIPTIONS[1]
				+ getAttackStr()
				+ strings.DESCRIPTIONS[2]
				+ (nightmare.effectCount + 1)
				+ strings.DESCRIPTIONS[3];
	}
}
