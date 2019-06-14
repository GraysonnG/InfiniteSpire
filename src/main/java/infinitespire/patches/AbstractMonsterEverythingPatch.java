package infinitespire.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.interfaces.RageBarMonster;
import infinitespire.quests.OneTurnKillQuest;
import infinitespire.quests.SlayQuest;
import infinitespire.ragebar.RageBar;

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

	@SpirePatch(clz=AbstractMonster.class, method="update")
	public static class RageBarUpdatePatch {
		@SpirePostfixPatch
		public static void updateRageBar(AbstractMonster monster) {
			if(monster instanceof RageBarMonster) {
				RageBarMonster rageBarMonster = (RageBarMonster) monster;
				RageBar bar = rageBarMonster.getRageBar();
				if(bar != null) {
					bar.update();
				}
			}
		}
	}

	@SpirePatch(clz=AbstractMonster.class, method="render")
	public static class RageBarRenderPatch {
		@SpirePostfixPatch
		public static void renderRageBar(AbstractMonster monster, SpriteBatch sb) {
			if(monster instanceof RageBarMonster) {
				RageBarMonster rageBarMonster = (RageBarMonster) monster;
				RageBar bar = rageBarMonster.getRageBar();
				if(bar != null) {
					bar.render(sb);
				}
			}
		}
	}
}
