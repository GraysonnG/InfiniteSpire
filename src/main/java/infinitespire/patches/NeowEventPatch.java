package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.blights.TimeMaze;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;

public class NeowEventPatch {

	public static final String CLS = "com.megacrit.cardcrawl.neow.NeowEvent";

	@SpirePatch(cls = CLS, method = "uniqueBlight")
	public static class BadBlight {

		@SpireInsertPatch(rloc = 1, localvars = {"temp"})
		public static void InsertNewCheck(NeowEvent __insance, @ByRef AbstractBlight[] temp){
			temp[0] = AbstractDungeon.player.getBlight("TimeMaze");
		}

		@SpireInsertPatch(rloc = 33)
		public static SpireReturn<Void> InsertReturnMethod(NeowEvent __insance){
			AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new TimeMaze());
			return SpireReturn.Return(null);
		}
	}
}
