package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import infinitespire.relics.EvilPickle;
import javassist.CtBehavior;

@SpirePatch(clz= CombatRewardScreen.class, method="setupItemReward")
public class SetupItemRewardPatch {

	@SpireInsertPatch(locator = Locator.class)
	public static void evilPicklePatch(CombatRewardScreen rewardScreen) {
		if(AbstractDungeon.player.hasRelic(EvilPickle.ID) && AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
			AbstractDungeon.player.getRelic(EvilPickle.ID).onTrigger();
		}
	}

	public static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctBehavior) throws Exception {

			Matcher matcher = new Matcher.MethodCallMatcher(
				CombatRewardScreen.class, "positionRewards");

			return LineFinder.findInOrder(ctBehavior, matcher);
		}
	}
}
