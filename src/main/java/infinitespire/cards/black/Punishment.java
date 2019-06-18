package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class Punishment extends BlackCard {
	
	private static final String ID = InfiniteSpire.createID("Punishment");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String TEXTURE = "img/infinitespire/cards/punishment.png";
	private static final int COST = 1;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final String DESCRIPTION_2 = cardStrings.UPGRADE_DESCRIPTION;
	
	public Punishment() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
		this.baseDamage = 0;
	}
	
	public void updateBaseDamage() {
		AbstractPlayer p = AbstractDungeon.player;
		
		this.baseDamage = 0;

		int newDamage = p.drawPile.size();
		newDamage += p.hand.size();
		newDamage += p.discardPile.size();

		this.baseDamage = newDamage * 2;
		this.isDamageModified = true;
		
		this.rawDescription = DESCRIPTION_2;
		this.initializeDescription();
	}

	@Override
	public void applyPowers() {
		this.updateBaseDamage();
		super.applyPowers();
	}

	@Override
	public void calculateCardDamage(AbstractMonster NOP) {
		this.updateBaseDamage();
		super.calculateCardDamage(NOP);
	}

	@Override
	public AbstractCard makeCopy() {
		return new Punishment();
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(0);
		}
	}

	@Override
	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}
}
