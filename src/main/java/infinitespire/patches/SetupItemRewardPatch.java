package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import infinitespire.relics.EvilPickle;
import infinitespire.rewards.VoidShardReward;
import javassist.CtBehavior;

@SpirePatch(clz= CombatRewardScreen.class, method="setupItemReward")
public class SetupItemRewardPatch {

	@SpireInsertPatch(locator = Locator.class)
	public static void itemRewardModifier(CombatRewardScreen rewardScreen) {
		if(AbstractDungeon.player.hasRelic(EvilPickle.ID) && AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
			AbstractDungeon.player.getRelic(EvilPickle.ID).onTrigger();
		}
		if(CardCrawlGame.isInARun() && AbstractDungeon.miscRng.randomBoolean(0.1f)){
			AbstractDungeon.combatRewardScreen.rewards.add(new VoidShardReward());
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
