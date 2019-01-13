package infinitespire.quests;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;

public class DieQuest extends Quest {

	public static final String ID = DieQuest.class.getName();
	private static final Color COLOR = new Color(0.25f, 0.25f, 0.25f, 1f);
	private static final QuestType TYPE = QuestType.BLUE;
	private static final int MAX_STEPS = 1;

	public DieQuest() {
		super(ID, COLOR, MAX_STEPS, TYPE, QuestRarity.RARE);
	}

	@Override
	public boolean autoClaim() {
		return true;
	}

	@Override
	public void giveReward() {
		AbstractPlayer p = AbstractDungeon.player;
		AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, (int)(p.maxHealth / 4f)));
	}

	@Override
	public Texture getTexture() {
		return InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/die.png");
	}

	@Override
	public String getRewardString() {
		if (Settings.language == Settings.GameLanguage.FRA){
				return "Soin \u00e9quivalent \u00e0 25% des PV max.";
		} else {
				return "Heal 25% max HP.";
		}

	}

	@Override
	public String getTitle() {
		if (Settings.language == Settings.GameLanguage.FRA){
					return "Mourrez!";
		} else {
				return "Die!";
		}
	}

	@Override
	public Quest createNew() {
		return this;
	}

	@Override
	public Quest getCopy() {
		return new DieQuest();
	}

	public void onPlayerDie() {
		this.incrementQuestSteps();
	}
}
