package infinitespire.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class InvigoratePower extends AbstractPower {
	public InvigoratePower(AbstractCreature owner) {
		this.owner = owner;
		if(AbstractDungeon.player.hasPower("Strength")) {
			this.amount = AbstractDungeon.player.getPower("Strength").amount;
		}
		this.name = "Invigorate";
		this.ID = "is_InvigoratePower";
		this.img = InfiniteSpire.getTexture("img/powers/invigor.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
	}
	
	public float atDamageFinalGive(float damageAmount, DamageInfo.DamageType info) {
		if(info == DamageInfo.DamageType.NORMAL) {
			return damageAmount += amount;
		}
		return damageAmount;
	}
	
	
	
	@Override
	public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
		if(target == AbstractDungeon.player && source == AbstractDungeon.player && power.ID.equals("Strength")) {
			amount += power.amount;
			this.flashWithoutSound();
			this.updateDescription();
		}
	}

	public void updateDescription() {
		this.description = "Attacks deal #y" + amount + " extra damage.";	
	}
}
