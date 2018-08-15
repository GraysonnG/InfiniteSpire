package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class BeetleShellPower extends AbstractPower {
	
	public BeetleShellPower(AbstractPlayer player, boolean shouldRemove) {
		this(player);
	}
	
	public BeetleShellPower(AbstractPlayer player) {
		this.owner = player;
		this.amount = 1;
		this.name = "Beetle Shell";
		this.ID = "is_BeetleShellPower";
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/beetleshell.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
        this.priority = 6;
	}
	
	@Override
	public float modifyBlock(float blockAmount) {
		return blockAmount * 2f;
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if(card.baseBlock > 0) {
			AbstractPlayer p = AbstractDungeon.player;
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, ID));
		}
	}

	public void updateDescription() {
		this.description = "The next time you gain block it is doubled.";
	}
}
