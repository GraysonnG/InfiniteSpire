package infinitespire.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.quests.Quest;

public abstract class QuestHelper {
	
	
	private static ArrayList<String> roomSymbolList = new ArrayList<String>();
	
	
	private static HashMap<String, Class<? extends Quest>> questsMap = new HashMap<String, Class<? extends Quest>>();
	
	public static void initializeLists() {
		roomSymbolList.add("?");
		roomSymbolList.add("M");
		roomSymbolList.add("E");
		roomSymbolList.add("$");
	}
	
	public static void addQuestTypeToMap(String classNameAsString, Class<? extends Quest> c) {
		questsMap.put(classNameAsString, c);
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
	
	private static String getRandomRoomType() {
		String result = null;
		
		result = roomSymbolList.get(AbstractDungeon.miscRng.random(roomSymbolList.size() - 1));
		
		return result;
	}	
}
