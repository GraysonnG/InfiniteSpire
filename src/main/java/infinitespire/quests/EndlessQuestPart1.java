package infinitespire.quests;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

import infinitespire.rooms.BlackGoopRoom;

public class EndlessQuestPart1 extends Quest {

	/**
	 * This is a temporary solution to enable endless
	 */
	public EndlessQuestPart1() {
		super(EndlessQuestPart1.class.getName() + "-1-0-255,100,255-null-0");
	}
	
	public EndlessQuestPart1(String questID) {
		this();
	}

	@Override
	protected String generateID() {
		return EndlessQuestPart1.class.getName() + "-1-0-255,100,255-null-0";
	}

	@Override
	protected void giveReward() {
		Settings.isEndless = true;
		AbstractDungeon.topPanel = new TopPanel();
		CardCrawlGame.sound.play("UNLOCK_PING");
	}
	
	@Override
	public void onRoomEntered(AbstractRoom room) {
		if(room.getClass().getName().equals(BlackGoopRoom.class.getName())) {
			this.incrementQuestSteps();
		}
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Endless Pt.1";
	}

	@Override
	public String getRewardString() {
		return "THIS QUEST IS NOT IMPLEMENTED";
	}

	@Override
	public int getCost(String s) {
		return 0;
	}
}
