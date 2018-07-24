package infinitespire.quests;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.InfiniteSpire;

public class DieQuest extends Quest {

	public DieQuest(String questID) {
		super(questID);
	}
	
	public DieQuest() {
		this(null);
	}
	
	@Override
	protected String generateID() {
		String id = DieQuest.class.getName() + "-1-0-100,100,100-null";
		id += "-" + getCost("");
		return id;
	}

	@Override
	public String getTitle() {
		return "Die!";
	}

	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		InfiniteSpire.points += cost;
		
		AbstractPlayer p = AbstractDungeon.player;
		
		AbstractDungeon.actionManager.addToTop(new HealAction(p, p, (p.maxHealth / 4)));
	}

	@Override
	public String getRewardString() {
		return this.cost + "s" + " & Heal 25%" ;
	}

	@Override
	public int getCost(String s) {
		return MathUtils.round(1000 * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
	}

	public void onPlayerDie() {
		this.incrementQuestSteps();
	}

}
