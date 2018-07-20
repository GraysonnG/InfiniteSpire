package infinitespire.quests;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import infinitespire.InfiniteSpire;

@Deprecated
public class FlawlessQuest extends Quest {

	public FlawlessQuest(String id) {
		super(id);
	}

	public FlawlessQuest() {
		this(null);
	}
	
	@Override
	public void onEnemyKilled(AbstractCreature creature) {
//		if (!AbstractDungeon.getCurrRoom().smoked && GameActionManager.damageReceivedThisCombat - GameActionManager.hpLossThisCombat <= 0 && this instanceof MonsterRoomElite) {
//            ++CardCrawlGame.champion;
//      }
		if(((AbstractMonster) creature).type == AbstractMonster.EnemyType.BOSS && !(GameActionManager.damageReceivedThisCombat > 0)) {
			this.incrementQuestSteps();
		}
		super.onEnemyKilled(creature);
	}

	@Override
	protected String generateID() {
		String retVal = FlawlessQuest.class.getName() + "-1-0-255,255,0-null";
		retVal += "-" + this.getCost("");
		return retVal;
	}

	@Override
	public String getTitle() {
		return "Flawless the Boss";
	}

	@Override
	protected void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		InfiniteSpire.points += cost;
	}

	@Override
	public String getRewardString() {
		return this.cost + "s";
	}

	@Override
	public int getCost(String s) {
		return MathUtils.round(300 * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
	}

}
