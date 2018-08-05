package infinitespire.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;

import infinitespire.InfiniteSpire;
import infinitespire.quests.*;
import infinitespire.quests.Quest.QuestRarity;

public class QuestHelper {
	
	public static HashMap<String, Class<? extends Quest>> questMap = new HashMap<String, Class<? extends Quest>>();
	
	public static void init() {
		addQuestType(FetchQuest.class);
		addQuestType(DieQuest.class);
		addQuestType(SlayQuest.class);
		addQuestType(FlawlessQuest.class);
		addQuestType(OneTurnKillQuest.class);
	}
	
	private static void addQuestType(Class<? extends Quest> type) {
		questMap.put(type.getName(), type);
	}
	
	private static final String dirPath = ConfigUtils.CONFIG_DIR + File.separator
            + "InfiniteSpire" + File.separator + "QuestLog.json";
	
	public static int makeRandomCost(int range) {
		return MathUtils.round(range * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
	}
	
	public static ArrayList<Quest> getRandomQuests(int amount) {
		ArrayList<Quest> retVal = new ArrayList<Quest>();
		
		for(int i = 0; i < amount; i++) {
			retVal.add(getRandomQuest());
		}
		
		return retVal;
	}
	
	@Deprecated //this needs to be smarter about giving you the same shit over and over
	public static Quest getRandomQuest() {
		Quest retVal = null;
		int roll = AbstractDungeon.miscRng.random(0, 99);
		try {
			if(roll < 75) {
				retVal = getRandomQuestClass(QuestRarity.COMMON).newInstance().createNew();
			}else {
				retVal = getRandomQuestClass(QuestRarity.RARE).newInstance().createNew();
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
	
	public static String getQuestLogSaveData() {
		GsonBuilder builder = new GsonBuilder();
		//builder.setPrettyPrinting();
		Gson gson = builder.create();
		
		String saveString = gson.toJson(InfiniteSpire.questLog, QuestLog.class);
		InfiniteSpire.logger.info(saveString);
		return saveString;
	}
	
	public static void loadQuestLog() {
		QuestLog tempLog = new QuestLog();
		try {
			BufferedReader br = new BufferedReader(new FileReader(dirPath));
			String questLogString = br.readLine();
			JsonReader reader = new JsonReader();
			if(questLogString != null) {
				JsonValue listOfQuests = reader.parse(questLogString);
				for(JsonValue questString : listOfQuests) {
					Gson gson = new Gson();
					for(Class<? extends Quest> qC : questMap.values()) {
						if(qC.getName().equals(questString.get("id").asString())) {
							Quest quest = gson.fromJson(questString.toJson(OutputType.json), qC);
							InfiniteSpire.logger.info("Loaded: " + gson.toJson(quest));
							InfiniteSpire.logger.info("... as type " + quest.getClass().getName());
							tempLog.add(quest);
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			saveQuestLog();
		}
		
		InfiniteSpire.questLog = tempLog;
	}
	
	public static void saveQuestLog() {
		
		try {
			FileWriter writer = new FileWriter(dirPath);
			writer.write(getQuestLogSaveData());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void clearQuestLog() {
		InfiniteSpire.questLog.clear();
		try {
			FileWriter writer = new FileWriter(dirPath);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Class<? extends Quest> getRandomQuestClass(QuestRarity rarity) {
		ArrayList<Class<? extends Quest>> questPool = new ArrayList<Class<? extends Quest>>();
		
		for(Class<? extends Quest> q : questMap.values()) {
			questPool.add(q);
		}
		
		int roll = AbstractDungeon.miscRng.random(questPool.size() - 1);
		
		return questPool.get(roll);
	}
	
	public static AbstractRelic returnRandomRelic(RelicTier tier) {
		String key = Circlet.ID;
		AbstractRelic retVal = new Circlet();
		switch(tier) {
		case BOSS:
			key = AbstractDungeon.bossRelicPool.get(AbstractDungeon.relicRng.random(AbstractDungeon.bossRelicPool.size() - 1));
			break;
		case COMMON:
			key = AbstractDungeon.commonRelicPool.get(AbstractDungeon.relicRng.random(AbstractDungeon.commonRelicPool.size() - 1));
			break;
		case RARE:
			key = AbstractDungeon.rareRelicPool.get(AbstractDungeon.relicRng.random(AbstractDungeon.rareRelicPool.size() - 1));
			break;
		case UNCOMMON:
			key = AbstractDungeon.uncommonRelicPool.get(AbstractDungeon.relicRng.random(AbstractDungeon.uncommonRelicPool.size() - 1));
			break;
		default:
			key = Circlet.ID;
			break;
		}	
		
		retVal = RelicLibrary.getRelic(key);
		
		return retVal;
	}
}
