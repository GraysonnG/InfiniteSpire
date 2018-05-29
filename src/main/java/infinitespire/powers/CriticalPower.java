package infinitespire.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class CriticalPower extends AbstractPower {

	private float critChance;
	private float roll;
	private boolean modifyDamage;
	
	public CriticalPower(AbstractPlayer player, float critChance) {
		this.critChance = critChance;
		this.owner = player;
		this.amount = -1;
		this.name = "Critical Chance";
		this.ID = "is_CritPower";
		this.img = InfiniteSpire.getTexture("img/powers/crit.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
		
		roll = (float) Math.random();
		if(roll <= critChance) {
			modifyDamage = true;
			this.flash();
		}
	}

	@Override
	public float atDamageFinalGive(float damageAmount, DamageInfo.DamageType info) {
		if(info == DamageInfo.DamageType.NORMAL && modifyDamage) {
			modifyDamage = false;
			return damageAmount *= 2;
		}
		return damageAmount;
	}
	
	@Override
	public void onPlayCard(AbstractCard card, AbstractMonster m) {
		roll = (float) Math.random();
		if(roll <= critChance) {
			modifyDamage = true;
			this.flash();
		}
	}

	public void updateDescription() {
		this.description = "When you play an attack, it has a " + (int)(critChance * 100) + "% chance to deal 2x damage.";
	}
	
}
