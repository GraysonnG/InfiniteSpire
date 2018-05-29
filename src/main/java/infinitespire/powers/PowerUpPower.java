package infinitespire.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class PowerUpPower extends AbstractPower{
	public PowerUpPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = -1;
		this.name = "Power-Up";
		this.ID = "is_PowerUpPower";
		this.img = InfiniteSpire.getTexture("img/powers/powerup.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
	}
	
	public float atDamageFinalGive(float damageAmount, DamageInfo.DamageType info) {
		if(info == DamageInfo.DamageType.NORMAL) {
			return (float)Math.ceil(damageAmount *= 1.10);
		}
		return damageAmount;
	}
	
	public void updateDescription() {
		this.description = "All attacks deal 10% more damage.";
	}
}
