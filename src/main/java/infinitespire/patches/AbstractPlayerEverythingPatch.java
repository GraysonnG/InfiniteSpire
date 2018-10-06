package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.quests.DieQuest;

public class AbstractPlayerEverythingPatch {
	@SpirePatch(cls="com.megacrit.cardcrawl.characters.AbstractPlayer", method="damage")
	public static class DamagePatch {
	    @SpirePatch(cls = "com.megacrit.cardcrawl.characters.AbstractPlayer", method = "damage")
	    public static class Damage2 {
	    	//This covers Fairy in a bottle prevent death function
	    	//Inserted after: this.currentHealth = 0;
	    	@SpireInsertPatch(rloc = 89)
	    	public static void Insert(AbstractPlayer player, DamageInfo info) {
	    		for(Quest q : InfiniteSpire.questLog) {
	    			if(q instanceof DieQuest) {
	    				((DieQuest)q).onPlayerDie();
	    			}
	    		}
	        }
	    }
	    
	    @SpirePatch(cls = "com.megacrit.cardcrawl.characters.AbstractPlayer", method = "damage")
	    public static class Damage3 {
	    	//This covers Lizard Tail prevent death function
	    	//Inserted after: this.currentHealth = 0;
	    	@SpireInsertPatch(rloc = 98)
	    	public static void Insert(AbstractPlayer player, DamageInfo info) {
	    		for(Quest q : InfiniteSpire.questLog) {
	    			if(q instanceof DieQuest) {
	    				((DieQuest)q).onPlayerDie();
	    			}
	    		}
	        }
	    }
	}
}
