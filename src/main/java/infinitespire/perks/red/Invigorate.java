package infinitespire.perks.red;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.perks.AbstractPerk;
import infinitespire.powers.InvigoratePower;

public class Invigorate extends AbstractPerk {
	
	public static final String NAME = "Invigorate";
    public static final String ID = "Invigorate";
    private static final String DESCRIPTION = "Strength is twice as effective.";
    private static final int TIER = 4;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.RED;
	
	public Invigorate() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Crit2.ID);
	}

	@Override
	public void onCombatStart() {
		AbstractPlayer player = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new InvigoratePower(player)));
	}
	
	
}
