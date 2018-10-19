package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import infinitespire.abstracts.BlackCard;

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
		
		int poisonTicks = this.energyOnUse + 1;
		
		if(p.hasRelic(ChemicalX.ID)) {
			p.getRelic(ChemicalX.ID).flash();
			poisonTicks += 2;
		}
		
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));

		for(int i = 0; i < poisonTicks; i++) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
		}
		
		p.energy.use(EnergyPanel.totalCount);
	}

}
