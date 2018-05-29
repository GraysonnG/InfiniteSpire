package infinitespire.perks.red;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.perks.AbstractPerk;
import infinitespire.powers.PowerUpPower;

public class PowerUp extends AbstractPerk{
	
	public static final String NAME = "Power-Up";
    public static final String ID = "PowerUp";
    private static final String DESCRIPTION = "Attacks deal 10% more damage.";
    private static final int TIER = 1;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.RED;
	
	public PowerUp() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Strengthen.ID);
		this.xOffset -= 75f;
	}

	@Override
	public void onCombatStart() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PowerUpPower(AbstractDungeon.player)));
	}
}
