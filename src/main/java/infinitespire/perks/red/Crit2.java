package infinitespire.perks.red;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.perks.AbstractPerk;
import infinitespire.powers.CriticalPower;


public class Crit2 extends AbstractPerk{
	
	public static final String NAME = "Critical Lvl.2";
    public static final String ID = "Crit2";
    private static final String DESCRIPTION = "20% chance the next attack you deal does 2x damage.";
    private static final int TIER = 3;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.RED;
    private static final String[] PARENTS = {Crit1.ID};
    
    public Crit2() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, PARENTS);
	}

	@Override
	public void onCombatStart() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CriticalPower(AbstractDungeon.player, 0.20f)));
	}

}