package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.NlothsGift;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;

public class PickUpCardQuest extends Quest {
	
	private static final Color COLOR = new Color(0.5f, 1.0f, 0.25f, 1f);
	public String cardID;
	private static final int REWARD_AMOUNT = 1;

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
		InfiniteSpire.gainVoidShards(REWARD_AMOUNT);
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
		return voidShardStrings.TEXT[2] + REWARD_AMOUNT + voidShardStrings.TEXT[4];
	}

	@Override
	public String getTitle() {

		return questStrings.TEXT[10] + CardLibrary.cards.get(cardID).name;
	}

	@Override
	public Quest getCopy() {
		return new PickUpCardQuest();
	}

	public boolean isCard(AbstractCard card) {
		return card.cardID.equals(this.cardID);
	}
}
