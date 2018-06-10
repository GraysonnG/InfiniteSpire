package infinitespire.perks.green;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;

import infinitespire.perks.AbstractPerk;

public class Bulwark extends AbstractPerk {
	public static final String NAME = "Bulwark";
    public static final String ID = "Bulwark";
    private static final String DESCRIPTION = "Dexterity is now twice as effective.";
    private static final int TIER = 4;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.GREEN;
    
    public Bulwark() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Retaliate.ID);
		this.xOffset -= 75f;
	}

	@Override
	public void onAddPower(AbstractPower p) {
		if((p instanceof DexterityPower || p instanceof LoseDexterityPower) && p.owner == AbstractDungeon.player) {
			p.amount *= 2;
		}
	}
}
