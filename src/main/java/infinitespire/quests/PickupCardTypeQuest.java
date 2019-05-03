package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;

public class PickupCardTypeQuest extends PickUpCardQuest {

	public AbstractCard.CardType ctype;

	public PickupCardTypeQuest() {
		this.id = PickupCardTypeQuest.class.getName();
		this.color = new Color(0.25f, 0.75f, 0.25f, 1.0f);
		this.type = QuestType.GREEN;
		this.rarity = QuestRarity.RARE;
		this.maxSteps = 3;
	}

	@Override
	public String getTitle() {
		String typeString = "Attack";

		if(this.ctype.equals(AbstractCard.CardType.SKILL))
			typeString = "Skill";


		return "Pickup " + this.maxSteps + " " + typeString + " cards";
	}

	@Override
	public void giveReward() {

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
		return "Nothing...";
	}

	@Override
	public Quest getCopy() {
		return new PickupCardTypeQuest();
	}

	@Override
	public boolean isCard(AbstractCard card) {
		return card.type.equals(this.ctype);
	}

	@Override
	public boolean isSameQuest(Quest q) {
		return (q instanceof PickupCardTypeQuest) && ((PickupCardTypeQuest) q).ctype.equals(this.ctype);
	}
}
