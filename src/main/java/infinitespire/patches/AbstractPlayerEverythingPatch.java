package infinitespire.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.quests.DieQuest;
import infinitespire.relics.SneckoEssence;
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

	@SpirePatch(clz = AbstractPlayer.class, method = "renderHoverReticle")
	public static class SneckoEssencePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> doSneckoEssenceLogic(AbstractPlayer __instance, SpriteBatch sb) {
			if(!AbstractDungeon.player.hasRelic(SneckoEssence.ID))
				return SpireReturn.Continue();

			switch (__instance.hoveredCard.target) {
				case ALL:
				case SELF_AND_ENEMY:
					__instance.renderReticle(sb);
					AbstractDungeon.getCurrRoom().monsters.renderReticle(sb);
					break;
				case ALL_ENEMY:
				case ENEMY:
					AbstractDungeon.getCurrRoom().monsters.renderReticle(sb);
					break;
				case SELF:
				case NONE:
					return SpireReturn.Continue();
			}

			return SpireReturn.Return(null);
		}
	}
}
