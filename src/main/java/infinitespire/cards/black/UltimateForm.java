package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.abstracts.BlackCard;
import infinitespire.powers.UltimateFormPower;

public class UltimateForm extends BlackCard {
	
	private static final String ID = "UltimateForm";
	private static final String NAME = "Ultimate Form";
	private static final String TEXTURE = "img/infinitespire/cards/ultimateform.png";
	private static final int COST = 5;
	private static final String DESCRIPTION = "Gain !M! Strength and Dexterity each turn. NL If you have orb slots gain !M! Focus each turn.";
	private static final int MAGIC = 2;
	
	public UltimateForm() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.POWER, CardTarget.SELF);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
	}

	@Override
	public AbstractCard makeCopy() {
		return new UltimateForm();
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(1);
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new UltimateFormPower(p, this.magicNumber), this.magicNumber));
	}
}
