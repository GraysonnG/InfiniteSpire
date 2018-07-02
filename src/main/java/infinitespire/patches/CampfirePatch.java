package infinitespire.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;

import basemod.ReflectionHacks;
import infinitespire.ui.FlaskOption;

public class CampfirePatch {
	
	@SpirePatch(cls="com.megacrit.cardcrawl.rooms.CampfireUI", method="initializeButtons")
	public static class InitializeButtons {
		
		@SuppressWarnings("unchecked")
		public static void Postfix(CampfireUI __instance) {
			ArrayList<AbstractCampfireOption> campfireButtons = (ArrayList<AbstractCampfireOption>) ReflectionHacks.getPrivate(__instance, CampfireUI.class, "buttons");
		
			for(int i = 0; i < campfireButtons.size(); i++) {
				AbstractCampfireOption option = campfireButtons.get(i);
				if(option instanceof RestOption && AbstractDungeon.player.hasRelic("Magic Flask") && AbstractDungeon.player.getRelic("Magic Flask").counter > 0) {
					
					AbstractCampfireOption newOption = new FlaskOption();
					newOption.setPosition(option.hb.cX, option.hb.cY);
					campfireButtons.set(i, newOption);
				}
			}
		}
	}
}
