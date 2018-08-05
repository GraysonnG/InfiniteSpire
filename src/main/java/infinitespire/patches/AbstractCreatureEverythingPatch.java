package infinitespire.patches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;
import infinitespire.perks.AbstractPerk;

public class AbstractCreatureEverythingPatch {
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	
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
	
	@SpirePatch(cls="com.megacrit.cardcrawl.core.AbstractCreature", method="loseBlock", paramtypes = {"int", "boolean"})
	public static class LoseBlock {
		public static void Postfix(AbstractCreature creature, int amount, boolean noAnimation) {
			if(creature instanceof AbstractPlayer) {
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
