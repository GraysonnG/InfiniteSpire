package infinitespire.ui.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import infinitespire.InfiniteSpire;
import infinitespire.patches.ScreenStatePatch;

public class QuestLogButton {
	
	private static float rotation = 0f;
	private static float yPos = Settings.HEIGHT - (64f * Settings.scale);
	private static float tipYPos = Settings.HEIGHT - (120.0f * Settings.scale);
	private static float xPos = Settings.WIDTH - (256f * Settings.scale);
	private static float padding = (10.0f * Settings.scale) * 4f;
	private static boolean isEnabled = true;
	private static Hitbox hb = new Hitbox(xPos - padding, yPos , 64f * Settings.scale, 64f * Settings.scale);
	
	public static void renderQuestLogButton(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		
		Texture texture = isEnabled ? 
				InfiniteSpire.questLog.hasUpdate ? InfiniteSpire.getTexture("img/ui/topPanel/questLogIcon-alert.png") : InfiniteSpire.getTexture("img/ui/topPanel/questLogIcon.png") : 
				InfiniteSpire.questLog.hasUpdate ? InfiniteSpire.getTexture("img/ui/topPanel/questLogIcon-disabled-alert.png") : InfiniteSpire.getTexture("img/ui/topPanel/questLogIcon-disabled.png");
		
		if(AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
			sb.draw(texture, xPos - padding, yPos, 32.0f, 32.0f, 64.0f, 64.0f, Settings.scale, Settings.scale, rotation, 0, 0, 64, 64, false, false);
			if (hb.hovered) {
	            sb.setBlendFunction(770, 1);
	            sb.setColor(new Color(1.0f, 1.0f, 1.0f, 0.25f));
	            sb.draw(texture, xPos - padding, yPos, 32.0f, 32.0f, 64.0f, 64.0f, Settings.scale, Settings.scale, rotation, 0, 0, 64, 64, false, false);
	            sb.setBlendFunction(770, 771);
	            
	            
	            renderToolTip(sb);
	        }
		}
		hb.render(sb);
	}
	
	public static void updateQuestLogButton() {
		hb.update();
		
		if(hb.hovered) {
			rotation = MathHelper.angleLerpSnap(rotation, 15.0f);
		} else {
			rotation = MathHelper.angleLerpSnap(rotation, 0.0f);
		}
		
		if(AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) return;
		
		if(hb.justHovered) {
			CardCrawlGame.sound.play("UI_HOVER");
		}
		
		if(hb.hovered && InputHelper.justClickedLeft && isEnabled) {
			onClick();
		}
		
		isEnabled = true;
		
		if(AbstractDungeon.isScreenUp) {
			if(AbstractDungeon.screen != ScreenStatePatch.QUEST_LOG_SCREEN) {
				isEnabled = false;
			} else {
				isEnabled = true;
			}
		}
	}
	
	public static void renderToolTip(SpriteBatch sb){
		sb.setColor(Color.CYAN);
        if (AbstractDungeon.screen != ScreenStatePatch.PERK_SCREEN) {
            TipHelper.renderGenericTip(xPos - padding, tipYPos, "Quest Log", "Description text here.");
        }
	}
	
	public static void onClick() {
		if(!AbstractDungeon.isScreenUp) {
			InfiniteSpire.questLogScreen.open();
		} else {
			AbstractDungeon.closeCurrentScreen();
			InfiniteSpire.questLogScreen.close();
		}
	}
}
