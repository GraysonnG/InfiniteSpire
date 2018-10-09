package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.abstracts.BlackCard;
import infinitespire.powers.SuperSlowPower;

public class Execution extends BlackCard {
	private static final String ID = "Execution";
	private static final String NAME = "Execution";
	private static final String TEXTURE = "img/infinitespire/cards/execution.png";
	private static final int COST = 2;
	private static final String DESCRIPTION = "Apply Shredded to an enemy. NL Exhaust.";

	public Execution() {
		super(ID, NAME, TEXTURE, COST, DESCRIPTION, CardType.SKILL, CardTarget.ENEMY);
		this.exhaust = true;
	}

	@Override
	public void upgrade() {
		if(!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(1);
		}
	}

	@Override
	public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
		AbstractDungeon.actionManager.addToBottom(
			new ApplyPowerAction(abstractMonster, abstractPlayer,
				new SuperSlowPower(abstractMonster, 0), 0));
	}
}
