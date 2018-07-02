package infinitespire.relics;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MidasBlood extends Relic {
	public static final String ID = "Midas Blood";
	
	public MidasBlood() {
		super(ID, "midasblood", RelicTier.UNCOMMON, LandingSound.MAGICAL);
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MidasBlood();
	}

	@Override
	public void onLoseHp(int damageAmount) {
		AbstractDungeon.player.gainGold(5);
		flash();
		CardCrawlGame.sound.play("GOLD_GAIN");
	}
}
