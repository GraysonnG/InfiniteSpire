package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;

public class FlawlessQuest extends Quest {

	public static final int REWARD_AMOUNT = 3;

	public FlawlessQuest() {
		super(FlawlessQuest.class.getName(), new Color(1.0f, 1.0f, 0.0f, 1.0f), 1, QuestType.BLUE, QuestRarity.RARE);
	}

	@Override
	public void giveReward() {
		InfiniteSpire.gainVoidShards(REWARD_AMOUNT);
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/boss.png");
	}

	@Override
	public Quest createNew() {
		return this;
	}

	@Override
	public String getRewardString() {
		return voidShardStrings.TEXT[2] + REWARD_AMOUNT + voidShardStrings.TEXT[4];
	}

	@Override
	public String getTitle() {
		return questStrings.TEXT[8];
	}

	@Override
	public Quest getCopy() {
		return new FlawlessQuest();
	}
}
