package infinitespire.helpers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.cards.black.SealOfDarkness;

import java.util.ArrayList;

public class CardHelper {
	private static ArrayList<BlackCard> blackCards = new ArrayList<BlackCard>();

	public static ArrayList<AbstractCard> getBlackRewardCards() {
		return getBlackRewardCards(3);
	}

	public static ArrayList<AbstractCard> getBlackRewardCards(int amount) {
		ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
		int amountOfCardsToGive = amount;
		int attempts = 1000;
		
		do {
			boolean isUnique = true;
			AbstractCard card = getRandomBlackCard().makeStatEquivalentCopy();
			for(AbstractCard c : cards) {
				if(c.cardID.equals(card.cardID)) {
					isUnique = false;
				}
				if(c.cardID.equals(SealOfDarkness.ID)) {
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
		BlackCard card =  blackCards.get(AbstractDungeon.cardRng.random(blackCards.size() -1));
		UnlockTracker.markCardAsSeen(card.cardID);
		card.isSeen = true;
		return (BlackCard) card.makeStatEquivalentCopy();
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
