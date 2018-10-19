package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import infinitespire.abstracts.BlackCard;

public class TheBestDefense extends BlackCard{
	public static final String ID = "TheBestDefense";
	private static final String NAME = "The Best Defense";
	private static final String IMG = "img/infinitespire/cards/bestdefense.png";
	private static final int COST = 1;

	private static final String DESCRIPTION = "Convert all of your Block into Strength, lose that Strength at the end of your turn.";
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

		AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(p, p));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, amountOfBlock), amountOfBlock));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new LoseStrengthPower(p, amountOfBlock), amountOfBlock));
	}
}
