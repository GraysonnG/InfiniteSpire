package infinitespire.patches;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.events.thebeyond.SpireHeart;
import infinitespire.InfiniteSpire;
import infinitespire.screens.PerkScreen;
//import infinitespire.dungeons.ExordiumNewGamePlus;
import infinitespire.util.SuperclassFinder;

@Deprecated
public class SpireHeartPatch {
	
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	
	@SpirePatch(cls="com.megacrit.cardcrawl.events.thebeyond.SpireHeart", method="ctor")
	public static class Constructor{
		public static void Postfix(SpireHeart __instance) {
			//__instance.roomEventText.addDialogOption("[Infinite Spire] Start over again.");
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.events.thebeyond.SpireHeart", method = "buttonEffect")
	public static class ButtonEffect {
		public static void Postfix(SpireHeart __instance, int buttonPressed) {
//			try {
//				Field screenField = SuperclassFinder.getSuperclassField(__instance.getClass(), "screen");
//				screenField.setAccessible(true);
//				
//				Object screen = screenField.get(__instance);
//				
//				System.out.println(screen.toString());
//				if(buttonPressed == 1) {
//					CardCrawlGame.nextDungeon = Exordium.ID;
//			        AbstractDungeon.fadeOut();
//			        AbstractDungeon.isDungeonBeaten = true;
//			        PerkScreen.hasPurchasedCurse = false;
//			        if(AbstractDungeon.isAscensionMode && AbstractDungeon.ascensionLevel < 15) {
//						InfiniteSpire.ascensionLevel += 1;
//						AbstractDungeon.isAscensionMode = true;
//			        	AbstractDungeon.ascensionLevel = InfiniteSpire.ascensionLevel;
//			        }
//			        __instance.hasFocus = false;
//			        InfiniteSpire.isRerun = true;
//				} else {
//					InfiniteSpire.isRerun = false;
//				}
//			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
//				e.printStackTrace();
//			}
		}
	}
}
