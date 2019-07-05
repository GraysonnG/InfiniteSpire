package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.events.shrines.Bonfire;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

@SpirePatch(clz = Bonfire.class, method = "setReward")
public class BonfirePatch {

	@SpireInsertPatch(
		rloc = 1,
		localvars = {"offeredCard"}
	)
	public static SpireReturn<Void> blackCardReward(Bonfire __instance, AbstractCard.CardRarity rarity, AbstractCard card) {
		if(card instanceof BlackCard) {
			InfiniteSpire.gainVoidShards(7);
			return SpireReturn.Return(null);
		}

		return SpireReturn.Continue();
	}
}
