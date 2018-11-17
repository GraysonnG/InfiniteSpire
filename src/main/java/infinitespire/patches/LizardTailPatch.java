package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.relics.LizardTail;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.quests.DieQuest;

public class LizardTailPatch {

	@SpirePatch(clz= LizardTail.class, method = "onTrigger")
	public static class DieQuestHook {
		@SpirePostfixPatch
		public static void onDeath(LizardTail __instance) {
			for(Quest q : InfiniteSpire.questLog) {
				if(q instanceof DieQuest) {
					((DieQuest)q).onPlayerDie();
				}
			}
		}
	}
}
