package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;

public class FlawlessQuest extends Quest {

	public FlawlessQuest() {
		super(FlawlessQuest.class.getName(), new Color(1.0f, 1.0f, 0.0f, 1.0f), 1, QuestType.BLUE, QuestRarity.RARE);
	}

	@Override
	public void giveReward() {
		AbstractRelic relic = AbstractDungeon.returnRandomRelic(RelicTier.RARE);
		relic.instantObtain();
		relic.playLandingSFX();
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/boss.png");
	}

	@Override
	public Quest createNew() {
		return this;
	}

	@Override
	public String getRewardString() {
		return "Receive a Rare Relic";
	}

	@Override
	public String getTitle() {
		return "Flawless the Boss";
	}

	@Override
	public Quest getCopy() {
		return new FlawlessQuest();
	}
}
