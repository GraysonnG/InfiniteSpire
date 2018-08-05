package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;

public class FlawlessQuest extends Quest {

	public FlawlessQuest() {
		super(FlawlessQuest.class.getName(), new Color(1.0f, 1.0f, 0.0f, 1.0f), 1, QuestType.RED, QuestRarity.RARE);
	}

	@Override
	public void giveReward() {
		AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.RARE);
	}

	@Override
	public Quest createNew() {
		return this;
	}

	@Override
	public String getRewardString() {
		return "Receive a Random Rare Relic";
	}

	@Override
	public String getTitle() {
		return "Flawless the Boss";
	}

	@Override
	public Quest getCopy() {
		return new FlawlessQuest();
	}
}
