package infinitespire.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
		AbstractDungeon.topLevelEffects.add(new QuestLogUpdateEffect());
		InfiniteSpire.questLog.addAll(QuestHelper.getRandomQuests(amount));
		return true;
	}
}
