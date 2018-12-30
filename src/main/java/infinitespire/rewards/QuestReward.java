package infinitespire.rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import infinitespire.InfiniteSpire;
import infinitespire.effects.QuestLogUpdateEffect;
import infinitespire.helpers.QuestHelper;
import infinitespire.patches.RewardItemTypeEnumPatch;
import infinitespire.util.TextureLoader;

public class QuestReward extends CustomReward {

	private static final Texture ICON = TextureLoader.getTexture("img/infinitespire/ui/topPanel/questLogIcon.png");

	public int amount;

	public QuestReward(int amount){
		super(ICON, "Add " + amount + " quests to your Quest Log", RewardItemTypeEnumPatch.QUEST);
		this.amount = amount;
	}

	@Override
	public boolean claimReward() {
		//AbstractDungeon.actionManager.addToTop(new AddQuestAction(QuestHelper.getRandomQuests(amount)));
		AbstractDungeon.topLevelEffects.add(new QuestLogUpdateEffect());
		InfiniteSpire.questLog.addAll(QuestHelper.getRandomQuests(amount));
		return true;
	}

	@Override
	public RewardSave createRewardSaveFromItem(RewardItem item) {
		return new RewardSave(item.type.toString(), null, ((QuestReward)item).amount, 0);
	}

	@Override
	public RewardItem createRewardItemFromSave(RewardSave save) {
		return new QuestReward(save.amount);
	}
}
