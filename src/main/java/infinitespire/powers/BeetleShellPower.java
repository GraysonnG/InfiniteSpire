package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class BeetleShellPower extends AbstractPower {
	
	private boolean shouldRemove;
	
	public BeetleShellPower(AbstractPlayer player, boolean shouldRemove) {
		this(player);
		this.shouldRemove = true;
	}
	
	public BeetleShellPower(AbstractPlayer player) {
		this.owner = player;
		this.amount = -1;
		this.name = "Beetle Shell";
		this.ID = "is_BeetleShellPower";
		this.img = InfiniteSpire.getTexture("img/powers/beetleshell.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.shouldRemove = false;
	}
	
	@Override
	public float modifyBlock(float blockAmount) {
		blockAmount *= 2f;
		return blockAmount;
	}

	@Override
	public void onAfterUseCard(AbstractCard card, UseCardAction action) {
		if(shouldRemove) {
			AbstractPlayer p = AbstractDungeon.player;
			AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(p, p, ID));
		}
		shouldRemove = true;
	}

	public void updateDescription() {
		this.description = "The next time you gain block it is doubled.";
	}
}
