package infinitespire.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class GolemPower extends AbstractPower {
	
	private static float PERCENTAGE_GAIN = 0.05f;
	private int count;
	
	public GolemPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = 0;
		this.name = "Golem's Might";
		this.ID = "is_GolemPower";
		this.img = InfiniteSpire.getTexture("img/powers/golem.png");
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.updateDescription();
	}
	
	@Override
	public void atStartOfTurn() {
		if(count == 20) return;
		this.flash();
		count += 1;
		updateDescription();
	}
	
	@Override
	public float atDamageFinalGive(float damageAmount, DamageType info) {
		if(info == DamageInfo.DamageType.NORMAL) {
			return (float) Math.ceil(damageAmount *= (1.0f + (count * PERCENTAGE_GAIN)));
		}
		return damageAmount;
	}
	
	public void updateDescription() {
		this.description = "All attacks deal " + (int) Math.round((count * PERCENTAGE_GAIN) * 100) + "% more damage. (Cap: 100%)";
	}
}
