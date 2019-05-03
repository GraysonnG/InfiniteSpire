package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.CardHelper;

import java.util.ArrayList;

public class EliteQuest extends SlayQuest {

	public String currentActID;

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
		ArrayList<AbstractCard> randomBlackCards = CardHelper.getBlackRewardCards();
		AbstractDungeon.cardRewardScreen.open(randomBlackCards, null, "Select a Card.");
	}

	@Override
	public String getRewardString() {
		return "Pick a Black Card";
	}

	@Override
	public String getTitle() {
		return "Kill " + maxSteps + " Elites This Act";
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
