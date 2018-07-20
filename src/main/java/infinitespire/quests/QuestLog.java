package infinitespire.quests;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import infinitespire.InfiniteSpire;
import infinitespire.effects.QuestLogUpdateEffect;
import infinitespire.helpers.QuestHelper;

public class QuestLog extends ArrayList<Quest> implements PostUpdateSubscriber {
	private static final long serialVersionUID = -8923472099668326287L;

	public boolean hasUpdate = false;
	private boolean justUpdated = false;
	
	public QuestLog() {
		BaseMod.subscribe(this);
	}
	
	@Override
	public boolean add(Quest quest) {
		hasUpdate = true;
		if(!this.justUpdated && AbstractDungeon.effectsQueue != null) {
			AbstractDungeon.effectsQueue.add(new QuestLogUpdateEffect());
			this.justUpdated = true;
		}
		return super.add(quest);
		
	}

	@Override
	public boolean addAll(Collection<? extends Quest> c) {
		hasUpdate = true;
		if(!this.justUpdated && AbstractDungeon.effectsQueue != null) {
			AbstractDungeon.effectsQueue.add(new QuestLogUpdateEffect());
			this.justUpdated = true;
		}
		return super.addAll(c);
	}

	public void initializeQuestLog() {
		
		InfiniteSpire.logger.info("InfiniteSpire | Initializing Quest Log");
		
		QuestHelper.addQuestTypeToMap(SlayQuest.class);
		QuestHelper.addQuestTypeToMap(FetchQuest.class);
		QuestHelper.addQuestTypeToMap(DieQuest.class);
		QuestHelper.addQuestTypeToMap(EndlessQuestPart1.class);
		QuestHelper.addQuestTypeToMap(OneTurnKillQuest.class);
		QuestHelper.addQuestTypeToMap(FlawlessQuest.class);
		
		QuestLog log = new QuestLog();
		
		try {
			SpireConfig logData = new SpireConfig("InfiniteSpire", "questLog");
			logData.load();
			String data = logData.getString("data");
			System.out.println(data);
			if(data != null && !data.equals("null")) {
				InfiniteSpire.logger.info("InfiniteSpire found quests! Loading Quests...");
				log = createLogFromString(data);
			}
			
		} catch (IOException e) {
			InfiniteSpire.logger.error("Infinite Spire failed to load questlog file...");
		}
		
		this.addAll(log);
	}
	
	public void clearQuestLog() {
		this.clear();
		
		try {
			SpireConfig logData = new SpireConfig("InfiniteSpire", "questLog");
			logData.setString("data", "null");
			logData.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void saveQuestLog() {
		try {
			SpireConfig logData = new SpireConfig("InfiniteSpire", "questLog");
			
			if(this.isEmpty()) {
				logData.setString("data", "null");
				InfiniteSpire.logger.info("Setting quest data to null.");
				logData.save();
				return;
			}
			
			InfiniteSpire.logger.info("Saving Quest Data.");
			
			String data = this.get(0).getID();
			
			for(int i = 1; i < this.size(); i++) {
				data += ":" + this.get(i).getID();
			}
			
			logData.setString("data", data);
			
			logData.save();
			
		} catch (IOException e) {
			InfiniteSpire.logger.error("Infinite Spire failed to save questlog file...");
		}
		
	}
	
	private QuestLog createLogFromString(String data) {
		QuestLog log = new QuestLog();
		
		
		
		System.out.println(data);
		
		String[] questIds = data.split(":");
		for(String questID : questIds) {
			
			String questClassString = questID.split("-")[0];
			
			
			try {
				Class<? extends Quest> questClass = QuestHelper.getQuestClassFromMap(questClassString);
				log.add((Quest) 
						questClass.getConstructor(String.class)
						.newInstance(questID));
					
					
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return log;
	}

	@Override
	public void receivePostUpdate() {
		for(int i = this.size() - 1; i >= 0; i--) {
			Quest quest = this.get(i);
			if(quest.isCompleted()) {
				InfiniteSpire.logger.info("Removing: " + quest.getID());
				this.remove(i);
			}
		}
		this.justUpdated = false;
	}
}
