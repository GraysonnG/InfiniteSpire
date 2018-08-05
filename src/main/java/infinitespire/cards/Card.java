package infinitespire.cards;

import basemod.abstracts.CustomCard;
import infinitespire.InfiniteSpire;

public abstract class Card extends CustomCard {

	public Card(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color,
			CardRarity rarity, CardTarget target) {
		super(InfiniteSpire.createID(id), name, img, cost, rawDescription, type, color, rarity, target);
	}

}
