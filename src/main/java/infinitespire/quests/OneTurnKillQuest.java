package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.QuestHelper;

public class OneTurnKillQuest extends Quest {

	private static final Color COLOR = new Color(1f, 0, 0.5f, 1.0f);
	public int cost;
	
	public OneTurnKillQuest() {
		super(OneTurnKillQuest.class.getName(), COLOR, 1, QuestType.RED, QuestRarity.RARE);
	}

	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		AbstractDungeon.player.gainGold(cost);
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/elite.png");
	}

	@Override
	public Quest createNew() {
		this.cost = QuestHelper.makeRandomCost(300);
		return this;
	}

	@Override
	public String getRewardString() {
		return this.cost + "g";
	}

	@Override
	public String getTitle() {
		return "One Turn Kill an Elite";
	}

	@Override
	public Quest getCopy() {
		return new OneTurnKillQuest();
	} 
}
