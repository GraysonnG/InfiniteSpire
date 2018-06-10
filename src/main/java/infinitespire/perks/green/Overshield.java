package infinitespire.perks.green;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.perks.AbstractPerk;

public class Overshield extends AbstractPerk{
	public static final String NAME = "Overshield";
    public static final String ID = "Overshield";
    private static final String DESCRIPTION = "At the start of each turn gain #y5 block.";
    private static final int TIER = 4;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.GREEN;
    
    public Overshield() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Retaliate.ID);
		this.xOffset += 75f;
	}

	@Override
	public void onTurnStart() {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 10));
	}
    
    
}
