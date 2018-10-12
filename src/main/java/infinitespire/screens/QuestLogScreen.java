package infinitespire.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.patches.ScreenStatePatch;
import infinitespire.quests.QuestLog;

import java.util.ArrayList;

public class QuestLogScreen {
	
	private QuestLog gameQuestLog;
	
	private ArrayList<Hitbox> hbs = new ArrayList<Hitbox>();
	private boolean justClicked = false;
	private boolean justClickedRight = false;
	private float completedAlpha = 0f;
	private float completedSin = 0f;
	private float yScale;

	public boolean openedDuringReward;
	
	public QuestLogScreen(QuestLog log) {
		gameQuestLog = log;
	}

	public void open() {
		AbstractDungeon.player.releaseCard();
		gameQuestLog.hasUpdate = false;
		AbstractDungeon.screen = ScreenStatePatch.QUEST_LOG_SCREEN;
		AbstractDungeon.overlayMenu.showBlackScreen();
		AbstractDungeon.overlayMenu.proceedButton.hide();
		AbstractDungeon.overlayMenu.cancelButton.show("Return");
		AbstractDungeon.isScreenUp = true;
		this.gameQuestLog = InfiniteSpire.questLog;

		if (MathUtils.randomBoolean()) {
			CardCrawlGame.sound.play("MAP_OPEN", 0.1f);
		}
		else {
			CardCrawlGame.sound.play("MAP_OPEN_2", 0.1f);
		}

		hbs.clear();

		for(Quest quest : InfiniteSpire.questLog) {
			quest.abandon = false;
			hbs.add(new Hitbox(480f * Settings.scale, 96f * Settings.scale));
		}
		this.yScale = 0.0f;
	}

	public void close() {
		AbstractDungeon.overlayMenu.cancelButton.hide();
		if (MathUtils.randomBoolean()) {
			CardCrawlGame.sound.play("MAP_OPEN", 0.1f);
		}
		else {
			CardCrawlGame.sound.play("MAP_OPEN_2", 0.1f);
		}
	}

	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		
		int rI = 0, gI = 0, bI = 0;
		int i = 0;
		for(Quest quest : gameQuestLog) {
			int index = i;
			switch(quest.type) {
			case BLUE:
				index = bI;
				bI++;
				break;
			case GREEN:
				index = gI;
				gI++;
				break;
			case RED:
				index = rI;
				rI++;
				break;
			}
			renderQuest(index, i, sb, quest);
			i++;
		}
		yScale = MathHelper.scaleLerpSnap(yScale, 1.0f);
		this.renderBanner(sb);
		this.renderTipScroll(sb);
		justClicked = false;
		justClickedRight = false;

		for(Quest quest : gameQuestLog){
			quest.render(sb);
		}
	}

	public void update() {
		if(InputHelper.justClickedLeft) {
			justClicked = true;
		}

		if(InputHelper.justClickedRight){
		    justClickedRight = true;
        }

		if(InfiniteSpire.questLog.hasUpdate){
		    InfiniteSpire.questLog.hasUpdate = false;
        }
	}

	public void renderBanner(SpriteBatch sb){
		float y = Settings.HEIGHT - 280.0f * Settings.scale;

		sb.setColor(Color.WHITE.cpy());
		sb.draw(ImageMaster.VICTORY_BANNER, Settings.WIDTH / 2.0f - 556.0f, y - 119.0f,
				556.0f, 119.0f, 1112.0f, 238.0f, Settings.scale, Settings.scale,
				0.0f, 0, 0, 1112, 238, false, false);
		FontHelper.renderFontCentered(sb, FontHelper.bannerFont, "Quest Log", Settings.WIDTH / 2.0f,
				y + 22.0f * Settings.scale, Color.WHITE, 1f);
	}

	private void renderTipScroll(SpriteBatch sb){
		float y = Settings.HEIGHT - 285.0f * Settings.scale;
		float x = 512f * Settings.scale * (yScale - 1f);
		float bannerOffset = 185f * Settings.scale;

		sb.setColor(Color.WHITE.cpy());
		sb.draw(InfiniteSpire.getTexture("img/infinitespire/ui/questLog/tipScroll.png"),
				x, y - bannerOffset, 512f * Settings.scale, 256 * Settings.scale);

		FontHelper.renderFontLeft(sb, FontHelper.topPanelAmountFont,"Tip: Right Click to Abandon", x + 25f * Settings.scale, y, Color.WHITE.cpy());
	}

	public void renderQuest(int index, int hbI, SpriteBatch sb, Quest quest) {
		float xOffset = 1;
		
		switch(quest.type) {
		case RED:
			xOffset = 1;
			break;
		case BLUE:
			xOffset = 2;
			break;
		case GREEN:
			xOffset = 3;
			break;
		}
		
		float width = 500f * Settings.scale;
		float xPos = ((Settings.WIDTH / 4f) * xOffset) - (width / 2f);
		float yPos = Settings.HEIGHT - (450f * Settings.scale);
		float textXOffset = 115f * Settings.scale;
		float textYOffset = 80f * Settings.scale;

		String boxString = quest.getRewardString();

		if(quest.isCompleted()) {
			boxString = "Claim: " + boxString;
		}else{
			boxString = "Reward: " + boxString;
		}

		if(quest.abandon){
			boxString = "Left Click to Confirm Abandon";
		}

		yPos -= (100 * Settings.scale) * index * yScale;
		
		Hitbox tempHitbox = hbs.get(hbI);
		tempHitbox.update(xPos + (10f * Settings.scale), yPos + (10f * Settings.scale));
		
		//Render the base Quest texutre in the color of the quest
		sb.setColor(quest.color);
		sb.draw(InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questBackground.png"), xPos, yPos, 0, 0, 500f, 116f, Settings.scale, Settings.scale, 0.0f, 0, 0, 500, 116, false, false);
        //Render the icon
		renderQuestIcon(sb, quest, xPos, yPos);

		if(quest.isNew || quest.isCompleted())
			sb.draw(InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questNewOverlay.png"), xPos, yPos, 0, 0, 500f, 116f, Settings.scale, Settings.scale, 0.0f, 0, 0, 500, 116, false, false);
		if(quest.abandon && !quest.isCompleted())
            sb.draw(InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questRemoveOverlay.png"), xPos, yPos, 0, 0, 500f, 116f, Settings.scale, Settings.scale, 0.0f, 0, 0, 500, 116, false, false);

		//Renders the name of the font
		FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, quest.getTitle(), xPos + textXOffset, yPos + textYOffset, Color.WHITE);
	
		if(!tempHitbox.hovered && !quest.isCompleted()) {
			//Render the progress bar
			renderQuestCompletionBar(sb, quest, xPos + textXOffset + (4f * Settings.scale), yPos + 25f * (Settings.scale));
			quest.abandon = false;
			quest.isHovered = false;
		} else {
			//Render a light alpha version of the quest texture above the normal one to make it look highlighted
			sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0f, 1.0f, 1.0f, quest.isCompleted() ? this.completedAlpha : 0.5f));
            sb.draw(InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questBackground.png"), xPos, yPos, 0, 0, 500f, 116f, Settings.scale, Settings.scale, 0.0f, 0, 0, 500, 116, false, false);
            sb.setBlendFunction(770, 771);
            sb.setColor(Color.WHITE);
            
            //Change the completedAlpha so completed quests "glow"
            this.completedSin += Gdx.graphics.getDeltaTime() * 4f;
			this.completedAlpha = ((float) Math.sin(completedSin) + 1f) / 2f;

			//Set isHovered
			quest.isHovered = true;

			//Onclick action
			FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, boxString, xPos + textXOffset + ((384f * Settings.scale) / 2), yPos + 35f * (Settings.scale), Color.WHITE);
			if(justClicked && (quest.isCompleted() || quest.abandon) && tempHitbox.hovered) {
				InfiniteSpire.logger.info("I am being removed:" + quest.getTitle());
				quest.removeQuest();
			}
			if(justClickedRight && !quest.isCompleted() && tempHitbox.hovered){
			    quest.abandon = !quest.abandon;
            }
		}

		if(tempHitbox.justHovered) {
			CardCrawlGame.sound.play("UI_HOVER");
			quest.isNew = false;
		}
		
		tempHitbox.render(sb);
	}
	
	private void renderQuestCompletionBar(SpriteBatch sb, Quest quest, float xPos, float yPos) {
		
		xPos += 15f * Settings.scale;
		
		float hbSize = 20f * Settings.scale;
		float fullWidth = 330 * Settings.scale;

		sb.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		sb.draw(ImageMaster.HB_SHADOW_L, xPos - hbSize, yPos, hbSize, hbSize);
        sb.draw(ImageMaster.HB_SHADOW_B, xPos, yPos, fullWidth, hbSize);
        sb.draw(ImageMaster.HB_SHADOW_R, xPos + fullWidth, yPos, hbSize, hbSize);
        if(quest.getCompletionPercentage() > 0.0f) {
			sb.setColor(new Color(161f / 255f, 212f / 255f, 112f / 255f, 255f));
			sb.draw(ImageMaster.HB_SHADOW_L, xPos - hbSize, yPos, hbSize, hbSize);
	        sb.draw(ImageMaster.HB_SHADOW_B, xPos, yPos, fullWidth * quest.getCompletionPercentage(), hbSize);
	        sb.draw(ImageMaster.HB_SHADOW_R, xPos + fullWidth * quest.getCompletionPercentage(), yPos, hbSize, hbSize);
        }
       
        FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, quest.currentSteps + "/" + quest.maxSteps, xPos + fullWidth / 2f, yPos + (10 * Settings.scale), Color.WHITE);
	}

	private void renderQuestIcon(SpriteBatch sb, Quest quest, float xPos, float yPos){
		float x = xPos + 10 * Settings.scale;
		float y = yPos + 10 * Settings.scale;
		float imgX = x + 4 * Settings.scale;
		float imgY = y + 4 * Settings.scale;

		Texture text = quest.getTexture();

		sb.setColor(Color.WHITE);
		sb.draw(text, imgX, imgY, 0, 0, 88, 88,
				Settings.scale, Settings.scale, 0.0f,
				0, 0, 96, 96,
				false, false);

		sb.setColor(quest.color);
		sb.draw(InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/frame.png"),
				x, y,
				0, 0, 96, 96,
				Settings.scale, Settings.scale, 0.0f,
				0, 0, 96, 96,
				false, false);
		sb.setColor(Color.WHITE);
	}

	@SuppressWarnings("unused")
	private Texture getQuestTexture(Texture text){
		Texture retVal = text;
		if(text.getWidth() > 96 || text.getHeight() > 96){
			if(!text.getTextureData().isPrepared()){
				text.getTextureData().prepare();
			}
			Pixmap mapOrig = text.getTextureData().consumePixmap();
			Pixmap mapProperSize = new Pixmap(96, 96, mapOrig.getFormat());

			int origW = mapOrig.getWidth();
			int origH = mapOrig.getHeight();

			int origS = origW >= origH ? origH : origW;

			int pSrcX = origW >= origH ? (origW - origH) / 2 : 0;
			int pSrcY = origH >= origW ? (origH - origW) / 2 : 0;

			int sclX = pSrcX / 3;
			int sclY = pSrcY / 3;

			mapProperSize.drawPixmap(mapOrig,
					pSrcX + sclX, pSrcY + sclY,
					origS - sclX * 2, origS - sclY * 2,
					0, 0,96,96);

			text = new Texture(mapProperSize);
			mapOrig.dispose();
			mapProperSize.dispose();
		}
		return text;
	}
}