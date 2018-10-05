package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import infinitespire.abstracts.BlackCard;

@Deprecated
public class ThousandBlades extends BlackCard {

	private static final String ID = "ThousandBlades";
	private static final String NAME = "Thousand Blades";
	private static final String TEXTURE = "img/infinitespire/cards/thousandblades.png";
	private static final int COST = 1;
	private static final String DESCRIPTION = "Add !M! Shivs to your hand.";
	private static final String DESCRIPTION2 = "Add !M! Upgraded Shivs to your hand.";
	private static final int MAGIC = 5;
	
	public ThousandBlades() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.SKILL, CardTarget.SELF);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
	}
	@Override
	public AbstractCard makeCopy() {
		return new ThousandBlades();
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.upgradeName();
			this.rawDescription = DESCRIPTION2;
			this.initializeDescription();
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractCard shiv = new Shiv().makeCopy();
		if(this.upgraded) {
			shiv.upgrade();
		}
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(shiv, this.magicNumber));
	}
}
