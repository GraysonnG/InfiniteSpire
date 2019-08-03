package infinitespire.effects.simpleVFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import infinitespire.InfiniteSpire;

public class YellowHeartParticleEffect extends AbstractGameEffect {
	private static final int RAW_W = 64;
	private float x, y;
	private float scale = Settings.scale / 2f;

	public YellowHeartParticleEffect(float x, float y) {
		this.duration = 2f;
		this.x = x;
		this.y = y;
		this.renderBehind = true;
		this.color = new Color(1f, 1f, 1f, 0f);
	}

	@Override
	public void update() {
		scale += Gdx.graphics.getDeltaTime() * Settings.scale * 1.1f;

		if(duration > 1f) {
			color.a = Interpolation.fade.apply(0f, 0.3f, 1f - (this.duration - 1f));
		} else {
			color.a = Interpolation.fade.apply(0.3f, 0f, 1f - duration);
		}

		duration -= Gdx.graphics.getDeltaTime();
		if(duration < 0f) {
			this.isDone = true;
		}
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE);
		spriteBatch.setColor(this.color.cpy());
		spriteBatch.draw(
			InfiniteSpire.Textures.getUITexture("intents/tempHp.png"),
			x - RAW_W / 2f,
			y - RAW_W / 2f,
			RAW_W / 2f,
			RAW_W / 2f,
			RAW_W,
			RAW_W,
			scale,
			scale,
			0f,
			0,
			0,
			RAW_W,
			RAW_W,
			false,
			false
		);
		spriteBatch.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void dispose() {

	}
}
