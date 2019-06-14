package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import infinitespire.events.EndRunEvent;
import infinitespire.rooms.EndRunRoom;
import javassist.CtBehavior;

public class GoToEndGamePatch {
	@SpirePatch(clz = ProceedButton.class, method = "goToNextDungeon")
	public static class GoToNextDungeonPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(ProceedButton __instance, AbstractRoom room) {
			if(CardCrawlGame.dungeon instanceof TheBeyond && Settings.isEndless && AbstractDungeon.bossCount >= 8) {
				AbstractDungeon.currMapNode.room = new EndRunRoom(AbstractDungeon.currMapNode.room);
				AbstractDungeon.getCurrRoom().onPlayerEntry();
				AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;

				AbstractDungeon.combatRewardScreen.clear();
				AbstractDungeon.previousScreen = null;
				AbstractDungeon.closeCurrentScreen();

				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}

		private static class Locator extends SpireInsertLocator
		{
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
			{
				Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "fadeOut");
				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			}
		}
	}

	@SpirePatch(clz = TreasureRoomBoss.class, method = "getNextDungeonName")
	public static class NextDungeonPatch {

		@SpirePrefixPatch
		public static SpireReturn<String> fixEndlessDungeonPick(TreasureRoomBoss __instance) {
			if(Settings.isEndless && EndRunEvent.shouldEndRun) {
				return SpireReturn.Return(TheEnding.ID);
			}
			return SpireReturn.Continue();
		}
	}
}

