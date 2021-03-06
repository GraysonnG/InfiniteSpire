package infinitespire.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class MenacingPower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("MenacingPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public MenacingPower(AbstractPlayer owner, int amount){
		this.owner = owner;
		this.amount = amount;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("realityshift.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = amount > 1 ?
			strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1] :
			strings.DESCRIPTIONS[2];
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
