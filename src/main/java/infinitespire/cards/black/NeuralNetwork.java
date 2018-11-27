package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class NeuralNetwork extends BlackCard {

	private static final String ID = InfiniteSpire.createID("NeuralNetwork");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String TEXTURE = "img/infinitespire/cards/neuralnetwork.png";
	private static final int COST = 1;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final String DESCRIPTION2 = cardStrings.UPGRADE_DESCRIPTION;
	private static final int MAGIC = 2;
	
	public NeuralNetwork() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.POWER, CardTarget.SELF);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.upgradeName();
			this.isInnate = true;
			this.rawDescription = DESCRIPTION2;
			this.initializeDescription();
		}
	}

	@Override
	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DrawPower(p, this.magicNumber), this.magicNumber));
	}

}
