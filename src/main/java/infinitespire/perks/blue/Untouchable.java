package infinitespire.perks.blue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import infinitespire.perks.AbstractPerk;

public class Untouchable extends AbstractPerk{
	public static final String NAME = "Untouchable";
    public static final String ID = "Untouchable";
    private static final String DESCRIPTION = "At the start of combat gain #b1 #yIntangable.";
    private static final int TIER = 2;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.BLUE;
    
    public Untouchable() {
    	super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Calculated.ID);
    }
    @Override
    public void onCombatStart() {
    	AbstractPlayer player = AbstractDungeon.player;
    	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new IntangiblePlayerPower(player, 1), 1));
    }
}
