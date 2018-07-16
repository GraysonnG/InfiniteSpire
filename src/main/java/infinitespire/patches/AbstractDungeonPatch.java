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
import infinitespire.quests.Quest;
import infinitespire.relics.HolyWater;
import infinitespire.rooms.BlackGoopRoom;
import infinitespire.rooms.PerkRoom;
import infinitespire.screens.PerkScreen;
import infinitespire.util.SuperclassFinder;

public class AbstractDungeonPatch {
	
	@SpirePatch(cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon", method="closeCurrentScreen")
	public static class CloseCurrentScreen {
		public static void Prefix() {
			if(AbstractDungeon.screen == ScreenStatePatch.PERK_SCREEN) {
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
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "update")
	public static class Update {
		
		@SpireInsertPatch(rloc = 94)
		public static void Insert(AbstractDungeon __instance) {
			if(AbstractDungeon.screen == ScreenStatePatch.PERK_SCREEN)
				InfiniteSpire.perkscreen.update();
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "generateMap")
	public static class GenerateMap {
									//SL:637 = 36 right now
		@SpireInsertPatch(rloc = 36)// before AbstractDungeon.map = (ArrayList<ArrayList<MapRoomNode>>)RoomTypeAssigner.distributeRoomsAcrossMap(AbstractDungeon.mapRng, (ArrayList)AbstractDungeon.map, (ArrayList)roomList);
		public static void Insert() {
			//Settings.isEndless = true; // this needs to go in a better place
			AbstractDungeon.rareRelicPool.remove(HolyWater.ID);
			if(AbstractDungeon.bossCount >= 3 && AbstractDungeon.id.equals(Exordium.ID)) {
				
				AbstractDungeon.rareRelicPool.add(HolyWater.ID);
				Collections.shuffle(AbstractDungeon.rareRelicPool, new java.util.Random(AbstractDungeon.relicRng.randomLong()));
				
				InfiniteSpire.logger.info("Setting row 1 of map to PerkRoom.class");
				for(MapRoomNode node : AbstractDungeon.map.get(0)) {
					node.setRoom(new PerkRoom());
				}
			}
			
			for(int i = 0; i < 5; i ++) {
				InfiniteSpire.questLog.add(QuestHelper.createQuest("SlayQuest"));
			}
			
			InfiniteSpire.questLog.add(new infinitespire.quests.SlayQuest(Quest.createIDWithoutData("SlayQuest", 3, 0, java.awt.Color.RED) + "-" + com.megacrit.cardcrawl.monsters.exordium.Lagavulin.ID));
			
			InfiniteSpire.questLog.get(3).incrementQuestSteps();
			
			for(int j = 0; j < 6; j ++) {
				InfiniteSpire.logger.info((InfiniteSpire.questLog.get(j).getID()));
			}
			
			insertBlackGoopNode();
		}
		
		private static void insertBlackGoopNode() {
			
		}
	}
}
