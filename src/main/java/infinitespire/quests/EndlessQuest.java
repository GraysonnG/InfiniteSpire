package infinitespire.quests;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

@Deprecated
public class EndlessQuest extends Quest {

	public EndlessQuest() {
		super(EndlessQuest.class.getName() + "-1-0-255,100,255-null");
	}

	@Override
	protected String generateID() {
		return EndlessQuest.class.getName() + "-1-0-255,100,255-null";
	}

	@Override
	protected void giveReward() {
		Settings.isEndless = true;
		AbstractDungeon.topPanel = new TopPanel();
		CardCrawlGame.sound.play("UNLOCK_PING");
	}
}
