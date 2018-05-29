package infinitespire.perks.blue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import infinitespire.perks.AbstractPerk;

public class Prepared extends AbstractPerk {
	public static final String NAME = "Prepared";
    public static final String ID = "Prepared";
    private static final String DESCRIPTION = "At the start of each turn draw 1 extra card.";
    private static final int TIER = 0;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.BLUE;
    
    
    public Prepared() {
        super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR);
    }
    
    @Override
    public void onCombatStart() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new StrengthPower(player, 2), 2));
    }
}
