package infinitespire.screens;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import infinitespire.InfiniteSpire;
import infinitespire.patches.ScreenStatePatch;
import infinitespire.quests.Quest;
import infinitespire.quests.QuestLog;

public class QuestLogScreen {
	
	QuestLog gameQuestLog;
	ArrayList<Hitbox> hbs = new ArrayList<Hitbox>();
	
	public QuestLogScreen(QuestLog log) {
		gameQuestLog = log;
	}

	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		
		for(int i = 0; i < gameQuestLog.size(); i++) {
			Quest quest = gameQuestLog.get(i);
			renderQuest(i, sb, quest);
		}
	}

	public void update() {
		
	}

	public void open() {
		AbstractDungeon.player.releaseCard();
		gameQuestLog.hasUpdate = false;
		AbstractDungeon.screen = ScreenStatePatch.QUEST_LOG_SCREEN;
		AbstractDungeon.overlayMenu.showBlackScreen();
		AbstractDungeon.overlayMenu.proceedButton.hide();
		AbstractDungeon.overlayMenu.hideCombatPanels();
		AbstractDungeon.overlayMenu.cancelButton.show("Done.");
		AbstractDungeon.isScreenUp = true;
		if (MathUtils.randomBoolean()) {
            CardCrawlGame.sound.play("MAP_OPEN", 0.1f);
        }
        else {
            CardCrawlGame.sound.play("MAP_OPEN_2", 0.1f);
        }
		
		hbs.clear();
		
		for(int i = 0; i < InfiniteSpire.questLog.size(); i++) {
			hbs.add(new Hitbox(480f * Settings.scale, 96f * Settings.scale));
		}
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
	
	public void renderQuest(int index, SpriteBatch sb, Quest quest) {
		//96 - 480
		float xPos = Settings.WIDTH / 4f;
		float yPos = Settings.HEIGHT - (300f * Settings.scale);
		float textXOffset = 111f * Settings.scale;
		float textYOffset = 80f * Settings.scale;
		
		yPos -= (100 * Settings.scale) * index;
		
		Hitbox tempHitbox = hbs.get(index);
		tempHitbox.update(xPos + (10f * Settings.scale), yPos + (10f * Settings.scale));
		
		sb.setColor(quest.getColor());
		sb.draw(InfiniteSpire.getTexture("img/ui/questLog/questBackground.png"), xPos, yPos, 0, 0, 500f, 116f, Settings.scale, Settings.scale, 0.0f, 0, 0, 500, 116, false, false);
		sb.setColor(Color.WHITE);
		
		FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, quest.getTitle(), xPos + textXOffset, yPos + textYOffset, Color.WHITE);
	
		if(!tempHitbox.hovered) {
			renderQuestCompletionBar(sb, quest, xPos + textXOffset + (4f * Settings.scale), yPos + 25f * (Settings.scale));
		} else {
			FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, "Reward: " + quest.getRewardString(), xPos + textXOffset + ((384f * Settings.scale) / 2), yPos + 35f * (Settings.scale), Color.WHITE);
		}
		
		if(tempHitbox.justHovered) {
			CardCrawlGame.sound.play("UI_HOVER");
		}
		
		tempHitbox.render(sb);
	}
	
	public void renderQuestCompletionBar(SpriteBatch sb, Quest quest, float xPos, float yPos) {
		
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
       
        FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, quest.getCompletionString(), xPos + fullWidth / 2f, yPos + (10 * Settings.scale), Color.WHITE);
	}
}


















































