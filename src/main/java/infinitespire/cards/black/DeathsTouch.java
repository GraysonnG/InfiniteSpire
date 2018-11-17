package infinitespire.cards.black;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import infinitespire.abstracts.BlackCard;
import infinitespire.actions.DeathsTouchAction;

public class DeathsTouch extends BlackCard {

	public static final String ID = "DeathsTouch";
	private static final String NAME = "Deaths Touch";
	private static final String IMG = "img/infinitespire/cards/deathstouch.png";
	private static final int COST = -1;
											//Basically a better perfected strike
	private static final String DESCRIPTION = "Deal !D! damage. Apply !M! Poison X + 1 times.";
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardTarget TARGET = CardTarget.ENEMY;
	
	private static final int DAMAGE = 7;
	private static final int MAGIC = 3;
	
	public DeathsTouch() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
		this.baseDamage = DAMAGE;
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DeathsTouch();
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(2);
			this.upgradeDamage(3);
		}
	}

	@Override
	public void useWithEffect(AbstractPlayer p, AbstractMonster m) {
		if(this.energyOnUse < EnergyPanel.totalCount) {
			this.energyOnUse = EnergyPanel.totalCount;
		}

		AbstractDungeon.actionManager.addToBottom(new DeathsTouchAction(p, m, damage, magicNumber, damageTypeForTurn, freeToPlayOnce, energyOnUse));
	}
}
