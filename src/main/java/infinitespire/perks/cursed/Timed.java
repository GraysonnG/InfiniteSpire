package infinitespire.perks.cursed;

import infinitespire.perks.CursedPerk;

public class Timed extends CursedPerk {
	public static final String NAME = "Timed";
    public static final String ID = "Timed";
    private static final String DESCRIPTION = "After 5 turns start taking 5 damage per turn until the end of combat.";
    private static final int TIER = 0;
    private static final CursedPerkRarity RARITY = CursedPerkRarity.COMMON;

    public Timed() {
    	super(NAME, ID, DESCRIPTION, TIER, RARITY);
    	
    }
    
    
}
