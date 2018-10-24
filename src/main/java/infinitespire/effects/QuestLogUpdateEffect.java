package infinitespire.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import infinitespire.InfiniteSpire;

public class QuestLogUpdateEffect extends AbstractGameEffect{

	private static final float EFFECT_DUR = 0.75f;
	private float scale;
	private Color color;
	private Texture texture;
	private float x, y;

	//TODO: make position make sense
	public QuestLogUpdateEffect() {
		this.scale = Settings.scale;
		this.color = new Color(1f, 1f, 1f, 1f);
		this.texture = InfiniteSpire.getTexture("img/infinitespire/vfx/updateRing.png");
		this.x = Settings.WIDTH - ((256f + 32f) * Settings.scale) - ((10.0f * Settings.scale) * 4f);
		this.y = Settings.HEIGHT - ((64f + 32f) * Settings.scale);
		this.duration = EFFECT_DUR;
	}
	
	@Override
	public void update() {
		this.duration -= Gdx.graphics.getDeltaTime();
		this.scale *= 1.0f + Gdx.graphics.getDeltaTime() * 2.5f;
		this.color.a = Interpolation.fade.apply(0.0f, 0.75f, this.duration / EFFECT_DUR);
		if(this.color.a < 0.0f)
			this.color.a = 0.0f;
		if(this.duration < 0.0f) {
			this.isDone = true;
			InfiniteSpire.questLog.hasUpdate = true;
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(this.color);
		sb.setBlendFunction(770, 771);
		sb.draw(this.texture, x, y, 64f, 64f, 128f, 128f, scale, scale, 0.0f, 0, 0, 128, 128, false, false);
	}
}
