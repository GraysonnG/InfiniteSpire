package infinitespire.perks.blue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RetainCardPower;

import infinitespire.perks.AbstractPerk;

public class Calculated extends AbstractPerk {
	public static final String NAME = "Calculated";
    public static final String ID = "Calculated";
    private static final String DESCRIPTION = "At the end of your turn retain #y1 card.";
    private static final int TIER = 1;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.BLUE;

    public Calculated() {
        super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Prepared.ID);
    }
    
    @Override
    public void onCombatStart() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new RetainCardPower(player, 1), 1));
    }
}
