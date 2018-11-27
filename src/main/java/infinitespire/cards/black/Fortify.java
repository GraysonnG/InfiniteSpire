package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class Fortify extends BlackCard{

	public static final String ID = InfiniteSpire.createID("Fortify");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String IMG = "img/infinitespire/cards/fortify.png";
	private static final int COST = 1;

	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BLOCK = 3;
	private static final int MAGIC = 5;

	public Fortify(){
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
		this.baseBlock = BLOCK;
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
	}

	@Override
	public void upgrade() {
		if(!upgraded){
			this.upgradeName();
			this.upgradeMagicNumber(1);
		}
	}

	@Override
	public void useWithEffect(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
		for(int i = 0; i < magicNumber; i++){
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(abstractPlayer, abstractPlayer, block, true));
		}
	}
}
