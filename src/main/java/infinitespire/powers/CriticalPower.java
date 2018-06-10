package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class CriticalPower extends AbstractPower {
	
	public CriticalPower(AbstractPlayer player) {
		this.owner = player;
		this.amount = -1;
		this.name = "Critical";
		this.ID = "is_CritPower";
		this.img = InfiniteSpire.getTexture("img/powers/crit.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
	}

	@Override
	public float atDamageFinalGive(float damageAmount, DamageInfo.DamageType info) {
		if(info == DamageInfo.DamageType.NORMAL) {
			return damageAmount *= 2;
		}
	
		return damageAmount;
	}
	
	

	@Override
	public void onAfterUseCard(AbstractCard card, UseCardAction action) {
		if(card.type == AbstractCard.CardType.ATTACK) {
			InfiniteSpire.logger.info("Attempt to Remove: " + this.ID);
			AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, this.ID));
		}
	}

	public void updateDescription() {
		this.description = "The next attack you play will deal 2x damage";
	}
}