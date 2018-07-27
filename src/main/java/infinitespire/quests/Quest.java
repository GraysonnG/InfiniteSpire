package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import infinitespire.InfiniteSpire;
import infinitespire.effects.QuestLogUpdateEffect;

public abstract class Quest {
	protected int questSteps, maxQuestSteps;
	private Texture img;
	protected String id;
	public int cost;
	@SuppressWarnings("unused")
	private String classString;
	private Color color;
	private boolean completed = false;
	private boolean remove = false;
	
	public Quest(String id) {
		this.preInitialize();
		if(id != null && !id.equals("")) {
			this.id = id;
		}else {
			this.id = this.generateID();
			InfiniteSpire.logger.info(this.id);
		}
		if(this.id == null) return;
		
		String[] data = this.id.split("-");
		this.classString = data[0];
		this.maxQuestSteps = Integer.parseInt(data[1]);
		this.questSteps = Integer.parseInt(data[2]);
		String[] colorData = data[3].split(",");
		
		this.color = new Color(Integer.parseInt(colorData[0]) / 255f, Integer.parseInt(colorData[1]) / 255f, Integer.parseInt(colorData[2]) / 255f, 1f);
		
		this.cost = Integer.parseInt(data[5]);
		
		if(this.questSteps >= this.maxQuestSteps) {
			this.completed = true;
		}
	}
	
	/**
	 * Returns a string of the following format:<br>
	 * CLASS_NAME-AMOUNT_OF_STEPS-COMPLETED_STEPS-SILVERREWARD-COLOR_IN_RGB-DATA-COST
	 * <br>
	 * <br>
	 * Example:<br>
	 * SlayQuest-5-0-100-255,255,255-Byrd-235
	 * <br>
	 * <br>
	 * Example2:<br> 
	 * EndlessQuest-1-0-300-255,0,255-false-124
	 * @return theID of the object
	 */
	protected abstract String generateID();
	
	public abstract String getTitle();
	
	public abstract void giveReward();
	
	public abstract String getRewardString();
	
	public abstract int getCost(String s);
	
	protected void preInitialize() {}
	
	public static String createIDWithoutData(String className, int maxSteps, int steps, java.awt.Color color) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(className);
		builder.append("-" + maxSteps);
		builder.append("-" + steps);
		builder.append("-" + color.getRed() + "," + color.getGreen() + "," + color.getBlue());
		
		return builder.toString();
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		String retVal = "";
		
		String[] data = id.split("-");
		retVal += data[0];
		for(int i = 1; i < data.length; i ++) {
			if(i == 2) {
				retVal += "-" + this.questSteps;
			}else {
				retVal += "-" + data[i];
			}
		}
		
		return retVal;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public boolean shouldRemove() {
		return this.remove;
	}
	
	public void setRemove(boolean set) {
		this.remove = set;
	}
	
	public Texture getImage() {
		return img;
	}

	public Color getColor() {
		return this.color;
	}
	
	public float getCompletionPercentage() {
		return (float)this.questSteps / (float)this.maxQuestSteps;
	}
	
	public String getCompletionString() {
		return this.questSteps + "/" + this.maxQuestSteps;
	}
	
	public void saveData() {
		
	}
	
	public void incrementQuestSteps() {
		this.questSteps++;
		if(this.questSteps == this.maxQuestSteps) {
			InfiniteSpire.logger.info("Quest Completed:" + this.getID());
			this.completed = true;
			AbstractDungeon.topLevelEffects.add(new QuestLogUpdateEffect());
		}
	}

	public void onEnemyKilled(AbstractCreature creature) {
		
	}

	public void onRelicRecieved(AbstractRelic relic) {
	
	}

	public void onRoomEntered(AbstractRoom room) {
		
	}

	@Override
	public boolean equals(Object obj) {
		String[] objData = ((Quest) obj).getID().split("-");
		String[] thisData = this.getID().split("-");
		
		return (objData[0].equals(thisData[0]) && objData[4].equals(thisData[4]));
	}	
}
