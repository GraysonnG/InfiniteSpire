package infinitespire.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.InfiniteSpire;
import infinitespire.quests.*;

public class QuestHelper {	
	
	private static HashMap<String, Class<? extends Quest>> questsMap = new HashMap<String, Class<? extends Quest>>();
	
	public static void addQuestTypeToMap(Class<? extends Quest> c) {
		questsMap.put(c.getName(), c);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static Quest getRandomQuest() {
		Quest quest = createQuest(SlayQuest.class.getName());
		Class<? extends Quest> c;
		do {
			int randomNum = AbstractDungeon.miscRng.random(questsMap.size() - 1);
			c = (Class<? extends Quest>) questsMap.values().toArray()[randomNum];
		}
		while(c.equals(EndlessQuestPart1.class));
		
		try {
			quest = c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
//		if(AbstractDungeon.miscRng.randomBoolean(0.33f)) {
//			quest = createQuest(FetchQuest.class.getName());
//		}
		
		return quest;
	}
	
	public static ArrayList<Quest> getRandomQuests(int amount){
		ArrayList<Quest> quests = new ArrayList<Quest>();
		
		for(int i = 0; i < amount; i++) {
			Quest quest;
			
			do {
				quest = getRandomQuest();
			}
			while(InfiniteSpire.questLog.contains(quest) || quests.contains(quest));
			quests.add(quest);
		}
		return quests;
	}
	
	public static Class<? extends Quest> getQuestClassFromMap(String string) {
		return questsMap.get(string);
	}
	
	public static Quest createQuest(String classNameAsString) {
		try {
			return questsMap.get(classNameAsString).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}	
}
