package infinitespire.quests.endless;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.CardHelper;
import infinitespire.interfaces.IQuestLine;

import java.util.ArrayList;

public class EndlessQuestPart2 extends Quest implements IQuestLine{

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
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/boss.png");
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

	@Override
	public Quest getNextQuestInLine() {
		return new EndlessQuestPart3();
	}
}
