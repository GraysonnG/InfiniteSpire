package infinitespire.quests;

import java.util.ArrayList;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;

public class QuestLog extends ArrayList<Quest> implements PostUpdateSubscriber{
	
	public QuestLog() {
		super();
		BaseMod.subscribe(this);
	}
	
	private static final long serialVersionUID = -8923472099668326287L; 
	public boolean hasUpdate = false;
	
	@Override
	public void receivePostUpdate() {
		if(this.isEmpty()) return;
		
		for(int i = this.size() - 1; i >= 0; i--) {
			if(this.get(i).autoClaim() && this.get(i).isCompleted()) {
				this.get(i).giveReward();
				this.remove(i);
			}
		}
	}
}
