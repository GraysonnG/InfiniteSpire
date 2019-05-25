package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.QuestHelper;

public class OneTurnKillQuest extends Quest {

	private static final Color COLOR = new Color(1f, 0, 0.5f, 1.0f);
	private static final int REWARD_AMOUNT = 1;
	public int cost;
	
	public OneTurnKillQuest() {
		super(OneTurnKillQuest.class.getName(), COLOR, 1, QuestType.RED, QuestRarity.RARE);
	}

	@Override
	public void giveReward() {
		InfiniteSpire.gainVoidShards(REWARD_AMOUNT);
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/elite.png");
	}

	@Override
	public Quest createNew() {
		this.cost = QuestHelper.makeRandomCost(300);
		return this;
	}

	@Override
	public String getRewardString() {
		return voidShardStrings.TEXT[2] + REWARD_AMOUNT + voidShardStrings.TEXT[4];
	}

	@Override
	public String getTitle() {
		return questStrings.TEXT[9];
	}

	@Override
	public Quest getCopy() {
		return new OneTurnKillQuest();
	} 
}
