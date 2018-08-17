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
import infinitespire.abstracts.Quest;
import infinitespire.abstracts.Quest.QuestRarity;
import infinitespire.abstracts.Quest.QuestType;
import infinitespire.quests.*;
import infinitespire.quests.endless.*;
import infinitespire.quests.event.*;

public class QuestHelper {
	
	public static HashMap<String, Class<? extends Quest>> questMap = new HashMap<String, Class<? extends Quest>>();
	
	public static void init() {
		addQuestType(FetchQuest.class);
		addQuestType(DieQuest.class);
		addQuestType(SlayQuest.class);
		addQuestType(FlawlessQuest.class);
		addQuestType(OneTurnKillQuest.class);
		addQuestType(RemoveCardQuest.class);
		addQuestType(PickUpCardQuest.class);
		addQuestType(EndlessQuestPart1.class);
		addQuestType(EndlessQuestPart2.class);
		addQuestType(BearQuest.class);
		addQuestType(BlankyQuest.class);
	}
	
	public static void addQuestType(Class<? extends Quest> type) {
		questMap.put(type.getName(), type);
	}
	
	private static final String dirPath = ConfigUtils.CONFIG_DIR + File.separator
            + "InfiniteSpire" + File.separator + "QuestLog.json";
	
	public static int makeRandomCost(int range) {
		return MathUtils.round(range * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
	}
	
	public static ArrayList<Quest> getRandomQuests(int amount) {
		
		int questsAdded = 0;
		int attempts = 100;
		QuestAmount qs = new QuestAmount();

		ArrayList<Quest> questsToAdd = new ArrayList<Quest>();
		do {
			Quest q = getRandomQuest();
			
			boolean shouldContinue = false;
		
			if(InfiniteSpire.questLog.getAmount(q.type) + qs.getQuestNum(q.type) >= 7) {
				shouldContinue = true;
			}
			
			for(Quest rq : questsToAdd) {
				if(rq.isSameQuest(q)) {
					shouldContinue = true;
				}
			}
			
			if(InfiniteSpire.questLog.hasQuest(q)) {
				shouldContinue = true;
			}
			
			attempts--;
			if(attempts <= 0) break;
			
			
			if(shouldContinue) {
				continue;
			}
			
			qs.addQuest(q.type);
			questsToAdd.add(q);
			questsAdded++;
			
		}while(questsAdded < amount && attempts > 0);
		
		ArrayList<Quest> retVal = new ArrayList<Quest>();
		retVal.addAll(questsToAdd);
		return retVal;
	}

	public static Quest getRandomQuest() {
		Quest retVal = null;
		int roll = AbstractDungeon.miscRng.random(0, 99);
		try {
			if(roll < 90) {
				retVal = getRandomQuestClass(QuestRarity.COMMON).createNew();
			}else {
				retVal = getRandomQuestClass(QuestRarity.RARE).createNew();
			}
		} catch (InstantiationException | IllegalAccessException e) {
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
	
	public static Quest getRandomQuestClass(QuestRarity rarity) throws InstantiationException, IllegalAccessException {
		ArrayList<Class<? extends Quest>> questPool = new ArrayList<Class<? extends Quest>>();
		
		for(Class<? extends Quest> q : questMap.values()) {
			if(q.newInstance().createNew().rarity.equals(rarity)) {
				questPool.add(q);
			}
		}
		
		int roll = AbstractDungeon.miscRng.random(questPool.size() - 1);
		
		return questPool.get(roll).newInstance();
	}
	
	public static AbstractRelic returnRandomRelic(RelicTier tier) {
		String key = Circlet.ID;
		AbstractRelic retVal = new Circlet();
		switch(tier) {
		case BOSS:
			int bossPoolSize = AbstractDungeon.bossRelicPool.size() - 1;
			if(bossPoolSize > 0)
				key = AbstractDungeon.bossRelicPool.get(AbstractDungeon.relicRng.random(bossPoolSize));
			break;
		case COMMON:
			int commonPoolSize = AbstractDungeon.commonRelicPool.size() - 1;
			if(commonPoolSize > 0)
			key = AbstractDungeon.commonRelicPool.get(AbstractDungeon.relicRng.random(commonPoolSize));
			break;
		case RARE:
			int rarePoolSize = AbstractDungeon.rareRelicPool.size() - 1;
			if(rarePoolSize > 0)
			key = AbstractDungeon.rareRelicPool.get(AbstractDungeon.relicRng.random(rarePoolSize));
			break;
		case UNCOMMON:
			int uncommonPoolSize = AbstractDungeon.uncommonRelicPool.size() - 1;
			if(uncommonPoolSize > 0)
			key = AbstractDungeon.uncommonRelicPool.get(AbstractDungeon.relicRng.random(uncommonPoolSize));
			break;
		default:
			key = Circlet.ID;
			break;
		}	
		
		retVal = RelicLibrary.getRelic(key);
		
		return retVal;
	}
	
	private static class QuestAmount {
		public int redQuests, blueQuests, greenQuests;
		
		public QuestAmount() {
			redQuests = 0;
			blueQuests = 0;
			greenQuests = 0;
		}
		
		public int getQuestNum(QuestType type) {
			switch(type) {
			case BLUE:
				return blueQuests;
			case GREEN:
				return greenQuests;
			case RED:
				return redQuests;
			}
			return -1;
		}
		
		public void addQuest(QuestType type) {
			switch(type) {
			case BLUE:
				blueQuests++;
				break;
			case GREEN:
				greenQuests++;
				break;
			case RED:
				redQuests++;
				break;
				
			}
		}
	}
}
