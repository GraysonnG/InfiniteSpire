package infinitespire.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class GolemPower extends AbstractPower {
	public GolemPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = 0;
		this.name = "Golem's Power";
		this.ID = "is_GolemPower";
		this.img = InfiniteSpire.getTexture("img/powers/golem.png");
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.updateDescription();
	}
	
	@Override
	public void atStartOfTurn() {
		this.flash();
		this.amount += 1;
		updateDescription();
	}
	
	@Override
	public float atDamageFinalGive(float damageAmount, DamageType info) {
		if(info == DamageInfo.DamageType.NORMAL) {
			return (float)Math.ceil(damageAmount *= (1 + (this.amount * 0.10)));
		}
		return damageAmount;
	}
	
	public void updateDescription() {
		this.description = "All attacks deal " + amount + "0% more damage.";
	}
}
