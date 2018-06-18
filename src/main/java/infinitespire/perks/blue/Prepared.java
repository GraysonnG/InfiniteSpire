package infinitespire.perks.blue;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.perks.AbstractPerk;

public class Prepared extends AbstractPerk {
	public static final String NAME = "Prepared";
    public static final String ID = "Prepared";
    private static final String DESCRIPTION = "At the start of each combat draw #b2 more cards.";
    private static final int TIER = 0;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.BLUE;
    
    
    public Prepared() {
        super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR);
        
        this.state = PerkState.UNLOCKED;
    }
    
    @Override
    public void onCombatStart() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(player, 2));
    }
}
