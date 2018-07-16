package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public abstract class Quest {
	private String name;
	private String description;
	private int questSteps, maxQuestSteps;
	private Texture img;
	protected String id;
	private String classString;
	private Color color;
	private boolean completed = false;
	
	public Quest(String id) {
		this.preInitialize();
		if(id != null && !id.equals("")) {
			this.id = id;
		}else {
			this.id = this.generateID();
		}
		if(id == null) return;
		
		String[] data = id.split("-");
		this.classString = data[0];
		this.maxQuestSteps = Integer.parseInt(data[1]);
		this.questSteps = Integer.parseInt(data[2]);
		
		String[] colorData = data[3].split(",");
		
		this.color = new Color(Integer.parseInt(colorData[0]) / 255f, Integer.parseInt(colorData[1]) / 255f, Integer.parseInt(colorData[2]) / 255f, 1f);
	}
	
	/**
	 * Returns a string of the following format:<br>
	 * CLASS_NAME-AMOUNT_OF_STEPS-COMPLETED_STEPS-COLOR_IN_RGB-DATA
	 * <br>
	 * <br>
	 * Example:<br>
	 * SlayQuest-5-0-255,255,255-Byrd
	 * <br>
	 * <br>
	 * Example2:<br> 
	 * EndlessQuest-1-0-255,0,255-false
	 * @return theID of the object
	 */
	protected abstract String generateID();
	
	protected abstract void giveReward();
	
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
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public Texture getImage() {
		return img;
	}
	
	public void saveData() {
		
	}
	
	public void incrementQuestSteps() {
		this.questSteps++;
		if(this.questSteps == this.maxQuestSteps) this.completed = true;
	}

	public void onEnemyKilled(AbstractCreature creature) {
		
	}

	public void onRelicRecieved(AbstractRelic relic) {
	
	}

	public void onRoomEntered(AbstractRoom room) {
		
	}
}
