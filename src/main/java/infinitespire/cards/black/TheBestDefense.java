package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class TheBestDefense extends BlackCard{
	public static final String ID = InfiniteSpire.createID("TheBestDefense");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String IMG = "img/infinitespire/cards/bestdefense.png";
	private static final int COST = 1;

	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardTarget TARGET = CardTarget.SELF;

	public TheBestDefense(){
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
	}

	@Override
	public void upgrade() {
		if(!upgraded){
			this.upgradeName();
			this.upgradeBaseCost(0);
		}
	}

	@Override
	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		int amountOfBlock = p.currentBlock;

		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1)));
		AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(p, p));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, amountOfBlock), amountOfBlock));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseStrengthPower(p, amountOfBlock), amountOfBlock));
	}
}
