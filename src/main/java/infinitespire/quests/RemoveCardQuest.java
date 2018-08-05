package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.InfiniteSpire;
import infinitespire.helpers.QuestHelper;

public class RemoveCardQuest extends Quest {
	
	private static final Color COLOR = new Color(0f, 1f, 0.25f, 1f);
	public int cost;
	
	public RemoveCardQuest() {
		super(RemoveCardQuest.class.getName(), COLOR, 5, QuestType.GREEN, QuestRarity.COMMON);
	}

	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		AbstractDungeon.player.gainGold(this.cost);
	}

	@Override
	public Quest createNew() {
		this.cost = QuestHelper.makeRandomCost(165);
		return this;
	}

	@Override
	public String getRewardString() {
		return this.cost + "g";
	}

	@Override
	public String getTitle() {
		return "Remove 5 Cards";
	}

	@Override
	public Quest getCopy() {
		return new RemoveCardQuest();
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.cards.CardGroup", method="removeCard", paramtypes = {"com.megacrit.cardcrawl.cards.AbstractCard"})
	public static class PostRemoveCardHook {
		public static void Prefix() {
			for(Quest q : InfiniteSpire.questLog) {
				InfiniteSpire.logger.info(q.id);
				if(q instanceof RemoveCardQuest) {
					q.incrementQuestSteps();
				}
			}
		}
	}
}
