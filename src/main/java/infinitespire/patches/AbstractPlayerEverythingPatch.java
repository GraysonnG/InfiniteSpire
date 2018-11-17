package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.quests.DieQuest;
import javassist.CtBehavior;

public class AbstractPlayerEverythingPatch {
	@SpirePatch(cls="com.megacrit.cardcrawl.characters.AbstractPlayer", method="damage")
	public static class DamagePatch {
	    @SpirePatch(cls = "com.megacrit.cardcrawl.characters.AbstractPlayer", method = "damage")
	    public static class Damage {
	    	@SpireInsertPatch(locator = Locator.class)
	    	public static void Insert(AbstractPlayer player, DamageInfo info) {
	    		for(Quest q : InfiniteSpire.questLog) {
	    			if(q instanceof DieQuest) {
	    				((DieQuest)q).onPlayerDie();
	    			}
	    		}
	        }

	        public static class Locator extends SpireInsertLocator {
				@Override
				public int[] Locate(CtBehavior ctBehavior) throws Exception {
					Matcher matcher = new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion");

					return LineFinder.findInOrder(ctBehavior, matcher);
				}
			}
	    }
	}
}
