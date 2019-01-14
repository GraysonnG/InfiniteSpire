package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
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
		this.ID = "is_Menacing";
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/realityshift.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
	}

	public void updateDescription() {
		String textA;
		String textB;
		if (Settings.language == Settings.GameLanguage.FRA){
			this.name = "Menaçant";
			textA = 	"Les  " + amount + " prochaines attaques que vous jouez \u00e9tourdissent tous les ennemis touchez pour 1 tour.";
			textB = "La prochaine attaque que vous jouez étourdit les ennemis touchés pour 1 tour";
		} else {
			this.name = "Menacing";
			textA = 	"The next " + amount + " attacks you play stun any enemy they hit for 1 turn.";
			textB = "The next attack you play stuns any enemy it hits for 1 turn.";
		}


		this.description = amount > 1 ?	textA :textB;
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
