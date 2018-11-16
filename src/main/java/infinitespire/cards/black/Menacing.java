package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.abstracts.BlackCard;
import infinitespire.powers.MenacingPower;

public class Menacing extends BlackCard {
	private static final String ID = "Menacing";
	private static final String NAME = "Menacing";
	private static final String TEXTURE = "img/infinitespire/cards/menacing.png";
	private static final int COST = 1;
	private static final String DESCRIPTION = "The next attack you play stuns any enemy it hits for 1 turn. NL Exhaust.";
	private static final String UPG_DESCRIPTION = "The next !M! attacks you play stun any enemy they hit for 1 turn. NL Exhaust.";
	private static final CardType TYPE = CardType.SKILL;
	private static final CardTarget TARGET = CardTarget.SELF;
	private static final int MAGIC = 1;

	public Menacing() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, TYPE, TARGET);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
		this.exhaust = true;
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(1);
			this.rawDescription = UPG_DESCRIPTION;
			this.initializeDescription();
		}
	}

	@Override
	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MenacingPower(p, magicNumber)));
	}
}
