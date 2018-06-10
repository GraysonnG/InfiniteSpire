package infinitespire.patches;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;
import infinitespire.perks.AbstractPerk;
import infinitespire.util.SuperclassFinder;

public class AbstractCreatureEverythingPatch {
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());

	@SpirePatch(cls="com.megacrit.cardcrawl.core.AbstractCreature", method="renderPowerIcons")
	public static class RenderPowerIcons {
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
	
	@SpirePatch(cls="com.megacrit.cardcrawl.actions.common.ApplyPowerAction", method="update")
	public static class AddPower {
		@SpireInsertPatch(rloc=137, localvars={})
		public static void Insert(ApplyPowerAction __instance) {
			
			try {
				Field ptaField = SuperclassFinder.getSuperclassField(__instance.getClass(), "powerToApply");
				ptaField.setAccessible(true);
				AbstractPower p = (AbstractPower) ptaField.get(__instance);
				
				logger.info("Adding Power: " + p.ID + " | "+ p.amount + " | " + p.owner.name);
				
				for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
					if(perk.state == AbstractPerk.PerkState.ACTIVE) {
						perk.onAddPower(p);
					}
				}	
				
				
				p.updateDescription();
				
			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SpirePatch(cls="com.megacrit.cardcrawl.core.AbstractCreature", method="loseBlock")
	public static class LoseBlock {
		public static void Postfix(AbstractCreature creature, int amount, boolean noAnimation) {
			if(creature instanceof AbstractPlayer) {
				InfiniteSpire.logger.info("Player lost block.");
				for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
					perk.onLoseBlock(amount, noAnimation);
				}
			}
		}
	}
}
