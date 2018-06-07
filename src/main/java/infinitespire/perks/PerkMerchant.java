package infinitespire.perks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.InputHelper;

import infinitespire.InfiniteSpire;
import infinitespire.patches.ScreenStatePatch;

public class PerkMerchant {
	
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	private Hitbox hb = new Hitbox(((Settings.WIDTH / 4f) * 3f) - 300f, 250f, 250, 400);
	private float angle = 0.0f;
	
	
	public PerkMerchant() {
		InfiniteSpire.perkscreen.open();
		AbstractDungeon.screen = ScreenStatePatch.PERK_SCREEN;
	}

	public void render(SpriteBatch sb) {
		renderPortal(sb);
		hb.render(sb);
	}
	
	public void renderPortal(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		angle += 10 * Gdx.graphics.getDeltaTime();
		Texture portal = InfiniteSpire.getTexture("img/screen/portal.png");
		sb.draw(portal, 
				hb.x,
				hb.y + 150f, 
				128f, 
				128f, 
				256f, 
				256f, 
				1.0f, 
				1.0f, 
				angle,
				0,
				0,
				256,
				256,
				false,
				false
				);
	}

	public void update() {
		hb.update();
		
		if(hb.hovered && InputHelper.justClickedLeft) {
			InfiniteSpire.perkscreen.open();
		}
	}
}
