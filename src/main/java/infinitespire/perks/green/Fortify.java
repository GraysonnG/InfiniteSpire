package infinitespire.perks.green;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

import infinitespire.perks.AbstractPerk;

public class Fortify extends AbstractPerk{
	public static final String NAME = "Fortify";
    public static final String ID = "Fortify";
    private static final String DESCRIPTION = "At the start of combat, gain 2 Dexterity.";
    private static final int TIER = 0;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.GREEN;
    
    
    public Fortify() {
        super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR);
        
        this.state = PerkState.UNLOCKED;
    }
    
    @Override
    public void onCombatStart() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DexterityPower(player, 2), 2));
    }
}
