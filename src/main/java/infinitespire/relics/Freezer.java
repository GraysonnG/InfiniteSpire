package infinitespire.relics;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.abstracts.Relic;

public class Freezer extends Relic {

	public static final String ID = "Freezer";
	
	public Freezer() {
		super(ID, "freezer", RelicTier.UNCOMMON, LandingSound.CLINK);
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new Freezer();
	}

	@Override
	public void atBattleStart() {
		flash();
		AbstractDungeon.actionManager.addToTop(new ChannelAction(new Frost()));
	}
}
