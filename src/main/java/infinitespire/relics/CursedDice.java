package infinitespire.relics;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DeathScreen;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.abstracts.Relic;
import infinitespire.quests.DieQuest;

public class CursedDice extends Relic {
	public static final String ID = InfiniteSpire.createID("CursedDice");
	private int cardsTillDeath = -1;
	private int playableCards = -1;
	private boolean initialActionTaken = false;

	public CursedDice() {
		super(ID, "curseddice", RelicTier.RARE, LandingSound.SOLID);
	}

	public void setCounter(int counter) {
		if (counter == -2) {
			this.img = InfiniteSpire.getTexture("img/infinitespire/relics/curseddice-used.png");
			this.counter = -2;
		}
	}

	public boolean isActive(){
		return this.initialActionTaken && counter > -2;
	}

	@Override
	public void onVictory() {
		if(initialActionTaken && counter > -2) {
			this.setCounter(-2);
			this.doHeal();
			this.stopPulse();
			this.pulse = false;
			AbstractDungeon.player.tint.changeColor(Color.WHITE.cpy());
		}
	}

	@Override
	public void onTrigger() {
		GameActionManager manager = AbstractDungeon.actionManager;
		AbstractPlayer player = AbstractDungeon.player;

		this.flash();
		if(!initialActionTaken) {
			//shuffle all cards to drawPile
			manager.addToBottom(new ShuffleAllAction());
			manager.addToBottom(new ShuffleAction(player.drawPile, false));

			//set the counter
			cardsTillDeath = player.drawPile.group.size();
			cardsTillDeath += player.hand.group.size();
			cardsTillDeath += player.discardPile.group.size();
			cardsTillDeath += player.limbo.group.size();
			this.counter = cardsTillDeath;
			this.playableCards = cardsTillDeath;

			//draw cards if lethal damage is taken on player turn
			if(!manager.turnHasEnded) {
				int cardsToDraw = 5;
				//TODO: figure out how many cards to draw
				manager.addToBottom(new DrawCardAction(player, cardsToDraw));
			}

			//heal the player to prevent stupid death logic
			player.heal(1);


			initialActionTaken = true;

			for(Quest q : InfiniteSpire.questLog) {
				if(q instanceof DieQuest){
					q.incrementQuestSteps();
				}
			}

			this.beginPulse();
			this.pulse = true;
		}
	}

	public void update(){
		super.update();
		if(CardCrawlGame.isInARun()) {
			AbstractPlayer player = AbstractDungeon.player;
			if (!player.isDead && isActive()) {
				if (cardsTillDeath == 0) {
					player.currentHealth = 0;
					AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
					player.isDead = true;
				}

				if (this.counter != cardsTillDeath || this.counter <= -1) {
					this.flash();
					this.counter = cardsTillDeath;
				}

				cardsTillDeath = player.drawPile.group.size();
				cardsTillDeath += player.hand.group.size();
				cardsTillDeath += player.discardPile.group.size();
				cardsTillDeath += player.limbo.group.size();

				Color tint = Color.WHITE.cpy();
				tint.g = (float)cardsTillDeath / (float)playableCards;
				tint.r = 1.0f;
				tint.b = (float)cardsTillDeath / (float)playableCards;

				player.tint.changeColor(tint);
			}
			if (this.counter == -2) {
				this.img = InfiniteSpire.getTexture("img/infinitespire/relics/curseddice-used.png");
			}
		}
	}

	private void doHeal() {
		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		int healAmt = (int) (AbstractDungeon.player.maxHealth * 0.35f);
		if(AbstractDungeon.player.hasBlight("FullBelly")) {
			healAmt /= 2;
		}
		if(healAmt < 1){
			healAmt = 1;
		}
		AbstractDungeon.player.heal(healAmt, true);
	}

	public void moveCardToExhaust(AbstractCard card, boolean visualOnly) {
		if(isActive()) {
			if(!visualOnly) {
				AbstractDungeon.player.discardPile.moveToExhaustPile(card);
			}
		}
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		if(isActive()) return 0;

		return super.onAttacked(info, damageAmount);
	}
}
