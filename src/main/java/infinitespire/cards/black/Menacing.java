package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.effects.uniqueVFX.MenacingEffect;
import infinitespire.powers.MenacingPower;

public class Menacing extends BlackCard {
	private static final String ID = InfiniteSpire.createID("Menacing");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String TEXTURE = "img/infinitespire/cards/menacing.png";
	private static final int COST = 1;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final String UPG_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
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
		AbstractDungeon.actionManager.addToBottom(new VFXAction(new MenacingEffect(), 0.5f));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MenacingPower(p, magicNumber)));
	}
}
