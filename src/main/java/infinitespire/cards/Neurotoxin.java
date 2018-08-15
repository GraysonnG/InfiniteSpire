package infinitespire.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import basemod.abstracts.DynamicVariable;
import infinitespire.abstracts.Card;

public class Neurotoxin extends Card {
	
	public static final String ID = "Neurotoxin";
	public static final String NAME = "Neurotoxin";
	public static final String DESCRIPTION = "Apply !M! Poison. NL Each time this card is played, permanently increase it's poison by !inf_P!. NL Exhaust.";
	private static final int COST = 1;
	private int poisonCreep;
	
	public Neurotoxin() {
		super(ID, NAME, "img/infinitespire/cards/neurotoxin.png", COST, DESCRIPTION, CardType.SKILL, CardColor.GREEN, CardRarity.UNCOMMON, CardTarget.ENEMY);
		this.misc = 1;
		this.baseMagicNumber = misc;
		this.magicNumber = this.baseMagicNumber;
		this.poisonCreep = 2;
		this.exhaust = true;
	}

	@Override
	public AbstractCard makeCopy() {
		return new Neurotoxin();
	}
	
	

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		Neurotoxin card = (Neurotoxin) super.makeStatEquivalentCopy();
		card.poisonCreep = this.poisonCreep;
		return card;
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.poisonCreep = 3;
			this.upgradeName();
			this.updateDescription();
		}
	}
	
	public void updateDescription() {
		this.initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new IncreaseMiscAction(this.cardID, this.misc, this.poisonCreep));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, misc), misc));
	}

	@Override
    public void applyPowers() {
        this.baseMagicNumber = misc;
        this.magicNumber = this.baseMagicNumber;
        super.applyPowers();
        this.updateDescription();
    }
	
	public static class PoisonVariable extends DynamicVariable {

		@Override
		public int baseValue(AbstractCard card) {
			if(card instanceof Neurotoxin) {
				return ((Neurotoxin) card).poisonCreep;
			}
			return 0;
		}

		@Override
		public boolean isModified(AbstractCard card) {
			return false;
		}

		@Override
		public String key() {
			return "inf_P";
		}

		@Override
		public boolean upgraded(AbstractCard card) {
			return false;
		}

		@Override
		public int value(AbstractCard card) {
			if(card instanceof Neurotoxin) {
				return ((Neurotoxin) card).poisonCreep;
			}
			return 0;
		}
		
	}
}
