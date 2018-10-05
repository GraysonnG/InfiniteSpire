package infinitespire.quests.endless;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.interfaces.IQuestLine;
import infinitespire.monsters.Nightmare;
import infinitespire.quests.SlayQuest;

public class EndlessQuestPart1 extends SlayQuest implements IQuestLine{
	public EndlessQuestPart1() {
		this.id = EndlessQuestPart1.class.getName();
		this.color = new Color(0.75f, 0.0f, 1.0f, 1.0f);
		this.type = QuestType.BLUE;
		this.rarity = QuestRarity.SPECIAL;
		this.maxSteps = 1;
		this.monster = Nightmare.ID;
	}

	@Override
	public void giveReward() {
		Settings.isEndless = true;
		InfiniteSpire.isEndless = true;
		AbstractDungeon.topPanel.setPlayerName();
		CardCrawlGame.sound.play("UNLOCK_PING");
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/nightmare.png");
	}

	@Override
	public String getTitle() {
		return "Kill the Nightmare";
	}

	@Override
	public String getRewardString() {
		return "Unlock Endless";
	}

	@Override
	public Quest getCopy() {
		return new EndlessQuestPart1();
	}

	@Override
	public Quest createNew() {/*NOP*/ return this;}

	@Override
	public Quest getNextQuestInLine() {
		return new EndlessQuestPart2();
	}
}
