package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import infinitespire.abstracts.BlackCard;
import infinitespire.helpers.CardHelper;

import java.util.ArrayList;
import java.util.Objects;

public class TransformPatch {

	@SpirePatch(clz = AbstractDungeon.class, method = "returnTrulyRandomCardFromAvailable", paramtypes = {"com.megacrit.cardcrawl.cards.AbstractCard", "com.megacrit.cardcrawl.random.Random"})
	public static class ReturnTrulyRandomCardFromAvailable {
		@SpirePrefixPatch
		public static SpireReturn<AbstractCard> InsertBlackCardTransform(AbstractCard prohibited, Random rng){
			ArrayList<AbstractCard> list = new ArrayList<>();

			if(prohibited instanceof BlackCard){
				for(BlackCard card : CardHelper.getBlackCards()){
					if(!Objects.equals(card.cardID, prohibited.cardID))
						list.add(card);
				}

				return SpireReturn.Return(list.get(rng.random(list.size() - 1)));
			}
			return SpireReturn.Continue();
		}
	}
}
