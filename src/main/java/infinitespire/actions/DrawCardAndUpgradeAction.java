package infinitespire.actions;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;

public class DrawCardAndUpgradeAction extends AbstractGameAction {

	private boolean shuffleCheck;

	public DrawCardAndUpgradeAction(AbstractCreature source, int amount) {
		this.shuffleCheck = false;
		if(AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID)) {
			AbstractDungeon.player.getPower(NoDrawPower.POWER_ID).flash();
			this.setValues(AbstractDungeon.player, source, amount);
			this.isDone = true;
			this.duration = 0.0f;
			this.actionType = ActionType.WAIT;
			return;
		}
		this.setValues(AbstractDungeon.player, source, amount);
		this.actionType = ActionType.DRAW;
		if(Settings.FAST_MODE) {
			this.duration = Settings.ACTION_DUR_XFAST;
		}else {
			this.duration = Settings.ACTION_DUR_FASTER;
		}
	}
	
	@Override
	public void update() {
		AbstractPlayer player = AbstractDungeon.player;
		
		if(this.amount <= 0) {
			this.isDone = true;
			return;
		}
		
		CardGroup drawPile = player.drawPile;
		CardGroup discardPile = player.discardPile;
		
		int deckSize = drawPile.size();
		int discardSize = discardPile.size();
		
		if(SoulGroup.isActive()) {
			return;
		}
		
		if(deckSize + discardSize == 0) {
			this.isDone = true;
			return;
		}
		
		if(player.hand.size() == BaseMod.MAX_HAND_SIZE) {
			player.createHandIsFullDialog();
			this.isDone = true;
			return;
		}
		
		if(!this.shuffleCheck) {
			if(this.amount > deckSize) {
				int tmp = this.amount - deckSize;
				AbstractDungeon.actionManager.addToTop(new DrawCardAndUpgradeAction(player, tmp));
				AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
				if(deckSize != 0) {
					AbstractDungeon.actionManager.addToTop(new DrawCardAndUpgradeAction(player, deckSize));
				}
				this.amount = 0;
				this.isDone = true;
			}
			this.shuffleCheck = true;
		}
		
		this.duration -= Gdx.graphics.getDeltaTime();
		
		if(this.amount != 0 && this.duration < 0.0f) {
			if(Settings.FAST_MODE) {
				this.duration = Settings.ACTION_DUR_XFAST;
			}else {
				this.duration = Settings.ACTION_DUR_FASTER;
			}
			--this.amount;
			if(!drawPile.isEmpty()) {
				if(drawPile.getTopCard().canUpgrade()) {
					drawPile.getTopCard().upgrade();
				}
				player.draw();
				player.hand.refreshHandLayout();
			}
		}
	}
}
