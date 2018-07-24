package infinitespire.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import infinitespire.InfiniteSpire;
import infinitespire.quests.Quest;

public class QuestLogUpdateEffect extends AbstractGameEffect{

	private static final float EFFECT_DUR = 0.75f;
	private float scale;
	private float questScale;
	private boolean isCompleted;
	private Quest quest;
	private Color color;
	private Texture texture;
	private float x, y;
	
	public QuestLogUpdateEffect() {
		this(InfiniteSpire.questLog.get(0));
	}
	
	public QuestLogUpdateEffect(Quest quest) {
		this.scale = Settings.scale;
		this.questScale = Settings.scale * 0.50f;
		this.color = new Color(1f, 1f, 1f, 1f);
		this.texture = InfiniteSpire.getTexture("img/vfx/updateRing.png");
		this.x = Settings.WIDTH - ((256f + 32f) * Settings.scale) - ((10.0f * Settings.scale) * 4f);
		this.y = Settings.HEIGHT - ((64f + 32f) * Settings.scale);
		this.isCompleted = quest.isCompleted();
		this.quest = quest;
		this.duration = EFFECT_DUR;
	}
	
	@Override
	public void update() {
		this.duration -= Gdx.graphics.getDeltaTime();
		this.scale *= 1.0f + Gdx.graphics.getDeltaTime() * 2.5f;
		this.questScale *= 1.0f + Gdx.graphics.getDeltaTime();
		this.color.a = Interpolation.fade.apply(0.0f, 0.75f, this.duration / EFFECT_DUR);
		if(this.color.a < 0.0f)
			this.color.a = 0.0f;
		if(this.duration < 0.0f) {
			this.isDone = true;
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(this.color);
		sb.setBlendFunction(770, 771);
		sb.draw(this.texture, x, y, 64f, 64f, 128f, 128f, scale, scale, 0.0f, 0, 0, 128, 128, false, false);
		
		if(isCompleted) {
			renderQuest(sb, quest);
		}
		
	}
	
	public void renderQuest(SpriteBatch sb, Quest quest) {
		float width = 500 * questScale;
		float height = 116 * questScale;
		float xPos = (Settings.WIDTH / 2f) - (width / 2f);
		float yPos = (Settings.HEIGHT / 2f) - (height / 2f) + (75f * Settings.scale);
		float textXOffset = 111f * questScale;
		float textYOffset = 80f * questScale;
		
		Color c = new Color(quest.getColor().r, quest.getColor().g, quest.getColor().b, 1.0f - this.color.a);
		Color c2 = new Color(1f, 1f, 1f, 1.0f - this.color.a);
		
		sb.setColor(c);
		sb.draw(InfiniteSpire.getTexture("img/ui/questLog/questBackground.png"), 
				xPos, 
				yPos, 
				width / 2f, 
				height / 2f,
				width, 
				height,
				1.0f, 
				1.0f, 
				0.0f, 
				0, 
				0, 
				500, 
				116, 
				false, 
				false);
		sb.setColor(Color.WHITE);
		
		float fontScaleX = FontHelper.panelNameFont.getScaleX();
		float fontScaleY = FontHelper.panelNameFont.getScaleY();
		
		FontHelper.panelNameFont.getData().setScale(fontScaleX * questScale, fontScaleY * questScale);
		
		FontHelper.renderFontLeft(
				sb, 
				FontHelper.panelNameFont, 
				quest.getTitle(), 
				xPos + textXOffset, 
				yPos + textYOffset, 
				c2);
		FontHelper.renderFontCentered(
				sb, 
				FontHelper.topPanelAmountFont, 
				"Reward: " + quest.getRewardString(), 
				xPos + textXOffset + ((384f * questScale) / 2), 
				yPos + 35f * (questScale), 
				c2, questScale);
	
		FontHelper.panelNameFont.getData().setScale(fontScaleX, fontScaleY);
	
	}

}
