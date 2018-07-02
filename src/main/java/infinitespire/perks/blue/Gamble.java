package infinitespire.perks.blue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConfusionPower;

import infinitespire.perks.AbstractPerk;

public class Gamble extends AbstractPerk{
	
	public static final String NAME = "Gamble";
    public static final String ID = "Gamble";
    private static final String DESCRIPTION = "At the start of combat, gain #yConfusion.";
    private static final int TIER = 3;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.BLUE;

    public Gamble() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Untouchable.ID);
		
		this.xOffset += 75f;
	}

	@Override
	public void onCombatStart() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConfusionPower(AbstractDungeon.player)));
	}
}
