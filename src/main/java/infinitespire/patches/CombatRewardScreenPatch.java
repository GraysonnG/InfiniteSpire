package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import infinitespire.monsters.LordOfAnnihilation;
import infinitespire.rewards.BlackCardRewardItem;
import javassist.CtBehavior;

public class CombatRewardScreenPatch {

	@SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
	public static class SetupItemReward {
		@SpireInsertPatch(locator = Locator.class,
			localvars = {"cardReward"})
		public static void convertToBlackReward(CombatRewardScreen __instance, @ByRef RewardItem[] cardReward) {
			if (AbstractDungeon.bossKey.equals(LordOfAnnihilation.ID)) {
				if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
					cardReward[0] = new BlackCardRewardItem();
				}
			}
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethod) throws Exception {

				Matcher matcher = new Matcher.FieldAccessMatcher(RewardItem.class, "cards");

				return LineFinder.findAllInOrder(ctMethod, matcher);
			}
		}
	}
}
