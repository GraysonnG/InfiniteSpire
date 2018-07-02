package infinitespire.patches;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.InfiniteSpire;
import infinitespire.screens.PerkScreen;
import infinitespire.util.SuperclassFinder;

public class AbstractDungeonPatch {
	
	@SpirePatch(cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon", method="closeCurrentScreen")
	public static class CloseCurrentScreen {
		public static void Prefix() {
			if(AbstractDungeon.screen == ScreenStatePatch.PERK_SCREEN) {
				PerkScreen.isDone = true;
				
					try {
						Method overlayReset = SuperclassFinder.getSuperClassMethod(AbstractDungeon.class, "genericScreenOverlayReset");
						overlayReset.setAccessible(true);
						overlayReset.invoke(AbstractDungeon.class);
					} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
			
				AbstractDungeon.overlayMenu.hideBlackScreen();
			}
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "render")
	public static class Render {
		
		@SpireInsertPatch(rloc = 111) //112
		public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
			if(AbstractDungeon.screen == ScreenStatePatch.PERK_SCREEN)
				InfiniteSpire.perkscreen.render(sb);
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon", method = "update")
	public static class Update {
		
		@SpireInsertPatch(rloc = 94)
		public static void Insert(AbstractDungeon __instance) {
			if(AbstractDungeon.screen == ScreenStatePatch.PERK_SCREEN)
				InfiniteSpire.perkscreen.update();
		}
	}
}
