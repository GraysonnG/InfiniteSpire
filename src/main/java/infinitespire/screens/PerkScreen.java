package infinitespire.screens;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import infinitespire.InfiniteSpire;
import infinitespire.patches.ScreenStatePatch;
import infinitespire.perks.AbstractPerk;

public class PerkScreen {
	
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	
	public static boolean isDone;
	private static boolean allowPurchase = false;
	public static boolean renderCurses;
	public static boolean hasPurchasedCurse = false;
	
	public Hitbox hitbox;
	
	public PerkScreen() {
		hitbox = new Hitbox(1520 * Settings.scale, 100 * Settings.scale, 100, 100);
	}

	public void update() {
		for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
			perk.update(allowPurchase);
		}
		
		hitbox.update();
		
		if(hitbox.hovered && InputHelper.justClickedLeft) {
			
		}
		
		hitbox.update(1520 * Settings.scale, 100 * Settings.scale);
	}
	
	public void render(SpriteBatch sb) {
		this.renderScroll(sb);
		if(!renderCurses) {
			this.renderPerksAndPrices(sb);
		} else {
			this.renderCursesAndPrices(sb);
		}
		
		hitbox.render(sb);
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
		AbstractDungeon.overlayMenu.proceedButton.hide();
		allowPurchase = b;
		renderCurses = false;
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
	
	public void renderCursesAndPrices(SpriteBatch sb) {
		for(AbstractPerk curses : InfiniteSpire.allCurses.values()) {
			curses.render(sb, hasPurchasedCurse);
		}
	}
}
