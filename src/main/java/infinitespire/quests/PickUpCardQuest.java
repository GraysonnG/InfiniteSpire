package infinitespire.quests;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.NlothsGift;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.helpers.QuestHelper;

public class PickUpCardQuest extends Quest {

	private static final Color COLOR = new Color(0.5f, 1.0f, 0.25f, 1f);
	public String cardID;
	public int gold;



	public PickUpCardQuest() {
		super(PickUpCardQuest.class.getName(), COLOR, 1, QuestType.GREEN, QuestRarity.COMMON);
	}

	public void render(SpriteBatch sb){
		if(isHovered) {
			if(this.cardID != null) {
				AbstractCard rCard = CardLibrary.getCard(this.cardID);
				if (rCard != null) {
					rCard.drawScale = 0.75f;
					rCard.current_x = InputHelper.mX - (rCard.hb.width / 2f);
					rCard.current_y = InputHelper.mY;
					rCard.render(sb);
				}
			}
		}
	}


	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		AbstractDungeon.player.gainGold(this.gold);
	}

	@Override
	public Texture getTexture() {
		Texture texture = InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/card-skill.png");
		AbstractCard c = null;

		if(cardID != null){
			c = CardLibrary.getCard(this.cardID);
		}

		if(c != null) {
			switch (CardLibrary.getCard(this.cardID).type) {
				case ATTACK:
					texture = InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/card-attack.png");
					break;
				case POWER:
					texture = InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/card-power.png");
					break;
				default:
					//basically dont load anything
					break;
			}
		}

		return texture;
	}

	@Override
	public Quest createNew() {
		this.gold = QuestHelper.makeRandomCost(100);
		AbstractCard.CardRarity rarity = getRandomRarity();
		AbstractCard card = null;
		if(AbstractDungeon.player.hasRelic(PrismaticShard.ID)) {
			card = CardLibrary.getAnyColorCard(rarity);
		}else{
			card = AbstractDungeon.getCard(rarity);
		}

		this.cardID = card.cardID;
		return this;
	}

	private AbstractCard.CardRarity getRandomRarity() {
		int rareRate = 3;
		if(AbstractDungeon.player.hasRelic(NlothsGift.ID)) rareRate = 9;
		int roll = AbstractDungeon.cardRng.random(99);

		if(roll < rareRate){
			return AbstractCard.CardRarity.RARE;
		}

		if(roll < 40){
			return AbstractCard.CardRarity.UNCOMMON;
		}

		return AbstractCard.CardRarity.COMMON;
	}

	@Override
	public String getRewardString() {
		String currency;
		String gain;
		if (Settings.language == Settings.GameLanguage.FRA){
				currency = " Or.";
				gain = "Gagnez ";
		} else {
				currency = "g";
				gain = "Gain ";
		}
		return gain + gold + currency;
	}

	@Override
	public String getTitle() {
		String pick;
		if (Settings.language == Settings.GameLanguage.FRA){
				pick = "R\u00e9cup\u00e9rez ";
		} else {
			pick = "Pick up ";
		}
		return pick + CardLibrary.getCardNameFromKey(cardID);
	}

	@Override
	public Quest getCopy() {
		return new PickUpCardQuest();
	}

	public boolean isCard(AbstractCard card) {
		return card.cardID.equals(this.cardID);
	}
}
