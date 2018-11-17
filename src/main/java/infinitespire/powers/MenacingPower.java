package infinitespire.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class MenacingPower extends AbstractPower {

	public MenacingPower(AbstractPlayer owner, int amount){
		this.owner = owner;
		this.amount = amount;
		this.name = "Menacing";
		this.ID = "is_Menacing";
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/realityshift.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = amount > 1 ?
			"The next " + amount + " attacks you play stun any enemy they hit for 1 turn." :
			"The next attack you play stuns any enemy it hits for 1 turn.";
	}

	@Override
	public void onAfterUseCard(AbstractCard card, UseCardAction action) {
		if(card.type == AbstractCard.CardType.ATTACK){
			this.amount--;
			this.updateDescription();
		}
		if(amount <= 0) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
		}
	}

	@Override
	public void atEndOfRound() {
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
	}

	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		if(target instanceof AbstractMonster) {
			if (target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
				this.flash();
				boolean shouldApplyToMonster = true;

				for(AbstractGameAction action : AbstractDungeon.actionManager.actions){
					if(action instanceof StunMonsterAction && action.target == target) {
						shouldApplyToMonster = false;
					}
				}

				if(shouldApplyToMonster){
					AbstractDungeon.actionManager.addToBottom(new StunMonsterAction((AbstractMonster) target, owner, 1));
				}
			}
		}
	}
}
