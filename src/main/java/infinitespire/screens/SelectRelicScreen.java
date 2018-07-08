package infinitespire.screens;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.InfiniteSpire;
import infinitespire.patches.ScreenStatePatch;
import infinitespire.relics.Relic;

public class SelectRelicScreen {
	
	private AbstractRelic selectedRelic;
	private AbstractBlight selectedBlight;
	
	private ArrayList<Hitbox> relicHitboxes;
	private ArrayList<Hitbox> blightHitboxes;
	
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	private boolean itemSelected;
	private boolean selectingBlights;

	public void update() {
		if(!selectingBlights) {
			//for(Hitbox hb : relicHitboxes) hb.update();
		} else {
			//for(Hitbox hb : blightHitboxes) hb.update();
		}
	}
	
	public void updateRelics() {
		
	}
	
	public void updateBlights() {
		
	}
	
	
	
	
	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		if(!selectingBlights) {
			renderRelics(sb);
		} else {
			renderBlights(sb);
		}
	}
	
	private void renderBlights(SpriteBatch sb) {
		float x = 500f;
		float y = 500f;
		
		float xOffset = 10;
		
		for(AbstractBlight b : AbstractDungeon.blights) {
			sb.draw(b.largeImg, (x + xOffset) * Settings.scale, y);
			xOffset += 25;
		}
	}
	
	private void renderRelics(SpriteBatch sb) {
		float x = 20f;
		float y = Settings.HEIGHT / 2f;
		
		float xOffset = 10;
		
		for(AbstractRelic r : AbstractDungeon.player.relics) {
			if(r != null) {
				if(r instanceof Relic) {
					sb.draw(r.largeImg, (x + xOffset) * Settings.scale, y);
				}else {
					sb.draw(getTexture(r), (x + xOffset) * Settings.scale, y);
				}
				xOffset += 64f;
			}
		}
	}
	
	public void open() {
		this.open(false);
	}
	
	public void open(boolean selectingBlights) {
		AbstractDungeon.isScreenUp = true;
		AbstractDungeon.screen = ScreenStatePatch.SELECT_RELIC_SCREEN;
		AbstractDungeon.overlayMenu.showBlackScreen();
		AbstractDungeon.overlayMenu.proceedButton.hide();
		this.selectingBlights = selectingBlights;
	}
	
	private static Texture getTexture(AbstractRelic r) {
		Texture retVal = InfiniteSpire.getTexture("img/ui/missingtexture.png");
		if(textures.get("images/relics/" + r.imgUrl) == null) {
			loadTexture("images/relics/" + r.imgUrl);
		}
		retVal = textures.get("images/relics/" + r.imgUrl);
		return retVal;
	}
	
	private static void loadTexture(String s) {
		textures.put(s, ImageMaster.loadImage(s));
	}
}

