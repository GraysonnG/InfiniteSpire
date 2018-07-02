package infinitespire.perks.blue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import infinitespire.perks.AbstractPerk;

public class Ancient extends AbstractPerk {
	public static final String NAME = "Ancient";
    public static final String ID = "Ancient";
    private static final String DESCRIPTION = "At the start of combat, gain #b1 #yArtifact.";
    private static final int TIER = 3;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.BLUE;
    
	public Ancient() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Untouchable.ID);
		
		this.xOffset -= 75f;
	}

	@Override
	public void onCombatStart() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, 1),1));
	}
	
	
}
