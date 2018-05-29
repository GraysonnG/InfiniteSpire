package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import infinitespire.InfiniteSpire;
import infinitespire.perks.AbstractPerk;

public class AbstractPlayerEverythingPatch {
	//damage : 25
	@SpirePatch(cls="com.megacrit.cardcrawl.characters.AbstractPlayer", method="damage")
	public static class DamagePatch {
		@SpirePatch(cls = "com.megacrit.cardcrawl.characters.AbstractPlayer", method = "applyPreCombatLogic")
	    public static class ApplyPreCombatLogic
	    {
	        public static void Prefix(AbstractPlayer player) {
	            for (AbstractPerk perk : InfiniteSpire.allPerks.values()) {
	                if (perk.state.equals(AbstractPerk.PerkState.ACTIVE)) {
	                    perk.onCombatStart();
	                }
	            }
	        }
	    }
	    
	    @SpirePatch(cls = "com.megacrit.cardcrawl.characters.AbstractPlayer", method = "onVictory")
	    public static class OnVictory
	    {
	        public static void Prefix(AbstractPlayer player) {
	            for (AbstractPerk perk : InfiniteSpire.allPerks.values()) {
	                if (perk.state.equals(AbstractPerk.PerkState.ACTIVE)) {
	                    perk.onCombatVictory();
	                }
	            }
	        }
	    }
	    
	    @SpirePatch(cls = "com.megacrit.cardcrawl.characters.AbstractPlayer", method = "applyStartOfTurnRelics")
	    public static class ApplyStartOfTurnRelics
	    {
	        public static void Prefix(AbstractPlayer player) {
	            for (AbstractPerk perk : InfiniteSpire.allPerks.values()) {
	                if (perk.state.equals(AbstractPerk.PerkState.ACTIVE)) {
	                    perk.onTurnStart();
	                }
	            }
	        }
	    }
	    
	    @SpirePatch(cls = "com.megacrit.cardcrawl.characters.AbstractPlayer", method = "damage")
	    public static class Damage
	    {
	        @SpireInsertPatch(rloc = 25, localvars = { "damageAmount" })
	        public static void Insert(AbstractPlayer player, DamageInfo info, int damageAmount) {
	            for (AbstractPerk perk : InfiniteSpire.allPerks.values()) {
	            	if(perk.state.equals(AbstractPerk.PerkState.ACTIVE)) {
		                if (info.owner == player) {
		                    perk.onDamageDelt(info, damageAmount);
		                }
		                else {
		                    perk.onDamageTaken(info, damageAmount);
		                }
	            	}
	            }
	        }
	    }
	}
	//applyStartOfTurnRelics : prefix
	//onVictory : prefix
	//applyPreCombatLogic : prefix
}
