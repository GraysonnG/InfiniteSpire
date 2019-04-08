package infinitespire.quests;

import basemod.BaseMod;
import basemod.interfaces.PostDungeonUpdateSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.abstracts.Quest.QuestType;
import infinitespire.effects.QuestLogUpdateEffect;
import infinitespire.interfaces.IQuestLine;

import java.util.ArrayList;

public class QuestLog extends ArrayList<Quest> implements PostUpdateSubscriber, PostDungeonUpdateSubscriber{
	
	private static final long serialVersionUID = -8923472099668326287L; 
	public boolean hasUpdate = false;
	public static int masterDeckSize;
	
	public QuestLog(boolean shouldSubscribe) {
		super();
		
		if(!shouldSubscribe) return;
		BaseMod.subscribe(this);
	}
	
	public QuestLog() {
		this(true);
	}
	
	public int getAmount(QuestType type) {
		int count = 0;
		for(Quest q : this) {
			if(q.type == type) {
				count++;
			}
		}
		return count;
	}
	
	public boolean hasQuest(Quest q) {
		for(Quest quest : this) {
			if(q.isSameQuest(quest)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void receivePostUpdate() {
		if(this.isEmpty()) return;
		
		for(int i = this.size() - 1; i >= 0; i--) {
			if (this.get(i).justCompleted) {
				this.get(i).justCompleted = false;
				AbstractDungeon.topLevelEffects.add(new QuestLogUpdateEffect());
			}
			if (this.get(i).isCompleted()) {
				if (this.get(i).autoClaim()) {
					this.get(i).giveReward();
					InfiniteSpire.publishOnQuestRemoved(this.get(i));
					this.remove(i);
					continue;
				}
				if (this.get(i).shouldRemove()) {
					this.get(i).giveReward();
					if(this.get(i) instanceof IQuestLine) {
                        ((IQuestLine) this.get(i)).addNextStep(this, i);
                    }
					InfiniteSpire.publishOnQuestRemoved(this.get(i));
					this.remove(i);
				}
			} else {
				if (this.get(i).shouldRemove()) {
					InfiniteSpire.publishOnQuestRemoved(this.get(i));
					this.remove(i);
				}
			}
		}
	}

	public void markAllQuestsAsSeen() {
		for(Quest q : this) {
			q.isNew = false;
		}
	}

	@Override
	public void receivePostDungeonUpdate() {
		for(Quest quest : this) {
			quest.update();
		}
	}
}
