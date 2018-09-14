package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import infinitespire.InfiniteSpire;

public class UseCardActionPatch {

	public static final String CLS = "com.megacrit.cardcrawl.actions.utility.UseCardAction";

	@SpirePatch(cls = CLS, method = SpirePatch.CONSTRUCTOR, paramtypes = {"com.megacrit.cardcrawl.cards.AbstractCard", "com.megacrit.cardcrawl.core.AbstractCreature"})
	public static class Constructor {

		//Inserted after: if (card.exhaustOnUseOnce || card.exhaust) statement
		@SpireInsertPatch(rloc = 47)
		public static void BottledSoulCheck(UseCardAction __instance, AbstractCard card, AbstractCreature target){
			if(AbstractCardPatch.Field.isBottledSoulCard.get(card)){
				if(Loader.DEBUG) {
					InfiniteSpire.logger.info(card.name + " is Bottled!");
				}
				__instance.exhaustCard = false;
			}
		}
	}
}
