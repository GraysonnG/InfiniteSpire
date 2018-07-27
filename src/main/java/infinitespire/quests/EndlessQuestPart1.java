package infinitespire.quests;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import infinitespire.InfiniteSpire;

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
	public void giveReward() {
		Settings.isEndless = true;
		InfiniteSpire.isEndless = true;
		//add the next quest in the questline
		CardCrawlGame.sound.play("UNLOCK_PING");
	}
	
	public void onEnemyKilled(AbstractCreature creature) {
		
		if(creature.id.equalsIgnoreCase("Nightmare")) {
			InfiniteSpire.logger.info(creature.id);
			this.incrementQuestSteps();
			InfiniteSpire.logger.info(this.questSteps);
		}
	}

	@Override
	public String getTitle() {
		return "Kill the Nightmare";
	}

	@Override
	public String getRewardString() {
		return "Unlock Endless";
	}

	@Override
	public int getCost(String s) {
		return 0;
	}
}
