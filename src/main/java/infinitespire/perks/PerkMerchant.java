package infinitespire.perks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.InputHelper;

import infinitespire.InfiniteSpire;
import infinitespire.patches.ScreenStatePatch;

public class PerkMerchant {
	
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	private Hitbox hb = new Hitbox((Settings.WIDTH / 4f) * 3f, 250f, 100, 100);
	
	public PerkMerchant() {
		InfiniteSpire.perkscreen.open();
		AbstractDungeon.screen = ScreenStatePatch.PERK_SCREEN;
	}

	public void render(SpriteBatch sb) {
		hb.render(sb);
	}

	public void update() {
		hb.update();
		
		if(hb.hovered && InputHelper.justClickedLeft) {
			InfiniteSpire.perkscreen.open();
		}
	}
}
