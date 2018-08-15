package infinitespire.abstracts;

import com.badlogic.gdx.graphics.Color;

public abstract class Quest {
	
	public String id;
	public int maxSteps, currentSteps;
	public Color color;
	public QuestType type;
	public QuestRarity rarity;
	public boolean isNew, justCompleted = false;
	
	private boolean completed, remove;
	
	public enum QuestType {
		RED,
		BLUE,
		GREEN
	}
	
	public enum QuestRarity {
		COMMON,
		RARE,
		SPECIAL
	}
	
	public Quest(String id, Color color, int steps, QuestType type, QuestRarity rarity) {
		this.id = id;
		this.color = color;
		this.maxSteps = steps;
		this.type = type;
		this.rarity = rarity;
		this.completed = false;
		this.remove = false;
		this.isNew = true;
	}
	/**
	 * This is called when the player clicks on a quest in the log or when the quest log looks for completed auto complete quests.
	 */
	public abstract void giveReward();
	/**
	 * This is called immediatly after the quests constructor is called and is used to generate unique data for the quest.
	 * @return this
	 */
	public abstract Quest createNew();
	/**
	 * @return A string that is written after "Reward:" or "Claim:" on the quests in the quest log
	 */
	public abstract String getRewardString();
	/**
	 * @return The title of the quest.
	 */
	public abstract String getTitle();
	
	/**
	 * 
	 * @return a copy of the quest without any data attatched to it.
	 */
	@Deprecated
	public abstract Quest getCopy();
	
	public void incrementQuestSteps() {
		this.currentSteps++;
		if(this.currentSteps >= this.maxSteps && !completed) {
			this.completed = true;
			this.justCompleted = true;
		}
	}
	
	/**
	 * Override this to be true on quests that you want to auto-complete
	 * @return True if the quest should auto-complete
	 */
	public boolean autoClaim() {
		return false;
	}

	public float getCompletionPercentage() {
		return (float) currentSteps / (float) maxSteps;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void removeQuest() {
		remove = true;
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public boolean isSameQuest(Quest q) {
		return this.getTitle().equals(q.getTitle());
	}
}
