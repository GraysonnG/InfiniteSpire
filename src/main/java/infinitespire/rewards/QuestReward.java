package infinitespire.rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Reward;
import infinitespire.effects.QuestLogUpdateEffect;
import infinitespire.helpers.QuestHelper;
import infinitespire.patches.RewardItemTypeEnumPatch;
import infinitespire.util.TextureLoader;

public class QuestReward extends Reward {

	private static final Texture ICON = TextureLoader.getTexture("img/infinitespire/ui/topPanel/questLogIcon.png");

	public int amount;

	public QuestReward(int amount){
		super(ICON, rewardStrings.TEXT[3] + amount + rewardStrings.TEXT[4], RewardItemTypeEnumPatch.QUEST);
		this.amount = amount;
	}

	@Override
	public boolean claimReward() {
		AbstractDungeon.topLevelEffects.add(new QuestLogUpdateEffect());
		InfiniteSpire.questLog.addAll(QuestHelper.getRandomQuests(amount));
		return true;
	}
}
