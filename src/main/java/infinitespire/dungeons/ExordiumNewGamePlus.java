package infinitespire.dungeons;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EmptyRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.TheBottomScene;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import infinitespire.InfiniteSpire;
import infinitespire.rooms.PerkRoom;

public class ExordiumNewGamePlus extends AbstractDungeon{
	public static final String ID = "ExordiumNGP";
	public static final String TEXT[] = {""};
	public static final String NAME = "Exordium New Game Plus";
	
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	
	public ExordiumNewGamePlus(AbstractPlayer p, ArrayList<String> emptyList) {
		super(NAME, ID, p, emptyList);
		logger.info("Loading Exordium NGP");
		this.initializeRelicList();
		if(scene != null) {
			scene.dispose();
		}
		scene = new TheBottomScene();
		scene.randomizeScene();
		
		fadeColor = Color.valueOf("1e0f0aff");
		this.initializeSpecialOneTimeEventList();
		this.initializeLevelSpecificChances();
		mapRng = new Random(Settings.seed);
		generateMap();
		CardCrawlGame.music.changeBGM(id);
		AbstractDungeon.currMapNode = new MapRoomNode(0, -1);
		AbstractDungeon.currMapNode.room = new PerkRoom();
		AbstractDungeon.currMapNode.room = new EmptyRoom();
	}
	
	public ExordiumNewGamePlus(AbstractPlayer p, SaveFile file) {
		super(NAME, p, file);
		logger.info("Loading Exordium NGP");
		CardCrawlGame.dungeon = this;
		if(scene != null) scene.dispose();
		scene = new TheBottomScene();
		scene.randomizeScene();
		fadeColor = Color.valueOf("1e0f0aff");
		this.initializeLevelSpecificChances();
		miscRng = new Random(Settings.seed + file.floor_num);
		CardCrawlGame.music.changeBGM(id);
		mapRng = new Random(Settings.seed);
		generateMap();
		firstRoomChosen = true;
		this.populatePathTaken(file);
		if(file.floor_num == 0) {
			AbstractDungeon.firstRoomChosen = false;
		}
	}
	
	@Override
	protected void initializeLevelSpecificChances() {
		shopRoomChance = 0.5f;
		restRoomChance = 0.12f;
		treasureRoomChance = 0.0f;
		eventRoomChance = 0.22f;
		eliteRoomChance = 0.08f;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3f;
        cardUpgradedChance = 0.0f;
	}
	
	@Override
	protected void generateMonsters() {
		ArrayList<MonsterInfo> monsters = new ArrayList<MonsterInfo>();
		monsters.add(new MonsterInfo("Cultist", 2.0f));
		monsters.add(new MonsterInfo("Jaw Worm", 2.0f));
        monsters.add(new MonsterInfo("2 Louse", 2.0f));
        monsters.add(new MonsterInfo("Small Slimes", 2.0f));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, 3, false);
        monsters.clear();
        monsters.add(new MonsterInfo("Blue Slaver", 2.0f));
        monsters.add(new MonsterInfo("Gremlin Gang", 1.0f));
        monsters.add(new MonsterInfo("Looter", 2.0f));
        monsters.add(new MonsterInfo("Large Slime", 2.0f));
        monsters.add(new MonsterInfo("Lots of Slimes", 1.0f));
        monsters.add(new MonsterInfo("Exordium Thugs", 1.5f));
        monsters.add(new MonsterInfo("Exordium Wildlife", 1.5f));
        monsters.add(new MonsterInfo("Red Slaver", 1.0f));
        monsters.add(new MonsterInfo("3 Louse", 2.0f));
        monsters.add(new MonsterInfo("2 Fungi Beasts", 2.0f));
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, 12, false);
        monsters.clear();
        monsters.add(new MonsterInfo("Gremlin Nob", 1.0f));
        monsters.add(new MonsterInfo("Lagavulin", 1.0f));
        monsters.add(new MonsterInfo("3 Sentries", 1.0f));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, 10, true);
	}
	
	@Override
	protected ArrayList<String> generateExclusions() {
		ArrayList<String> retVal = new ArrayList<String>();
		String s = monsterList.get(monsterList.size() - 1);
		switch(s) {
			case "Looter": {
	            retVal.add("Exordium Thugs");
	        	}
	        case "Jaw Worm": {}
	        case "Blue Slaver": {
	            retVal.add("Red Slaver");
	            retVal.add("Exordium Thugs");
	            break;
	        	}
	        case "2 Louse": {
	            retVal.add("3 Louse");
	            break;
	        	}
	        case "Small Slimes": {
	            retVal.add("Large Slime");
	            retVal.add("Lots of Slimes");
	            break;
	        }
		}
		return retVal;
	}

	

	@Override
	protected void initializeBoss() {
		if (!UnlockTracker.isBossSeen("GUARDIAN")) {
            bossList.add("The Guardian");
        }
        else if (!UnlockTracker.isBossSeen("GHOST")) {
            bossList.add("Hexaghost");
        }
        else if (!UnlockTracker.isBossSeen("SLIME")) {
            bossList.add("Slime Boss");
        }
        else {
            bossList.add("The Guardian");
            bossList.add("Hexaghost");
            bossList.add("Slime Boss");
            Collections.shuffle(bossList, new java.util.Random(monsterRng.randomLong()));
        }
	}

	@Override
	protected void initializeEventImg() {
		if(eventImg != null) {
			eventImg.dispose();
			eventImg = null;
		}
		
		eventImg = ImageMaster.loadImage("images/ui/event/panel.png");
	}

	@Override
	protected void initializeEventList() {
		eventList.add("Big Fish");
        eventList.add("The Cleric");
        eventList.add("Dead Adventurer");
        eventList.add("Golden Idol");
        eventList.add("Golden Wing");
        eventList.add("World of Goop");
        eventList.add("Liars Game");
        eventList.add("Living Wall");
        eventList.add("Mushrooms");
        eventList.add("Scrap Ooze");
        eventList.add("Shining Light");
	}

	

	@Override
	protected void initializeShrineList() {
		shrineList.add("Match and Keep!");
        shrineList.add("Golden Shrine");
        shrineList.add("Transmorgrifier");
        shrineList.add("Purifier");
        shrineList.add("Upgrade Shrine");
        shrineList.add("Wheel of Change");
	}
}
