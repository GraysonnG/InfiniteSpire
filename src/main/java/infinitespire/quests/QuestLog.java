package infinitespire.quests;

import java.util.ArrayList;

import basemod.BaseMod;
import basemod.interfaces.PostDungeonInitializeSubscriber;

public class QuestLog implements PostDungeonInitializeSubscriber{
	
	private ArrayList<Quest> quests = new ArrayList<Quest>();
	
	public QuestLog() {
		BaseMod.subscribe(this);
	}

	@Override
	public void receivePostDungeonInitialize() {
		
		
	}
	
	public void add(Quest questToAdd) {
		quests.add(questToAdd);
	}
	
	public Quest get(int index) {
		return quests.get(index);
	}
}
