package infinitespire.relics;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HolyWater extends Relic {

	public static final String ID = "Holy Water";
	
	public HolyWater() {
		super(ID, "holywater", RelicTier.RARE, LandingSound.CLINK);
	}

	@Override
	public AbstractRelic makeCopy() {
		return new HolyWater();
	}

	@Override
	public void onEquip() {
		int counter = 2;
		for(int i = AbstractDungeon.blights.size() - 1; (i >= 0 && AbstractDungeon.blights.size() > 2); i--) {
			if(counter > 0) {	
				AbstractDungeon.blights.get(i).flash();
				AbstractDungeon.blights.remove(i);
				counter--;
				CardCrawlGame.sound.play("");
			}
		}
		
		CardCrawlGame.sound.play("CARD_EXHAUST", 0.2f);
		this.flash();
	}

	
}
