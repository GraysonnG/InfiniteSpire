package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;

public class PickUpCardTypeQuest extends PickUpCardQuest {

	public AbstractCard.CardType ctype;
	public static final int REWARD_AMOUNT = 2;

	public PickUpCardTypeQuest() {
		this.id = PickUpCardTypeQuest.class.getName();
		this.color = new Color(0f, 1f, 0.75f, 1f);
		this.type = QuestType.GREEN;
		this.rarity = QuestRarity.COMMON;
		this.maxSteps = 3;
	}

	@Override
	public String getTitle() {
		byte stringOffset = 5;

		if(this.ctype.equals(AbstractCard.CardType.SKILL))
			stringOffset += 1;
		if(this.ctype.equals(AbstractCard.CardType.POWER))
			stringOffset += 2;

		return questStrings.TEXT[stringOffset];
	}

	@Override
	public void giveReward() {
		InfiniteSpire.gainVoidShards(REWARD_AMOUNT);
	}

	@Override
	public Texture getTexture() {
		Texture texture;

		switch (this.ctype) {
			case ATTACK:
				texture = InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/card-attack.png");
				break;
			case POWER:
				texture = InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/card-power.png");
				break;
			default:
				texture = InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/card-skill.png");
				break;
		}

		return texture;
	}

	@Override
	public Quest createNew() {
		switch (AbstractDungeon.cardRng.random(2)) {
			case 0:
				this.ctype = AbstractCard.CardType.ATTACK;
				break;
			case 1:
				this.ctype = AbstractCard.CardType.SKILL;
				break;
			case 2:
				this.ctype = AbstractCard.CardType.POWER;
				break;
		}

		return this;
	}

	@Override
	public String getRewardString() {
		return voidShardStrings.TEXT[2] + REWARD_AMOUNT + voidShardStrings.TEXT[4];
	}

	@Override
	public Quest getCopy() {
		return new PickUpCardTypeQuest();
	}

	@Override
	public boolean isCard(AbstractCard card) {
		return card.type.equals(this.ctype);
	}

	@Override
	public boolean isSameQuest(Quest q) {
		return (q instanceof PickUpCardTypeQuest) && ((PickUpCardTypeQuest) q).ctype.equals(this.ctype);
	}
}
