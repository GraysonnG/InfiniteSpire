package infinitespire.crossover.cards;

import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction;
import com.evacipated.cardcrawl.mod.bard.notes.WildCardNote;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class DarkHarmony extends BlackCard {
	public static final String ID = InfiniteSpire.createID("DarkHarmony");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String TEXTURE = "img/infinitespire/cards/beta.png";
	private static final int COST = 2;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final int MAGIC = 2;

	public DarkHarmony() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.SKILL, CardTarget.SELF);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
	}

	public void upgrade() {
		if (!upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(1);
		}
	}

	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		for(int i = 0; i < magicNumber; i++) {
			AbstractDungeon.actionManager.addToBottom(new QueueNoteAction(WildCardNote.get()));
		}
	}
}