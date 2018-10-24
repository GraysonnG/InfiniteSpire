package infinitespire.effects.uniqueVFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import infinitespire.effects.simpleVFX.ColoredTorchParticleXLEffect;

public class UltimateFormEffect extends AbstractGameEffect {

	private float x, y;
	private static final float X_RADIUS = 200.0f * Settings.scale;
	private static final float Y_RADIUS = 200.0f * Settings.scale;

	private Vector2 v1;
	private Vector2 v2;

	public UltimateFormEffect(float x, float y) {
		this.v1 = new Vector2(0.0f, 0.0f);
		this.v2 = new Vector2(0.0f, 0.0f);
		this.duration = 1.0f;
		this.x = x;
		this.y = y;
		this.renderBehind = false;
	}

	@Override
	public void update() {
		if(duration == 1.0f){
			CardCrawlGame.sound.playA("ATTACK_FLAME_BARRIER",0.6f);
		}

		final float tmp = Interpolation.fade.apply(-290, 30, this.duration / 0.75f) * 0.017453292f;
		final float tmp2 = Interpolation.fade.apply(30, -290, this.duration / 0.75f) * 0.017453292f;
		this.v1.x = MathUtils.cos(tmp) * (X_RADIUS - 100f * Settings.scale);
		this.v1.y = MathUtils.sin(tmp) * (Y_RADIUS + 50f * Settings.scale);
		this.v2.x = MathUtils.cos(tmp2) * (X_RADIUS + 50f * Settings.scale);
		this.v2.y = MathUtils.sin(tmp2) * (Y_RADIUS - 100f * Settings.scale);
		AbstractDungeon.effectsQueue.add(new ColoredTorchParticleXLEffect(this.x + this.v1.x + MathUtils.random(-30.0f, 30.0f) * Settings.scale, this.y + this.v1.y + MathUtils.random(-10.0f, 10.0f) * Settings.scale, new Color((138f / 255f),0f,255f,1f)));
		AbstractDungeon.effectsQueue.add(new ColoredTorchParticleXLEffect(this.x + this.v1.x + MathUtils.random(-30.0f, 30.0f) * Settings.scale, this.y + this.v1.y + MathUtils.random(-10.0f, 10.0f) * Settings.scale, new Color((138f / 255f),0f,255f,1f)));
		AbstractDungeon.effectsQueue.add(new ColoredTorchParticleXLEffect(this.x + this.v2.x + MathUtils.random(-30.0f, 30.0f) * Settings.scale, this.y + this.v2.y + MathUtils.random(-10.0f, 10.0f) * Settings.scale, new Color(1f,0f,0f,1f)));
		AbstractDungeon.effectsQueue.add(new ColoredTorchParticleXLEffect(this.x + this.v2.x + MathUtils.random(-30.0f, 30.0f) * Settings.scale, this.y + this.v2.y + MathUtils.random(-10.0f, 10.0f) * Settings.scale, new Color(1f,0f,0f,1f)));
		//AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.x + this.v1.x, this.y + this.v1.y));

		this.duration -= Gdx.graphics.getDeltaTime();
		if(this.duration < 0.0f){
			this.isDone = true;
		}
	}

	@Override
	public void render(SpriteBatch spriteBatch) {

	}
}
