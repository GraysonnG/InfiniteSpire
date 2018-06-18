package infinitespire.perks.red;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import infinitespire.perks.AbstractPerk;

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
	public void onAddPower(AbstractPower p, AbstractCreature target, AbstractCreature source, int[] amount) {
		if((p instanceof StrengthPower || p instanceof LoseStrengthPower) && p.owner == AbstractDungeon.player) {
			if(amount[0] <= 0 || p.amount <= 0)
				return;
			
			if(target.hasPower(p.ID)) {
				amount[0] *= 2;
				target.getPower(p.ID).updateDescription();
			} else {
				p.amount *= 2;
				p.updateDescription();
			}
		}
	}
	
}
