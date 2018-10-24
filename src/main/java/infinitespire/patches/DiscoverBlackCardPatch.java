package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import infinitespire.helpers.CardHelper;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz=CardRewardScreen.class, method = "discoveryOpen", paramtypez = {})
public class DiscoverBlackCardPatch {
	public static AbstractCard.CardColor lookingForColor = null;
	public static int lookingForCount = 3;
	public static AbstractCard lookingForProhibit = null;

	@SpireInsertPatch(locator = Locator.class, localvars = {"derp"})
	public static void Insert(CardRewardScreen __instance, ArrayList<AbstractCard> derp){
		if(lookingForColor != null) {
			derp.clear();
			while(derp.size() != lookingForCount) {
				boolean dupe = false;
				AbstractCard tmp = CardHelper.getRandomBlackCard().makeStatEquivalentCopy();
				if(tmp.hasTag(AbstractCard.CardTags.HEALING)){
					dupe = true;
				}

				for(AbstractCard c : derp) {
					if(c.cardID.equals(tmp.cardID)){
						dupe = true;
						break;
					}
				}

				if(!dupe) {
					AbstractCard c = tmp.makeCopy();
					derp.add(c);
				}
			}
			__instance.rewardGroup = derp;
			lookingForColor = null;
			lookingForCount = 3;
			lookingForProhibit = null;
		}
	}

	private static class Locator extends SpireInsertLocator
	{
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
		{
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardRewardScreen.class, "rewardGroup");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
