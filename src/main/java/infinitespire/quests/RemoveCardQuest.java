package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;

public class RemoveCardQuest extends Quest {
	
	private static final Color COLOR = new Color();
	
	public RemoveCardQuest() {
		super(RemoveCardQuest.class.getName(), COLOR, 5, QuestType.GREEN, QuestRarity.COMMON);
	}

	@Override
	public void giveReward() {
		
	}

	@Override
	public Quest createNew() {
		
		return this;
	}

	@Override
	public String getRewardString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Quest getCopy() {
		// TODO Auto-generated method stub
		return null;
	}

}
