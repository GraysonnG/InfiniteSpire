package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MinionPower;
import infinitespire.abstracts.Quest;

public class ActKillQuest extends SlayQuest{

	public String currentActID;

	public ActKillQuest(){
		this.id = ActKillQuest.class.getName();
		this.color = new Color(0.75f, 0.0f, 0.25f, 1.0f);
		this.type = QuestType.BLUE;
		this.rarity = QuestRarity.RARE;
		this.maxSteps = 20;
	}

	@Override
	public void onEnemyKilled(AbstractCreature creature) {
		if(!creature.hasPower(MinionPower.POWER_ID)) {
			this.incrementQuestSteps();
		}
	}

	public void update(){
		if(!this.currentActID.equals(AbstractDungeon.id)){
			this.currentSteps = 0;
			this.currentActID = AbstractDungeon.id;
		}
	}

	@Override
	public Quest createNew() {
		this.currentActID = AbstractDungeon.id;
		return this;
	}

	@Override
	public String getTitle() {
		return "Kill " + maxSteps + " Monsters This Act";
	}

	@Override
	public String getRewardString() {
		return super.getRewardString();
	}
}
