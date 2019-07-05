package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class Starlight extends BlackCard {
	private static final String ID = InfiniteSpire.createID("Starlight");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String TEXTURE = "img/infinitespire/cards/starlight.png";
	private static final int COST = 1;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int MAGIC = 10;

	public Starlight() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.SKILL, CardTarget.SELF);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
		this.exhaust = true;
	}

	public void upgrade() {
		if (!upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(5);
		}
	}

	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		addToBot(new HealAction(p, p, this.magicNumber));
	}
}