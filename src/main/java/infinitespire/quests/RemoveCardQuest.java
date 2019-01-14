package infinitespire.quests;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.QuestHelper;

public class RemoveCardQuest extends Quest {

	private static final Color COLOR = new Color(0f, 0.5f, 1f, 1f);
	public int cost;
	public RemoveCardQuest() {
		super(RemoveCardQuest.class.getName(), COLOR, 5, QuestType.BLUE, QuestRarity.COMMON);
	}

	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		AbstractDungeon.player.gainGold(this.cost);
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/remove.png");
	}

	@Override
	public Quest createNew() {
		this.cost = QuestHelper.makeRandomCost(165);
		return this;
	}

	@Override
	public String getRewardString() {
		if (Settings.language == Settings.GameLanguage.FRA){
			return this.cost +  "Or";
		} else {
		return this.cost +  "g";
		}
	}

	@Override
	public String getTitle() {
		if (Settings.language == Settings.GameLanguage.FRA){
				return "Retirez 5 cartes";
		} else {
				return "Remove 5 Cards";
		}

	}

	@Override
	public Quest getCopy() {
		return new RemoveCardQuest();
	}
}
