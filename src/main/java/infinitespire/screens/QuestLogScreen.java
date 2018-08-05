package infinitespire.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import infinitespire.InfiniteSpire;
import infinitespire.patches.ScreenStatePatch;
import infinitespire.perks.AbstractPerk;
import infinitespire.quests.Quest;
import infinitespire.quests.QuestLog;

public class QuestLogScreen {
	
	private QuestLog gameQuestLog;
	private ArrayList<Hitbox> hbs = new ArrayList<Hitbox>();
	private boolean justClicked = false;
	private float completedAlpha = 0f;
	private float completedSin = 0f;
	
	public QuestLogScreen(QuestLog log) {
		gameQuestLog = log;
	}

	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		
		for(int i = gameQuestLog.size() - 1; i >= 0; i--) {
			Quest quest = gameQuestLog.get(i);
			renderQuest(i, sb, quest);
			if(quest.shouldRemove()) {
				quest.giveReward();
				gameQuestLog.remove(i);
			}
		}
		
		float perksX = 980f * Settings.scale;
		float perksY = Settings.HEIGHT - (400f * Settings.scale);
		
		renderPerks(sb, perksX, perksY);
		justClicked = false;
	}

	public void update() {
		if(InputHelper.justClickedLeft) {
			justClicked = true;
		}
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
		this.gameQuestLog = InfiniteSpire.questLog;
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
	
	public void renderPerks(SpriteBatch sb, float xPos, float yPos) {
		float xOffset = 0.0f * Settings.scale;
		float yOffset = 0.0f * Settings.scale;
		
		for(AbstractPerk perk : InfiniteSpire.allPerks.values()) {
			if(perk.state == AbstractPerk.PerkState.ACTIVE) {
				perk.renderInGame(sb, xPos + xOffset, yPos + yOffset);
			}
		}
	}
	
	public void renderQuest(int index, SpriteBatch sb, Quest quest) {
		//96 - 480
		float xPos = Settings.WIDTH / 4f;
		float yPos = Settings.HEIGHT - (400f * Settings.scale);
		float textXOffset = 111f * Settings.scale;
		float textYOffset = 80f * Settings.scale;
		
		yPos -= (100 * Settings.scale) * index;
		
		Hitbox tempHitbox = hbs.get(index);
		tempHitbox.update(xPos + (10f * Settings.scale), yPos + (10f * Settings.scale));
		
		//Render the base Quest texutre in the color of the quest
		sb.setColor(quest.color);
		sb.draw(InfiniteSpire.getTexture("img/ui/questLog/questBackground.png"), xPos, yPos, 0, 0, 500f, 116f, Settings.scale, Settings.scale, 0.0f, 0, 0, 500, 116, false, false);
		sb.setColor(Color.WHITE);
		
		//Renders the name of the font
		FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, quest.getTitle(), xPos + textXOffset, yPos + textYOffset, Color.WHITE);
	
		if(!tempHitbox.hovered && !quest.isCompleted()) {
			//Render the progress bar
			renderQuestCompletionBar(sb, quest, xPos + textXOffset + (4f * Settings.scale), yPos + 25f * (Settings.scale));
		} else {
			//Render a light alpha version of the quest texture above the normal one to make it look highlighted
			sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0f, 1.0f, 1.0f, quest.isCompleted() ? this.completedAlpha : 0.5f));
            sb.draw(InfiniteSpire.getTexture("img/ui/questLog/questBackground.png"), xPos, yPos, 0, 0, 500f, 116f, Settings.scale, Settings.scale, 0.0f, 0, 0, 500, 116, false, false);
            sb.setBlendFunction(770, 771);
            sb.setColor(Color.WHITE);
            
            //Change the completedAlpha so completed quests "glow"
            this.completedSin += Gdx.graphics.getDeltaTime() * 4f;
			this.completedAlpha = ((float) Math.sin(completedSin) + 1f) / 2f;
            
			//Onclick action
			FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, (quest.isCompleted() ? "Claim: " : "Reward: ") + quest.getRewardString(), xPos + textXOffset + ((384f * Settings.scale) / 2), yPos + 35f * (Settings.scale), Color.WHITE);
			if(justClicked && quest.isCompleted() && tempHitbox.hovered) {
				quest.removeQuest();;
			}
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
       
        FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, quest.currentSteps + "/" + quest.maxSteps, xPos + fullWidth / 2f, yPos + (10 * Settings.scale), Color.WHITE);
	}
}