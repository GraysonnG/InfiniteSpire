package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

public class Gouge extends BlackCard {
	
	public static final String ID = InfiniteSpire.createID("Gouge");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String IMG = "img/infinitespire/cards/gouge.png";
	private static final int COST = 0;
	
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardTarget TARGET = CardTarget.ENEMY;
	private static final int DAMAGE = 5;
	private static final int MAGIC = 2;
	
	public Gouge() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
		this.baseDamage = DAMAGE;
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
	}
	@Override
	public AbstractCard makeCopy() {
		return new Gouge();
	}

	@Override
	public void upgrade() {
		if(!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(4);
			this.upgradeMagicNumber(1);
		}
	}

	@Override
	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, p, ArtifactPower.POWER_ID));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
	}

}
