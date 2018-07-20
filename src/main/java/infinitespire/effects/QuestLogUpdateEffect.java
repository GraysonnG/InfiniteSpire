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

	private static final float EFFECT_DUR = 0.5f;
	private float scale;
	private Color color;
	private Texture texture;
	private float x, y;
	
	public QuestLogUpdateEffect() {
		this.scale = Settings.scale;
		this.color = new Color(1f, 1f, 1f, 1f);
		this.texture = InfiniteSpire.getTexture("img/vfx/updateRing.png");
//		this.x = Settings.WIDTH - ((256f + 32f) * Settings.scale) - ((10.0f * Settings.scale) * 4f);
//		this.y = Settings.HEIGHT - ((64f + 32f) * Settings.scale);
		
		this.x = Settings.WIDTH / 2;
		this.y = Settings.HEIGHT / 2;
		this.duration = EFFECT_DUR;
	}
	
	@Override
	public void update() {
		this.duration -= Gdx.graphics.getDeltaTime();
		this.scale *= 1.0f + Gdx.graphics.getDeltaTime() * 2.5f;
		this.color.a = Interpolation.fade.apply(0.0f, 0.75f, this.duration / EFFECT_DUR);
		if(this.color.a < 0.0f)
			this.color.a = 0.0f;
		if(this.duration < 0.0f)
			this.isDone = true;
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(this.color);
		sb.setBlendFunction(770, 1);
		sb.draw(this.texture, this.x, this.y, 128f / 2.0f, 128f / 2.0f, 128f, 128f, this.scale * 1.5f, this.scale * 1.5f);
		sb.setBlendFunction(770, 771);
	}

}
