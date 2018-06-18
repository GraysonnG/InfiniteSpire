package infinitespire.perks.red;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.InfiniteSpire;
import infinitespire.perks.AbstractPerk;
import infinitespire.powers.CriticalPower;

public class Crit1 extends AbstractPerk{
	
	public static final String NAME = "Critical Lvl.1";
    public static final String ID = "Crit1";
    private static final String DESCRIPTION = "10% chance the next attack you deal does 2x damage.";
    private static final int TIER = 2;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.RED;
    private static final String[] PARENTS = {PowerUp.ID, SpikedArmor.ID};
    
    public Crit1() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, PARENTS);
	}

	@Override
	public void onDamageDealt(DamageInfo info, int[] damageAmount) {
		if(willCrit(0.10f, info) && !(InfiniteSpire.allPerks.get(Crit2.ID).state == PerkState.ACTIVE)) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CriticalPower(AbstractDungeon.player), 1));
		}
	}
	
	public static boolean willCrit(float chance, DamageInfo info) {
		boolean retVal = false;
		
		if(!(info.type == DamageType.NORMAL))return false;
		
		float roll = (float)Math.random();
		if(roll < chance) {
			retVal = true;
		}
		
		return retVal;
	}
}
