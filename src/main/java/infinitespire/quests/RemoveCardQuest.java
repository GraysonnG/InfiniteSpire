package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;

public class RemoveCardQuest extends Quest {

	private static final Color COLOR = new Color(0f, 0.5f, 1f, 1f);
	private static final int REWARD_AMOUNT = 1;

	public RemoveCardQuest() {
		super(RemoveCardQuest.class.getName(), COLOR, 5, QuestType.BLUE, QuestRarity.COMMON);
	}

	@Override
	public void giveReward() {
		InfiniteSpire.gainVoidShards(REWARD_AMOUNT);
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/remove.png");
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
		return questStrings.TEXT[11];
	}

	@Override
	public Quest getCopy() {
		return new RemoveCardQuest();
	}
}
