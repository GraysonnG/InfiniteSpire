package infinitespire.relics;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class MidasBlood extends Relic {
	public static final String ID = InfiniteSpire.createID("Midas Blood");
	
	public MidasBlood() {
		super(ID, "midasblood", RelicTier.UNCOMMON, LandingSound.MAGICAL);
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MidasBlood();
	}

	@Override
	public void onLoseHp(int damageAmount) {
		AbstractDungeon.player.gainGold(2);
		flash();
		CardCrawlGame.sound.play("GOLD_GAIN");
	}
}
