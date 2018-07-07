package infinitespire.patches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;
import infinitespire.perks.AbstractPerk;

public class AbstractCreatureEverythingPatch {
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());

	@SpirePatch(cls="com.megacrit.cardcrawl.core.AbstractCreature", method="renderPowerIcons")
	public static class RenderPowerIcons {
		public static void Prefix(AbstractCreature __instance, SpriteBatch sb, float arg1, float arg2) {
			
			if(!(__instance instanceof AbstractPlayer))
				return;
			
			float x = 20f;
			float y = 825f;
			
			float xOffset = 0.0f * Settings.scale;
			float yOffset = 0.0f * Settings.scale;
			
			for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
				if(perk.state == AbstractPerk.PerkState.ACTIVE) {
					perk.renderInGame(sb, x + xOffset, y + yOffset);
				}
			}
			
			FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelAmountFont, "Silver: " + InfiniteSpire.points, 125f * Settings.scale, 835f * Settings.scale, Color.WHITE);
		}
	}
	
	@SpirePatch(cls="com.megacrit.cardcrawl.actions.common.ApplyPowerAction", method="update")
	public static class AddPower {
		@SpireInsertPatch(rloc=6, localvars={"powerToApply","target","source","amount"})
		public static void Insert(ApplyPowerAction __instance, AbstractPower p, AbstractCreature target, AbstractCreature source, @ByRef int[] amount) {
			if(target.isPlayer) {
				logger.info("Modify Power: " + p.name);
				for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
					if(perk.state == AbstractPerk.PerkState.ACTIVE) {
						perk.onAddPower(p, target, source, amount);
					}
					p.updateDescription();
				}
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
	
	@SpirePatch(cls="com.megacrit.cardcrawl.core.AbstractCreature", method="addBlock")
	public static class AddBlock {
		@SpireInsertPatch(rloc=46,localvars = {"tmp"})
		public static void Insert(AbstractCreature __instance, int blockAmount, @ByRef int[] tmp) {
			if(__instance instanceof AbstractPlayer) {
				for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
					perk.onGainBlock(tmp[0]);
				}
			}
		}
	}
	
	
}
