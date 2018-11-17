package infinitespire.helpers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.patches.CardColorEnumPatch;

import java.util.ArrayList;
import java.util.Map;

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
		ArrayList<String> keys = new ArrayList<>();
		for(Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
			if(c.getValue().color == CardColorEnumPatch.CardColorPatch.INFINITE_BLACK){
				keys.add(c.getKey());
			}
		}
		return (BlackCard) CardLibrary.cards.get(keys.get(AbstractDungeon.cardRng.random(0, keys.size() - 1)));
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
