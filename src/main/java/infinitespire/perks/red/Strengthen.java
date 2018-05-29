package infinitespire.perks.red;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import infinitespire.perks.AbstractPerk;

public class Strengthen extends AbstractPerk {
	public static final String NAME = "Strengthen";
    public static final String ID = "Strengthen";
    private static final String DESCRIPTION = "At the start of combat, gain 2 Strength.";
    private static final int TIER = 0;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.RED;
    
    
    public Strengthen() {
        super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR);
        
        this.state = PerkState.UNLOCKED;
    }
    
    @Override
    public void onCombatStart() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new StrengthPower(player, 2), 2));
    }
}
