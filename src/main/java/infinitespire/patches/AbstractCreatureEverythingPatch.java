package infinitespire.patches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

import infinitespire.InfiniteSpire;
import infinitespire.perks.AbstractPerk;

public class AbstractCreatureEverythingPatch {
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());

	@SpirePatch(cls="com.megacrit.cardcrawl.core.AbstractCreature", method="renderPowerIcons")
	public static class RenderPowerIcons{
		public static void Prefix(AbstractCreature __instance, SpriteBatch sb, float arg1, float arg2) {
			
			if(!(__instance instanceof AbstractPlayer))
				return;
			
			float x = 20f;
			float y = 900f;
			
			float xOffset = 0.0f * Settings.scale;
			float yOffset = 0.0f * Settings.scale;
			
			for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
				if(perk.state == AbstractPerk.PerkState.ACTIVE) {
					perk.renderInGame(sb, x + xOffset, y + yOffset);
				}
			}
		}
	}
}
