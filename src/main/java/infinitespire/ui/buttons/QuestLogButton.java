package infinitespire.ui.buttons;

import basemod.TopPanelItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import infinitespire.InfiniteSpire;
import infinitespire.patches.ScreenStatePatch;

public class QuestLogButton extends TopPanelItem {
	private static final Texture IMG = InfiniteSpire.getTexture("img/infinitespire/ui/topPanel/questLogIcon.png");
	public static final String ID = "is_QuestLog";

	public QuestLogButton() {
		super(IMG, ID);
	}

	@Override
	protected void onClick() {
		if(!CardCrawlGame.isPopupOpen){
			toggleQuestLogScreen();
		}
	}

	@Override
	public void update() {
		super.update();
		//unhardcode hotkey to make it set-able
		if(!CardCrawlGame.isPopupOpen && Gdx.input.isKeyJustPressed(Input.Keys.Q)){
			toggleQuestLogScreen();
		}


		this.image = InfiniteSpire.questLog.hasUpdate ? InfiniteSpire.getTexture("img/infinitespire/ui/topPanel/questLogIcon-alert.png") :
			InfiniteSpire.getTexture("img/infinitespire/ui/topPanel/questLogIcon.png");
	}

	private static void toggleQuestLogScreen(){
		if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
			AbstractDungeon.closeCurrentScreen();
			InfiniteSpire.questLogScreen.open();
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
		}
		else if (!AbstractDungeon.isScreenUp) {
			InfiniteSpire.questLogScreen.open();
		}
		else if (AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN) {
			if (InfiniteSpire.questLogScreen.openedDuringReward) {
				InfiniteSpire.questLogScreen.openedDuringReward = false;
				AbstractDungeon.combatRewardScreen.reopen();
			}
			else {
				AbstractDungeon.closeCurrentScreen();
				CardCrawlGame.sound.play("DECK_CLOSE");
			}
		}
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DEATH) {
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.DEATH;
			AbstractDungeon.deathScreen.hide();
			InfiniteSpire.questLogScreen.open();
		}
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.BOSS_REWARD) {
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.BOSS_REWARD;
			AbstractDungeon.bossRelicScreen.hide();
			InfiniteSpire.questLogScreen.open();
		}
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SHOP) {
			AbstractDungeon.overlayMenu.cancelButton.hide();
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
			InfiniteSpire.questLogScreen.open();
		}
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && !AbstractDungeon.dungeonMapScreen.dismissable) {
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.MAP;
			InfiniteSpire.questLogScreen.open();
		}
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SETTINGS || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
			if (AbstractDungeon.previousScreen != null) {
				AbstractDungeon.screenSwap = true;
			}
			AbstractDungeon.closeCurrentScreen();
			InfiniteSpire.questLogScreen.open();
		}
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.INPUT_SETTINGS) {
			if (AbstractDungeon.previousScreen != null) {
				AbstractDungeon.screenSwap = true;
			}
			AbstractDungeon.closeCurrentScreen();
			InfiniteSpire.questLogScreen.open();
		}
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD) {
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.CARD_REWARD;
			AbstractDungeon.dynamicBanner.hide();
			InfiniteSpire.questLogScreen.open();
		}
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.GRID;
			AbstractDungeon.gridSelectScreen.hide();
			InfiniteSpire.questLogScreen.open();
		}
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT) {
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.HAND_SELECT;
			InfiniteSpire.questLogScreen.open();
		}
		InputHelper.justClickedLeft = false;
	}
}
