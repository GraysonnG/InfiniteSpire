package infinitespire.cards.black;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FinalStrike extends BlackCard {

	public static final String ID = "FinalStrike";
	private static final String NAME = "Final Strike";
	private static final String IMG = "img/cards/oneforall.png";
	private static final int COST = 3;
											//Basically a better perfected strike
	private static final String DESCRIPTION = "Deal !D! damage. Deals an additional !M! damage for ALL of your cards containing Strike.";
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardTarget TARGET = CardTarget.ENEMY;
	
	public FinalStrike() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
		this.baseDamage = 15;
		this.baseMagicNumber = 5;
		this.magicNumber = 5;
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new FinalStrike();
	}

	@Override
	public void upgrade() {
		if(this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(5);
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		
	}

}
