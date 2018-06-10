package infinitespire.perks.red;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.perks.AbstractPerk;
import infinitespire.powers.SpikedArmorPower;

public class SpikedArmor extends AbstractPerk {
	public static final String NAME = "Spiked Armor";
    public static final String ID = "SpikedArmor";
    private static final String DESCRIPTION = "When gaining Block deal #y1 damage.";
    private static final int TIER = 1;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.RED;
    
    public SpikedArmor() {
    	super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Strengthen.ID);
    	this.xOffset += 75f;
    }

	@Override
	public void onCombatStart() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpikedArmorPower(AbstractDungeon.player, 1)));
	}
    
    
}
