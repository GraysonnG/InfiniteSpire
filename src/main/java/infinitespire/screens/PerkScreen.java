package infinitespire.screens;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;

import infinitespire.InfiniteSpire;
import infinitespire.patches.ScreenStatePatch;
import infinitespire.perks.AbstractPerk;

public class PerkScreen {
	
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	
	public static boolean isDone;
	private static boolean allowPurchase = false;
	
	public PerkScreen() {
		
	}

	public void update() {
		for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
			perk.update(allowPurchase);
		}
	}
	
	public void render(SpriteBatch sb) {
		this.renderScroll(sb);
		this.renderPerksAndPrices(sb);
		this.renderAvailablePoints(sb);
	}
	
	
	public void open() {
		this.open(false);
	}
	
	public void open(boolean b) {
		AbstractDungeon.isScreenUp = true;
		AbstractDungeon.screen = ScreenStatePatch.PERK_SCREEN;
		isDone = false;
		AbstractDungeon.overlayMenu.cancelButton.show("Done.");
		AbstractDungeon.overlayMenu.showBlackScreen();
		allowPurchase = b;
	}
	
	public void renderScroll(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		
		sb.draw(InfiniteSpire.getTexture("img/perks/scroll.png"), ((1920f - 1520f) / 2f) * Settings.scale, 0, 1500 * Settings.scale, 900 * Settings.scale);
	}
	
	public void renderPerksAndPrices(SpriteBatch sb) {
		for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
			perk.render(sb, allowPurchase);
		}
	}
	
	public void renderAvailablePoints(SpriteBatch sb) {
		FontHelper.renderFontCenteredTopAligned(sb, FontHelper.bannerFont, "Points: "+ InfiniteSpire.points, (1920f - 1520f) * Settings.scale, 850f, Color.WHITE);
	}
}
