package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import infinitespire.abstracts.BlackCard;

public class UNNAMED_1 extends BlackCard {

	public static final String ID = "UNNAMED_1";
	public static final String NAME = "Unnamed Card";
	public static final String IMG = "img/infinitespire/cards/beta.png";
	public static final String DESCRIPTION = "Gain X Intangible.";
	public static final String DESCRIPTION_U = "Gain X + 1 Intangible.";

	public static final int COST = -1;

	public static final CardType TYPE = CardType.SKILL;
	public static final CardTarget TARGET = CardTarget.SELF;

	public UNNAMED_1(){
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
	}


	@Override
	public void upgrade() {
		if(!this.upgraded){
			this.upgradeName();
			this.rawDescription = DESCRIPTION_U;
			this.initializeDescription();
		}
	}

	@Override
	public void useWithEffect(AbstractPlayer p, AbstractMonster abstractMonster) {
		if(this.energyOnUse < EnergyPanel.totalCount) {
			this.energyOnUse = EnergyPanel.totalCount;
		}

		int energyAmount = this.energyOnUse;

		if(p.hasRelic(ChemicalX.ID)) {
			p.getRelic(ChemicalX.ID).flash();
			energyAmount += 2;
		}

		if(this.upgraded) energyAmount += 1;

		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
			new IntangiblePlayerPower(p, energyAmount), energyAmount));
	}
}
