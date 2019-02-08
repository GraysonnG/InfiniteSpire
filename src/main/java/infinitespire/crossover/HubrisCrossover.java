package infinitespire.crossover;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import infinitespire.abstracts.BlackCard;

import java.util.ArrayList;

public class HubrisCrossover {

	@SuppressWarnings("unchecked")
	public static boolean isDuctTapeBlackCard(AbstractCard card){
		if(!(card instanceof DuctTapeCard)) {
			return false;
		}

		DuctTapeCard c = (DuctTapeCard) card;
		ArrayList<AbstractCard> cards = (ArrayList<AbstractCard>) ReflectionHacks.getPrivate(c, DuctTapeCard.class, "cards");
		if(cards == null) return false;
		for(AbstractCard dCard: cards){
			if(dCard instanceof BlackCard) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Integer> getIndexsOfHubrisBlackCard(AbstractCard card) {
		ArrayList<Integer> ints = new ArrayList<>();
		ArrayList<AbstractCard> cards = (ArrayList<AbstractCard>) ReflectionHacks.getPrivate(card, DuctTapeCard.class, "cards");
		if(cards == null) return ints;
		for(int i = 0; i < cards.size(); i++){
			if(cards.get(i) instanceof BlackCard) {
				ints.add(i);
			}
		}

		return ints;
	}
}
