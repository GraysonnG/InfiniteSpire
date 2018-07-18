package infinitespire.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.patches.ScreenStatePatch;
import infinitespire.quests.QuestLog;

public class QuestLogScreen {
	
	QuestLog gameQuestLog;
	
	public QuestLogScreen(QuestLog log) {
		gameQuestLog = log;
	}

	public void render(SpriteBatch sb) {
		
		
	}

	public void update() {
		
	}

	public void open() {
		gameQuestLog.hasUpdate = false;
		AbstractDungeon.screen = ScreenStatePatch.QUEST_LOG_SCREEN;
		AbstractDungeon.overlayMenu.showBlackScreen();
		AbstractDungeon.overlayMenu.proceedButton.hide();
		AbstractDungeon.isScreenUp = true;
	}
}
