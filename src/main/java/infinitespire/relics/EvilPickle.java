package infinitespire.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;
import infinitespire.rewards.InterestReward;

public class EvilPickle extends Relic {
	public static final String ID = InfiniteSpire.createID("EvilPickle");

	public EvilPickle(){
		super(ID, "evilpickle", RelicTier.SPECIAL, LandingSound.FLAT);
	}

	@Override
	public void onTrigger() {
		this.flash();
		AbstractDungeon.combatRewardScreen.rewards.add(new InterestReward(Math.round(AbstractDungeon.player.gold * 0.065f)));
	}
}
