package infinitespire.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import basemod.abstracts.CustomCard;

public class Neurotoxin extends CustomCard {
	
	public static final String ID = "Neurotoxin";
	public static final String NAME = "Neurotoxin";
	public static final String DESCRIPTION = "Apply !B! Poison. NL Each time this card is played, permanently increase it's poison by !M!. NL Exhaust.";
	private static final int COST = 1;
	
	public Neurotoxin() {
		super(ID, NAME, "img/cards/neurotoxin.png", COST, DESCRIPTION, CardType.SKILL, CardColor.GREEN, CardRarity.UNCOMMON, CardTarget.ENEMY);
		
		this.misc = 2;
		this.baseMagicNumber = 2;
		this.magicNumber = 2;
		this.baseBlock = this.misc;
		this.exhaust = true;
	}

	@Override
	public AbstractCard makeCopy() {
		return new Neurotoxin();
	}

	@Override
	public void upgrade() {
		if(!upgraded) {
			this.upgradeMagicNumber(1);
			this.upgradeName();
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new IncreaseMiscAction(this.cardID, this.misc, this.magicNumber));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, baseBlock), baseBlock));
	}

}
