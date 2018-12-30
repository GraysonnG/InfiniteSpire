package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import infinitespire.rewards.BlackCardRewardItem;
import infinitespire.rewards.CustomReward;
import infinitespire.rewards.QuestReward;
import javassist.CtBehavior;
import org.apache.logging.log4j.Logger;

public class CustomRewardPatch {
	@SpirePatch(clz = CardCrawlGame.class, method = "loadPostCombat", paramtypez = {SaveFile.class})
	public static class LoadPostCombat {

		@SuppressWarnings("unused")
		@SpireInsertPatch(
			locator = Locator.class,
			localvars = {"i"}
		)
		public static void Insert(CardCrawlGame __instance, SaveFile saveFile, RewardSave rewardSave){
			if(rewardSave.type.equals(RewardItemTypeEnumPatch.QUEST.toString())){
				AbstractDungeon.getCurrRoom().rewards.add(new QuestReward(rewardSave.amount));
			}

			if(rewardSave.type.equals(RewardItemTypeEnumPatch.BLACK_CARD.toString())){
				AbstractDungeon.getCurrRoom().rewards.add(new BlackCardRewardItem());
			}
		}

		public static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws Exception {
				Matcher matcher = new Matcher.MethodCallMatcher(Logger.class, "info");
				return LineFinder.findInOrder(ctBehavior, matcher);
			}
		}
	}

	@SpirePatch(clz = SaveFile.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {SaveFile.SaveType.class})
	public static class SaveFileConstructor {

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(SaveFile __instance, SaveFile.SaveType saveType) {
			for(RewardItem item : AbstractDungeon.getCurrRoom().rewards) {
				if(item instanceof CustomReward){
					CustomReward customItem = (CustomReward)item;
					switch(customItem.type.toString()){
						case "QUEST":
						case "BLACK_CARD":
							__instance.combat_rewards.add(customItem.createRewardSaveFromItem(item));
					}
				}
			}
		}

		public static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws Exception {
				Matcher matcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "rewards");
				return LineFinder.findInOrder(ctBehavior, matcher);
			}
		}
	}
}
