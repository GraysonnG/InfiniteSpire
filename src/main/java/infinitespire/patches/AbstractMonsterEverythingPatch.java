package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;
import infinitespire.helpers.QuestHelper;
import infinitespire.quests.Quest;

public class AbstractMonsterEverythingPatch {
	@SpirePatch(cls="com.megacrit.cardcrawl.monsters.AbstractMonster", method="die", paramtypes={"boolean"})
	public static class Die {
		public static void Prefix(AbstractMonster __instance, boolean trigger) {
			InfiniteSpire.logger.info(__instance.name + " died!");
			for(Quest quest : InfiniteSpire.questLog) {
				quest.onEnemyKilled(__instance);
			}
			
			if(__instance.type == AbstractMonster.EnemyType.BOSS) {
				InfiniteSpire.questLog.addAll(QuestHelper.getRandomQuests(3));
			}
		}
	}
}
