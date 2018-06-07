package infinitespire.perks.green;

import infinitespire.perks.AbstractPerk;

public class Reinforce extends AbstractPerk {
	public static final String NAME = "Reinforce";
    public static final String ID = "Reinforce";
    private static final String DESCRIPTION = "Gain 10% more block.";
    private static final int TIER = 1;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.GREEN;
    
    public Reinforce() {
    	super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Fortify.ID);
    }
}
