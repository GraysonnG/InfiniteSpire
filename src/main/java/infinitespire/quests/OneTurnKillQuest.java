package infinitespire.quests;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import infinitespire.InfiniteSpire;

public class OneTurnKillQuest extends Quest {
	
	public OneTurnKillQuest(String id) {
		super(id);
	}
	
	public OneTurnKillQuest() {
		super(null);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onEnemyKilled(AbstractCreature creature) {
		AbstractMonster m = (AbstractMonster)creature;
		if(GameActionManager.turn <= 1 && m.type == AbstractMonster.EnemyType.ELITE) {
			this.incrementQuestSteps();
		}
	}

	@Override
	protected String generateID() {
		String retVal = OneTurnKillQuest.class.getName() + "-1-0-255,60,0-null";
		retVal += "-" + getCost(""); 
		return retVal;
	}

	@Override
	public String getTitle() {
		return "One Shot Kill an Elite";
	}

	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		InfiniteSpire.points += cost;
	}

	@Override
	public String getRewardString() {
		return cost + "s";
	}

	@Override
	public int getCost(String s) {
		return MathUtils.round(250f * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
	}

}
