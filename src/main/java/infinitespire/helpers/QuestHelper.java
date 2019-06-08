package infinitespire.helpers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.relics.Circlet;
import infinitespire.AutoLoaderIgnore;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.abstracts.Quest.QuestRarity;
import infinitespire.abstracts.Quest.QuestType;
import infinitespire.quests.QuestLog;
import javassist.NotFoundException;
import org.clapper.util.classutil.ClassFinder;
import org.clapper.util.classutil.ClassInfo;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class QuestHelper {
	
	public static HashMap<String, Class<? extends Quest>> questMap = new HashMap<>();
	
	public static void init() {
		try {
			autoLoadQuests();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private static void autoLoadQuests() throws URISyntaxException {
		ClassFinder finder = new ClassFinder();
		URL url = InfiniteSpire.class.getProtectionDomain().getCodeSource().getLocation();
		finder.add(new File(url.toURI()));

		List<ClassInfo> foundClasses = new ArrayList<>();
		finder.findClasses(foundClasses);
		foundClasses.stream()
			.filter((clazz) -> clazz.getClassName().startsWith("infinitespire.quests"))
			.map(clazz -> {
				try{
					return Loader.getClassPool().get(clazz.getClassName());
				} catch (NotFoundException e) {
					e.printStackTrace();
					return null;
				}})
			.filter(ctClass -> {
				try {
					return
						ctClass != null &&
						!ctClass.hasAnnotation(AutoLoaderIgnore.class) &&
						ctClass.subclassOf(Loader.getClassPool().get(Quest.class.getName()));
				} catch (NotFoundException e) {
					e.printStackTrace();
					return false;
				}
			})
			.map(ctClass -> {
				try {
					return (Class<Quest>) Loader.getClassPool().getClassLoader().loadClass(ctClass.getName());
				} catch ( ClassNotFoundException e) {
					return null;
				}
			})
			.filter(Objects::nonNull)
			.forEach(QuestHelper::registerQuest);

	}
	
	public static void registerQuest(Class<? extends Quest> type) {
		InfiniteSpire.logger.info("Registered Quest: " + type.getName());
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

		ArrayList<Quest> questsToAdd = new ArrayList<>();
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
			
		}while(questsAdded < amount);

		return questsToAdd;
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
					questMap.values().stream()
						.filter(questClass -> questClass.getName().equals(questString.get("id").asString()))
						.map(questClass -> gson.fromJson(questString.toJson(OutputType.json), questClass))
						.forEach(tempLog::add);
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

	public static void playVoidShardCollectSound() {
		CardCrawlGame.sound.play("RELIC_DROP_CLINK");
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
