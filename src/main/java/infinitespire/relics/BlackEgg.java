package infinitespire.relics;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class BlackEgg extends Relic implements ClickableRelic, CustomSavable<Boolean> {
	public static final String ID = InfiniteSpire.createID("BlackEgg");
	private static RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);

	public boolean shouldSpawn;

	public BlackEgg(){
		super(ID, "blackegg", RelicTier.SPECIAL, LandingSound.HEAVY);
		shouldSpawn = false;
	}

	public String getUpdatedDescription(){
		return strings.DESCRIPTIONS[0] + strings.DESCRIPTIONS[1];
	}

	@Override
	public void onRightClick() {
		if(!shouldSpawn){
			shouldSpawn = true;
			this.beginPulse();
			this.pulse = true;
		} else {
			shouldSpawn = false;
			this.stopPulse();
			this.pulse = false;
		}
	}

	@Override
	public void onTrigger() {
		this.shouldSpawn = false;
		this.stopPulse();
		this.pulse = false;
	}

	@Override
	public Boolean onSave() {
		return shouldSpawn;
	}

	@Override
	public void onLoad(Boolean aBoolean) {
		shouldSpawn = aBoolean;
		if(shouldSpawn){
			this.beginPulse();
			this.pulse = true;
		}
	}
}
