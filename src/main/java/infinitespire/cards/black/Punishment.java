package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import infinitespire.abstracts.BlackCard;

public class Punishment extends BlackCard {
	
	private static final String ID = "Punishment";
	private static final String NAME = "Punishment";
	private static final String TEXTURE = "img/infinitespire/cards/punishment.png";
	private static final int COST = 1;
	private static final String DESCRIPTION = "Deal 1 damage for each card in your deck.";
	private static final String DESCRIPTION_2 = "Deal 1 damage for each card in your deck. NL ( !D! )";
	
	public Punishment() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
		this.baseDamage = 0;
	}
	
	public void updateBaseDamage() {
		AbstractPlayer p = AbstractDungeon.player;
		
		this.baseDamage = 0;
		this.baseDamage += p.drawPile.size();
		this.baseDamage += p.hand.size();
		this.baseDamage += p.discardPile.size();
		this.isDamageModified = true;
		
		this.rawDescription = DESCRIPTION_2;
		this.initializeDescription();
	}

	@Override
	public void applyPowers() {
		this.updateBaseDamage();
		super.applyPowers();
	}

	@Override
	public void calculateCardDamage(AbstractMonster NOP) {
		this.updateBaseDamage();
		super.calculateCardDamage(NOP);
	}

	@Override
	public AbstractCard makeCopy() {
		return new Punishment();
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(0);
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}
}
