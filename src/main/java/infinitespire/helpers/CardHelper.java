package infinitespire.helpers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

import java.util.ArrayList;

public class CardHelper {
	private static ArrayList<BlackCard> blackCards = new ArrayList<BlackCard>();
	
	public static ArrayList<AbstractCard> getBlackRewardCards() {
		ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
		int amountOfCardsToGive = 3;
		int attempts = 1000;
		
		do {
			boolean isUnique = true;
			BlackCard card = getRandomBlackCard();
			for(AbstractCard c : cards) {
				if(c.cardID.equals(card.cardID)) {
					isUnique = false;
				}
			}
			if(isUnique) {
				cards.add(card);
				amountOfCardsToGive--;
			}
			attempts--;
			if(attempts == 0) InfiniteSpire.logger.info("Could not find enough cards for reward.");
		} while(amountOfCardsToGive > 0 && attempts >= 0);
		return cards;
	}
	
	public static BlackCard getRandomBlackCard() {
		return blackCards.get(AbstractDungeon.cardRng.random(blackCards.size() - 1));
	}
	
	public static void addCard(AbstractCard card) {
		if(card instanceof BlackCard) {
			blackCards.add((BlackCard) card);
		}
		BaseMod.addCard(card);
	}

	public static BlackCard[] getBlackCards() {
		return blackCards.toArray(new BlackCard[0]);
	}
}
