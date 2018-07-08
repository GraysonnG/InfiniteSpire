package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ReflectionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BrokenMirror extends Relic {
	
	public static final String ID = "Broken Mirror";
	
	public BrokenMirror() {
		super(ID, "brokenmirror", RelicTier.UNCOMMON, LandingSound.CLINK);
	}

	@Override
	public AbstractRelic makeCopy() {
		return new BrokenMirror();
	}

	@Override
	public void atBattleStart() {
		AbstractPlayer p = AbstractDungeon.player;
		AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new ReflectionPower(p, 1), 1));
	}
}
