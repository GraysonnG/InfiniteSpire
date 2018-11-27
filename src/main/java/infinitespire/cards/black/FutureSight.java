package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class FutureSight extends BlackCard {

	private static final String ID = InfiniteSpire.createID("FutureSight");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String TEXTURE = "img/infinitespire/cards/futuresight.png";
	private static final int COST = 1;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final int MAGIC = 4;
	
	public FutureSight() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.SKILL, CardTarget.SELF);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
		this.exhaust = true;
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new FutureSight();
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
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
	}

}
