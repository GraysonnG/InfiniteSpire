package infinitespire.patches;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.map.MapRoomNode;

import infinitespire.InfiniteSpire;
import infinitespire.helpers.QuestHelper;
import infinitespire.quests.*;
import infinitespire.relics.HolyWater;
import infinitespire.rooms.PerkRoom;
import infinitespire.screens.PerkScreen;
import infinitespire.util.SuperclassFinder;

public class AbstractDungeonPatch {
	
	@SpirePatch(cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon", method="closeCurrentScreen")
	public static class CloseCurrentScreen {
		public static void Prefix() {
			if(AbstractDungeon.screen == ScreenStatePatch.PERK_SCREEN || AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN) {
				PerkScreen.isDone = true;
				
					try {
						Method overlayReset = SuperclassFinder.getSuperClassMethod(AbstractDungeon.class, "genericScreenOverlayReset");
						overlayReset.setAccessible(true);
						overlayReset.invoke(AbstractDungeon.class);
					} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
			
				AbstractDungeon.overlayMenu.hideBlackScreen();
			}
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "render")
	public static class Render {
		
		@SpireInsertPatch(rloc = 111) //112
		public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
			if(AbstractDungeon.screen == ScreenStatePatch.PERK_SCREEN)
				InfiniteSpire.perkscreen.render(sb);
			if(AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN)
				InfiniteSpire.questLogScreen.render(sb);
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "update")
	public static class Update {
		
		@SpireInsertPatch(rloc = 94)
		public static void Insert(AbstractDungeon __instance) {
			if(AbstractDungeon.screen == ScreenStatePatch.PERK_SCREEN)
				InfiniteSpire.perkscreen.update();
			if(AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN)
				InfiniteSpire.questLogScreen.update();
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "generateMap")
	public static class GenerateMap {
									//SL:637 = 36 right now
		@SpireInsertPatch(rloc = 36)// before AbstractDungeon.map = (ArrayList<ArrayList<MapRoomNode>>)RoomTypeAssigner.distributeRoomsAcrossMap(AbstractDungeon.mapRng, (ArrayList)AbstractDungeon.map, (ArrayList)roomList);
		public static void Insert() {
			addHolyWaterToRareRelicPool();
			insertPerkRooms();
			addInitialQuests();
			insertBlackGoopNode();
		}
		
		private static void addInitialQuests() {
			if(AbstractDungeon.floorNum <= 1 && InfiniteSpire.questLog.isEmpty()) {
				InfiniteSpire.questLog.clear();
				//InfiniteSpire.questLog.add(new EndlessQuest());
				InfiniteSpire.questLog.add(new EndlessQuestPart1());
//				InfiniteSpire.questLog.add(new DieQuest());
//				InfiniteSpire.questLog.add(new OneTurnKillQuest());
//				InfiniteSpire.questLog.add(new FlawlessQuest());
				InfiniteSpire.questLog.addAll(QuestHelper.getRandomQuests(3));
				
				for(int j = 0; j < InfiniteSpire.questLog.size(); j ++) {
					InfiniteSpire.logger.info((InfiniteSpire.questLog.get(j).getID()));
				}
			}
		}
		
		private static void addHolyWaterToRareRelicPool() {
			AbstractDungeon.rareRelicPool.remove(HolyWater.ID);
			if(AbstractDungeon.bossCount >= 3 && AbstractDungeon.id.equals(Exordium.ID)) {
				AbstractDungeon.rareRelicPool.add(HolyWater.ID);
				Collections.shuffle(AbstractDungeon.rareRelicPool, new java.util.Random(AbstractDungeon.relicRng.randomLong()));
			}
		}
	
		private static void insertPerkRooms() {
			if(AbstractDungeon.bossCount >= 3 && AbstractDungeon.id.equals(Exordium.ID)) {
				InfiniteSpire.logger.info("Setting row 1 of map to PerkRoom.class");
				for(MapRoomNode node : AbstractDungeon.map.get(0)) {
					node.setRoom(new PerkRoom());
				}
			}
		}
		
		private static void insertBlackGoopNode() {
			
		}
	}
}
