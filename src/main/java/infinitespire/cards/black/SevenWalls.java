package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class SevenWalls extends BlackCard {
	public static final String ID = InfiniteSpire.createID("SevenWalls");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String IMG = "img/infinitespire/cards/walls.png";
	private static final int COST = 2;

	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardTarget TARGET = CardTarget.ENEMY;
	private static final int DAMAGE = 15;
	private static final int BLOCK = 15;

	public SevenWalls() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
		this.baseDamage = DAMAGE;
		this.baseBlock = BLOCK;
	}

	@Override
	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.name = cardStrings.UPGRADE_DESCRIPTION;
			++this.timesUpgraded;
			this.upgraded = true;
			this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
			this.initializeDescription();
			this.initializeTitle();
			this.upgradeBlock(5);
			this.upgradeDamage(5);
		}
	}
}
