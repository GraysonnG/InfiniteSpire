package infinitespire.relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.powers.BeetleShellPower;

public class BeetleShell extends Relic {

	public static final String ID = "Beetle Shell";
	
	public BeetleShell() {
		super(ID, "beetleshell", RelicTier.COMMON, LandingSound.SOLID);
		counter = 0;
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new BeetleShell();
	}

	@Override
	public int onPlayerGainedBlock(float blockAmount) {
		counter ++;
		this.stopPulse();
		AbstractPlayer p = AbstractDungeon.player;
		if(counter == 9) {
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p,p, new BeetleShellPower(p)));
			counter = -1;
			this.beginPulse();
		}
		else if (AbstractDungeon.player.hasPower("is_BeetleShellPower")) {
			AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(p, p, "is_BeetleShellPower"));
			return MathUtils.floor(blockAmount * 2);
		}
		return MathUtils.floor(blockAmount);
	}

	@Override
	public void atBattleStart() {
		AbstractPlayer p = AbstractDungeon.player;
		if(counter == -1) {
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p,p, new BeetleShellPower(p, true)));
			this.beginPulse();
		}
	}
	
	
	
	

}
