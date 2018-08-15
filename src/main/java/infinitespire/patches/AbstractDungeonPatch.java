package infinitespire.patches;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.green.Nightmare;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

import infinitespire.InfiniteSpire;
import infinitespire.helpers.QuestHelper;
import infinitespire.quests.endless.EndlessQuestPart1;
import infinitespire.relics.HolyWater;
import infinitespire.rooms.NightmareEliteRoom;

public class AbstractDungeonPatch {
	
	@SpirePatch(cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon", method="closeCurrentScreen")
	public static class CloseCurrentScreen {
		public static void Prefix() {
			if(AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN) {
					try {
						Method overlayReset = AbstractDungeon.class.getDeclaredMethod("genericScreenOverlayReset");
						overlayReset.setAccessible(true);
						overlayReset.invoke(AbstractDungeon.class);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				AbstractDungeon.overlayMenu.hideBlackScreen();
				InfiniteSpire.questLogScreen.close();
			}
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "nextRoomTransition")
	public static class NextRoomTransition {
		@SpireInsertPatch(rloc = 10) 
		public static SpireReturn<?> Insert(AbstractDungeon __instance){
			if(AbstractDungeon.getCurrRoom() instanceof NightmareEliteRoom && AbstractDungeon.eliteMonsterList.size() <= 0) {
				AbstractDungeon.eliteMonsterList.add(Nightmare.ID);
				return SpireReturn.Continue();
			}
			
			return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "render")
	public static class Render {
		
		@SpireInsertPatch(rloc = 111) //112
		public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
			if(AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN)
				InfiniteSpire.questLogScreen.render(sb);
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "update")
	public static class Update {
		
		@SpireInsertPatch(rloc = 94)
		public static void Insert(AbstractDungeon __instance) {
			if(AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN)
				InfiniteSpire.questLogScreen.update();
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "generateMap")
	public static class GenerateMap {
									//SL:637 = 36 right now
		@SpireInsertPatch(rloc = 37)// after AbstractDungeon.map = (ArrayList<ArrayList<MapRoomNode>>)RoomTypeAssigner.distributeRoomsAcrossMap(AbstractDungeon.mapRng, (ArrayList)AbstractDungeon.map, (ArrayList)roomList);
		public static void Insert() {
			Settings.isEndless = InfiniteSpire.isEndless;
			addHolyWaterToRareRelicPool();
			addInitialQuests();
			for(int i = 0; i < 3; i++) {
				insertNightmareNode();
			}
		}
		
		private static void addInitialQuests() {
			InfiniteSpire.logger.info("adding initial quests");
			if(AbstractDungeon.floorNum <= 1 &&  InfiniteSpire.questLog.isEmpty()) {
				InfiniteSpire.questLog.add(new EndlessQuestPart1().createNew());
				InfiniteSpire.questLog.addAll(QuestHelper.getRandomQuests(9));
				InfiniteSpire.questLog.markAllQuestsAsSeen();
				QuestHelper.saveQuestLog();
			}
		}
		
		private static void addHolyWaterToRareRelicPool() {
			AbstractDungeon.rareRelicPool.remove(HolyWater.ID);
			if(AbstractDungeon.bossCount >= 3 && AbstractDungeon.id.equals(Exordium.ID)) {
				AbstractDungeon.rareRelicPool.add(HolyWater.ID);
				Collections.shuffle(AbstractDungeon.rareRelicPool, new java.util.Random(AbstractDungeon.relicRng.randomLong()));
			}
		}
		
		private static void insertNightmareNode() {
			if(AbstractDungeon.bossCount < 1) return;
			
			int rand;
			ArrayList<MapRoomNode> eliteNodes = new ArrayList<MapRoomNode>();
			
			for(ArrayList<MapRoomNode> rows : AbstractDungeon.map) {
				for(MapRoomNode node : rows) {
					if(node.room != null && node.room instanceof MonsterRoomElite) {
						eliteNodes.add(node);
					}
				}
			}
			rand = AbstractDungeon.mapRng.random(eliteNodes.size() - 1);
			eliteNodes.get(rand).setRoom(new NightmareEliteRoom());
		}
	}
}
