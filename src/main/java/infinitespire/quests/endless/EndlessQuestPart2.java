package infinitespire.quests.endless;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.abstracts.Quest;
import infinitespire.helpers.CardHelper;

public class EndlessQuestPart2 extends Quest {

	private static final Color COLOR = new Color(0.75f, 0.0f, 1.0f, 1.0f);
	
	public EndlessQuestPart2() {
		super(EndlessQuestPart2.class.getName(), COLOR, 3, QuestType.BLUE, QuestRarity.SPECIAL);
	}
	
	@Override
	public void giveReward() {
		ArrayList<AbstractCard> randomBlackCards = CardHelper.getBlackRewardCards();
		AbstractDungeon.cardRewardScreen.open(randomBlackCards, null, "Select a Card.");
	}

	@Override
	public Quest createNew() {
		return this;
	}

	@Override
	public String getRewardString() {
		return "Pick a Black Card";
	}

	@Override
	public String getTitle() {
		return "Defeat 3 Bosses";
	}

	@Override
	public Quest getCopy() {
		return new EndlessQuestPart2();
	}
}
