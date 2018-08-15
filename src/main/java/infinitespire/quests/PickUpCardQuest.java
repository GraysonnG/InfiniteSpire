package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import infinitespire.abstracts.Quest;
import infinitespire.helpers.QuestHelper;

public class PickUpCardQuest extends Quest {
	
	private static final Color COLOR = new Color(0.5f, 1.0f, 0.25f, 1f);
	public String cardID;
	public int gold;

	public PickUpCardQuest() {
		super(PickUpCardQuest.class.getName(), COLOR, 1, QuestType.GREEN, QuestRarity.COMMON);
	}
	
	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		AbstractDungeon.player.gainGold(this.gold);
	}

	@Override
	public Quest createNew() {
		this.gold = QuestHelper.makeRandomCost(100);
		this.cardID = AbstractDungeon.returnTrulyRandomCard().cardID;
		return this;
	}

	@Override
	public String getRewardString() {
		return "Gain " + gold + " Gold. Remove a Card.";
	}

	@Override
	public String getTitle() {
		return "Pick up " + CardLibrary.getCardNameFromKey(cardID);
	}

	@Override
	public Quest getCopy() {
		return new PickUpCardQuest();
	}

	public boolean isCard(AbstractCard card) {
		return card.cardID.equals(this.cardID);
	}
}
