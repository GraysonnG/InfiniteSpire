package infinitespire.ragebar;

import basemod.BaseMod;
import basemod.interfaces.OnCardUseSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;

import java.util.function.Consumer;

public class RageBar implements OnCardUseSubscriber {

	private static float HB_SIZE = 20f * Settings.scale;
	private static float HB_SHADOW_SIZE = 26f * Settings.scale;

	private final float maxRage;
	private float currentRage;
	private float currentRageRender;
	private Color startColor;
	private Color endColor;
	private Color barColor;
	private AbstractCreature creature;
	private Hitbox hb;
	private Consumer<AbstractCreature> onFilled;

	public RageBar(AbstractCreature owner, float maxRage, Color barColor, Consumer<AbstractCreature> onFilled) {
		this.maxRage = maxRage;
		this.barColor = barColor.cpy();
		this.startColor = barColor.cpy();
		this.endColor = Color.RED.cpy();
		this.creature = owner;
		this.hb = new Hitbox(creature.hb.width, HB_SIZE);
		this.onFilled = onFilled;

		BaseMod.subscribe(this);
	}

	public void update() {
		this.hb.move(creature.hb.cX, creature.hb.y + (10f * Settings.scale));

		if(this.getRagePercent() >= 0.50f && this.getRagePercent() < 0.75f) {
			this.shake(0.5f);
		} else if(this.getRagePercent() > 0.75f && this.getRagePercent() < 0.9f) {
			this.shake(1.5f);
		} else if(this.getRagePercent() > 0.9f) {
			this.shake(3f);
		}

		if(currentRage != currentRageRender) {
			currentRageRender = MathHelper.scaleLerpSnap(currentRageRender, currentRage);
		}

		if(currentRage >= maxRage) {
			onRageFilled();
		}

		this.increaseCurrentRage(0.1f * Gdx.graphics.getDeltaTime());

		this.updateColor(getRagePercent());

		this.hb.update();
	}

	public void onRageFilled() {
		currentRage = 0;
		onFilled.accept(this.creature);
		//rage vfx
	}

	public void render(SpriteBatch sb) {
		float shadowOffsetX = 2f * Settings.scale;
		float shadowOffsetY = 3f * Settings.scale;

		float fullWidth = this.hb.width;
		float shadowFullWidth = fullWidth + 2f * shadowOffsetX * Settings.scale;

		sb.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
		sb.draw(ImageMaster.HB_SHADOW_L, hb.x - HB_SHADOW_SIZE - shadowOffsetX, hb.y - shadowOffsetY, HB_SHADOW_SIZE, HB_SHADOW_SIZE);
		sb.draw(ImageMaster.HB_SHADOW_B, hb.x - shadowOffsetX, hb.y - shadowOffsetY, shadowFullWidth, HB_SHADOW_SIZE);
		sb.draw(ImageMaster.HB_SHADOW_R, hb.x + shadowFullWidth - shadowOffsetX, hb.y - shadowOffsetY, HB_SHADOW_SIZE, HB_SHADOW_SIZE);

		sb.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		sb.draw(ImageMaster.HB_SHADOW_L, hb.x - HB_SIZE, hb.y, HB_SIZE, HB_SIZE);
		sb.draw(ImageMaster.HB_SHADOW_B, hb.x, hb.y, fullWidth, HB_SIZE);
		sb.draw(ImageMaster.HB_SHADOW_R, hb.x + fullWidth, hb.y, HB_SIZE, HB_SIZE);

		if(getRagePercent() > 0.0f) {
			sb.setColor(Color.ORANGE);
			sb.draw(ImageMaster.HB_SHADOW_L, hb.x - HB_SIZE, hb.y, HB_SIZE, HB_SIZE);
			sb.draw(ImageMaster.HB_SHADOW_B, hb.x, hb.y, fullWidth * getRagePercent(), HB_SIZE);
			sb.draw(ImageMaster.HB_SHADOW_R, hb.x + fullWidth * getRagePercent(), hb.y, HB_SIZE, HB_SIZE);
			sb.setColor(barColor);
			sb.draw(ImageMaster.HB_SHADOW_L, hb.x - HB_SIZE, hb.y, HB_SIZE, HB_SIZE);
			sb.draw(ImageMaster.HB_SHADOW_B, hb.x, hb.y, fullWidth * getRagePercentForRender(), HB_SIZE);
			sb.draw(ImageMaster.HB_SHADOW_R, hb.x + fullWidth * getRagePercentForRender(), hb.y, HB_SIZE, HB_SIZE);
		}
		sb.setColor(Color.WHITE.cpy());

		FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, "Rage: " + (int) (getRagePercent() * 100f) + "%", hb.cX, hb.cY);
	}
	
	public float getRagePercent() {
		return currentRage / maxRage;
	}

	private float getRagePercentForRender() {
		return currentRageRender / maxRage;
	}

	public void increaseCurrentRage(float amount) {
		if(amount >= 0) {
			this.currentRage += amount;
		}
	}

	private void shake(float amount) {
		Vector2 vector = (new Vector2(((float)Math.random() * 2f) - 1f, ((float)Math.random() * 2) - 1f)).nor();
		vector.x *= amount;
		vector.y *= amount;
		this.hb.update(hb.x + vector.x, hb.y + vector.y);
	}

	@Override
	public void receiveCardUsed(AbstractCard card) {
		currentRage++;

	}

	private void updateColor(float percent) {
		if (percent == 0)
			barColor.set(startColor);
		else if (percent == 1)
			barColor.set(endColor);
		else {
			float rSqA = (float) Math.pow((double)startColor.r * 255.0, 2.0);
			float gSqA = (float) Math.pow((double)startColor.g * 255.0, 2.0);
			float bSqA = (float) Math.pow((double)startColor.b * 255.0, 2.0);

			float rSqB = (float) Math.pow((double)endColor.r * 255.0, 2.0);
			float gSqB = (float) Math.pow((double)endColor.g * 255.0, 2.0);
			float bSqB = (float) Math.pow((double)endColor.b * 255.0, 2.0);

			float r = (float) Math.sqrt(rSqA + (rSqB - rSqA * percent) * percent) / 255f;
			float g = (float) Math.sqrt(gSqA + (gSqB - gSqA * percent) * percent) / 255f;
			float b = (float) Math.sqrt(bSqA + (bSqB - bSqA * percent) * percent) / 255f;
			float a = startColor.a + (endColor.a - startColor.a) * percent;

			barColor.set(r, g, b, a);
		}
	}
}
