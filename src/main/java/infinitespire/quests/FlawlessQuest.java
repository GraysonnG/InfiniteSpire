package infinitespire.quests;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
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
		AbstractRelic relic = AbstractDungeon.returnRandomRelicEnd(RelicTier.RARE);
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
		if (Settings.language == Settings.GameLanguage.FRA){
				return "Obtenez une Relique Rare";
		} else {
			return "Receive a Rare Relic";
		}

	}

	@Override
	public String getTitle() {
		if (Settings.language == Settings.GameLanguage.FRA){
				return "Tuez le boss sans perdre de PV";
		} else {
			return "Flawless the Boss";
		}

	}

	@Override
	public Quest getCopy() {
		return new FlawlessQuest();
	}
}
