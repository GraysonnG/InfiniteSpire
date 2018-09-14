package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.quests.OneTurnKillQuest;
import infinitespire.quests.SlayQuest;

public class AbstractMonsterEverythingPatch {
	@SpirePatch(cls="com.megacrit.cardcrawl.monsters.AbstractMonster", method="die", paramtypes={"boolean"})
	public static class Die {
		public static void Prefix(AbstractMonster __instance, boolean trigger) {
			if(AbstractDungeon.getMonsters().monsters.size() == 1) {
				AbstractRoom room = AbstractDungeon.getCurrRoom();
			}
			
			for(Quest quest : InfiniteSpire.questLog) {
				if(quest instanceof SlayQuest) {
					((SlayQuest) quest).onEnemyKilled(__instance);
				}
				
				if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite && __instance.type == EnemyType.ELITE && GameActionManager.turn <= 1 && quest instanceof OneTurnKillQuest) {
					quest.incrementQuestSteps();
				}
			}
		}
	}
}
