package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import infinitespire.blights.InfectiousMalware;

public class NeowEventPatch {

	@SpirePatch(clz = NeowEvent.class, method = "uniqueBlight")
	public static class BadBlight {
		//TODO convert to Locator
		@SpireInsertPatch(rloc = 1, localvars = {"temp"})
		public static void InsertNewCheck(NeowEvent __instance, @ByRef AbstractBlight[] temp){
			temp[0] = AbstractDungeon.player.getBlight(InfectiousMalware.ID);
		}

		@SpireInsertPatch(rloc = 33)
		public static SpireReturn<Void> InsertReturnMethod(NeowEvent __instance){
			AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new InfectiousMalware());
			return SpireReturn.Return(null);
		}
	}
}
