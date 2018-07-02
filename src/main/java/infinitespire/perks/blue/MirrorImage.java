package infinitespire.perks.blue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EchoPower;

import infinitespire.perks.AbstractPerk;

public class MirrorImage extends AbstractPerk {
	public static final String NAME = "Mirror Image";
    public static final String ID = "MirrorImage";
    private static final String DESCRIPTION = "The first card you play each combat is played twice.";
    private static final int TIER = 4;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.BLUE;
	public MirrorImage() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, new String[] {Ancient.ID, Gamble.ID});
	}
	
	public void onCombatStart() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EchoPower(AbstractDungeon.player, 1), 1));
	}
}
