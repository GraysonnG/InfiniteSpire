package infinitespire.cards;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Card;

public class Neurotoxin extends Card {
	
	public static final String ID = InfiniteSpire.createID("Neurotoxin");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final int COST = 1;
	private int poisonCreep;
	
	public Neurotoxin() {
		super(ID, NAME, "img/infinitespire/cards/neurotoxin.png", COST, DESCRIPTION, CardType.SKILL, CardColor.GREEN, CardRarity.RARE, CardTarget.ENEMY);
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
		AbstractDungeon.actionManager.addToBottom(new IncreaseMiscAction(this.uuid, this.misc, this.poisonCreep));
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
