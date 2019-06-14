package infinitespire.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import infinitespire.InfiniteSpire;
import infinitespire.avhari.AvhariRoom;
import infinitespire.helpers.QuestHelper;
import infinitespire.monsters.LordOfFortification;
import infinitespire.quests.endless.EndlessQuestPart1;
import infinitespire.relics.BlackEgg;
import infinitespire.relics.BottledSoul;
import infinitespire.relics.HolyWater;
import infinitespire.rooms.NightmareEliteRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

public class AbstractDungeonPatch {

	public static final String CLS = "com.megacrit.cardcrawl.dungeons.AbstractDungeon";

	//TODO Delete this
	@SpirePatch(cls = CLS, method="closeCurrentScreen")
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

	@SpirePatch(cls = CLS, method = "populatePathTaken")
	public static class PopulatePathTaken {

		@SpireInsertPatch(
			locator = PopPathLocator.class
		)
		public static void nightmareRoomPatch(AbstractDungeon __instance, SaveFile saveFile) {

			System.out.println(saveFile.current_room);

			if(saveFile.current_room.equals(NightmareEliteRoom.class.getName())){
				AbstractDungeon.currMapNode = new MapRoomNode(0, -1);
				AbstractDungeon.currMapNode.room = new NightmareEliteRoom();
			}


			System.out.println(AbstractDungeon.getCurrRoom().getClass().getName());
		}

		public static class PopPathLocator extends SpireInsertLocator{
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException{
				Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "nextRoomTransition");
				return LineFinder.findInOrder(ctBehavior, matcher);
			}
		}
	}

	@SpirePatch(cls = CLS, method = "returnEndRandomRelicKey")
	public static class BottledSoulFilter{
		@SpirePostfixPatch
		public static String Postfix(String retVal, AbstractRelic.RelicTier tier) {
			if(retVal.equals(BottledSoul.ID)) {
				boolean hasExhaust = false;
				for(AbstractCard card : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group) {
					if(card.exhaust){
						hasExhaust = true;
						break;
					}
				}
				if(!hasExhaust) return AbstractDungeon.returnEndRandomRelicKey(tier);

				return retVal;
			}
			return retVal;
		}
	}

	//TODO: Move to postfix to allow rendering on top of other screens
	@SpirePatch(cls = CLS, method = "render")
	public static class Render {

		@SpireInsertPatch(locator = RenderLocator.class)
		public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
			if(AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN)
				InfiniteSpire.questLogScreen.render(sb);
		}

		private static class RenderLocator extends SpireInsertLocator{
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.FieldAccessMatcher(
					"com.megacrit.cardcrawl.dungeons.AbstractDungeon", "screen");

				return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
			}
		}
	}

	//TODO: Move to prefix spire return to allow ignoring inputs on other screens
	@SpirePatch(cls = CLS, method = "update")
	public static class Update {

		@SpireInsertPatch(locator = UpdateLocator.class)
		public static void Insert(AbstractDungeon __instance) {
			if(AbstractDungeon.screen == ScreenStatePatch.QUEST_LOG_SCREEN)
				InfiniteSpire.questLogScreen.update();
		}

		private static class UpdateLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.FieldAccessMatcher(
					AbstractDungeon.class, "turnPhaseEffectActive"
				);

				int[] line = LineFinder.findInOrder(ctBehavior, matcher);

				return line;
			}
		}
	}

	@SpirePatch(cls = CLS, method = "setBoss")
	public static class SetBoss {

		@SpirePrefixPatch
		public static SpireReturn<?> LordBossSpawner(AbstractDungeon __instance, String key){
			if(shouldSpawn()){
				DungeonMap.boss = InfiniteSpire.Textures.getUITexture("map/bossIcon.png");
				DungeonMap.bossOutline = InfiniteSpire.Textures.getUITexture("map/bossIcon-outline.png");
				int bossToSpawn = AbstractDungeon.miscRng.random(2);
				switch (bossToSpawn) {
					case 0:
						AbstractDungeon.bossKey = LordOfFortification.ID;
					case 1:
						AbstractDungeon.bossKey = LordOfFortification.ID;
					case 2:
						AbstractDungeon.bossKey = LordOfFortification.ID;
				}

				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}

		public static boolean shouldSpawn(){
			boolean shouldSpawn = AbstractDungeon.bossCount == 8;
			if(AbstractDungeon.player.hasRelic(BlackEgg.ID) && !CardCrawlGame.nextDungeon.equals(TheEnding.ID)) {
				shouldSpawn = ((BlackEgg) AbstractDungeon.player.getRelic(BlackEgg.ID)).shouldSpawn;
			}

			return shouldSpawn;
		}
	}

	//TODO: Replace with Locator
	@SpirePatch(cls = CLS, method = "generateMap")
	public static class GenerateMap {
									//SL:637 = 36 right now
		@SpireInsertPatch(rloc = 37)// after AbstractDungeon.map = (ArrayList<ArrayList<MapRoomNode>>)RoomTypeAssigner.distributeRoomsAcrossMap(AbstractDungeon.mapRng, (ArrayList)AbstractDungeon.map, (ArrayList)roomList);
		public static void Insert() {
			Settings.isEndless = InfiniteSpire.isEndless;
			addHolyWaterToRareRelicPool();
			addInitialQuests();
			//number of nightmares increases with number of bosses beaten (max 3), is increased by 1 if the "kill a nightmare" quest has not been completed or discarded.
			insertNightmareNode();
		}
		
		private static void addInitialQuests() {
			InfiniteSpire.logger.info("adding initial quests");
			if(AbstractDungeon.floorNum <= 1 &&  InfiniteSpire.questLog.isEmpty()) {
				if(InfiniteSpire.startWithEndlessQuest)	InfiniteSpire.questLog.add(new EndlessQuestPart1().createNew());
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
			int rand2;
			ArrayList<MapRoomNode> eliteNodes = new ArrayList<MapRoomNode>();
			ArrayList<MapRoomNode> shopNodes = new ArrayList<>();
			for(ArrayList<MapRoomNode> rows : AbstractDungeon.map) {
				for(MapRoomNode node : rows) {
					if(node != null && node.room instanceof MonsterRoomElite) {
						eliteNodes.add(node);
					}
					if(node != null && node.room instanceof ShopRoom) {
						shopNodes.add(node);
					}
				}
			}
			rand = AbstractDungeon.mapRng.random(eliteNodes.size() - 1);
			rand2 = AbstractDungeon.mapRng.random(shopNodes.size() - 1);

			eliteNodes.get(rand).setRoom(new NightmareEliteRoom());
			shopNodes.get(rand2).setRoom(new AvhariRoom());
			shopNodes.get(rand2).hasEmeraldKey = true;
		}
	}
}
