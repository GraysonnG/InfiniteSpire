package infinitespire.ui;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepScreenCoverEffect;

import infinitespire.InfiniteSpire;
import infinitespire.relics.MagicFlask;
import infinitespire.effects.CampfireFlaskEffect;

public class FlaskOption extends RestOption {
	
	public static boolean shouldReOpen = false;
	private Texture disabledImg;
	
	public FlaskOption() {
		super(true);
		this.img = InfiniteSpire.Textures.getUITexture("campfire/flaskoption.png");
		this.disabledImg = InfiniteSpire.Textures.getUITexture("campfire/flaskoption-disabled.png");
		this.usable = true;
		this.label = "Drink";
	}

	public void useOption() {
		if(!this.usable) return;
		
		CardCrawlGame.sound.play("SLEEP_BLANKET");
		AbstractDungeon.effectList.add(new CampfireFlaskEffect());
		for (int i = 0; i < 30; ++i) {
            AbstractDungeon.topLevelEffects.add(new CampfireSleepScreenCoverEffect());
        }
		MetricData metricData = CardCrawlGame.metricData;
        ++metricData.campfire_rested;
        CardCrawlGame.metricData.addCampfireChoiceData("REST");
        
        //I can assume i have the relic at this point because it let me do the thing
		//As usual assume made and ass out of me
        ((MagicFlask)AbstractDungeon.player.getRelic(MagicFlask.ID)).useFlask();
        shouldReOpen = true;
        this.usable = false;
        this.img = this.disabledImg;
	}

}
