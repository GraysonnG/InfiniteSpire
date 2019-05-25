package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;

public class EliteQuest extends SlayQuest {

	public String currentActID;
	private static final int REWARD_AMOUNT = 2;

	public EliteQuest() {
		this.id = EliteQuest.class.getName();
		this.color = new Color(0.50f, 0.25f, 0.25f, 1.0f);
		this.type = QuestType.BLUE;
		this.rarity = QuestRarity.RARE;
		this.maxSteps = 3;
	}

	@Override
	public void onEnemyKilled(AbstractCreature creature) {
		if(!creature.hasPower(MinionPower.POWER_ID)) {
			AbstractMonster monster = (AbstractMonster) creature;
			if(monster.type == AbstractMonster.EnemyType.ELITE)
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
	public void giveReward() {
		InfiniteSpire.gainVoidShards(2);
	}

	@Override
	public String getRewardString() {
		return voidShardStrings.TEXT[2] + REWARD_AMOUNT + voidShardStrings.TEXT[4];
	}

	@Override
	public String getTitle() {
		return questStrings.TEXT[4];
	}

	@Override
	public Quest getCopy() {
		return new EliteQuest();
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.Textures.getUITexture("questLog/questIcons/elite.png");
	}
}
