package infinitespire.effects.simpleVFX;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleXLEffect;

public class ColoredTorchParticleXLEffect extends TorchParticleXLEffect {

	public ColoredTorchParticleXLEffect(float x, float y, Color c){
		super(x, y);
		this.color = randColorRange(c);
	}

	private Color randColorRange(Color startColor){
		float r = startColor.r;
		float g = startColor.g;
		float b = startColor.b;
		float[] ary = {r, g, b};

		Vector2[] ranges = {new Vector2(), new Vector2(), new Vector2()};


		for(int i = 0; i < ary.length; i ++) {
			float comp = ary[i];

			if(comp == 1.0f){
				ranges[i] = new Vector2(0.7f, 1.0f);
			}else if(comp == 0.0f){
				ranges[i] = new Vector2(0.0f, 0.3f);
			}else{
				if(comp >= 0.5f) {
					ranges[i] = new Vector2(comp - 0.3f, comp);
				} else {
					ranges[i] = new Vector2(comp, comp + 0.3f);
				}
			}
		}

		return new Color(
			MathUtils.random(ranges[0].x, ranges[0].y),
			MathUtils.random(ranges[1].x, ranges[1].y),
			MathUtils.random(ranges[2].x, ranges[2].y),
			0.01f);
	}

	@Override
	public void render(SpriteBatch sb){
		sb.setBlendFunction(770,1);
		sb.setColor(this.color);

		float x = (float) ReflectionHacks.getPrivate(this, TorchParticleXLEffect.class, "x");
		float y = (float) ReflectionHacks.getPrivate(this, TorchParticleXLEffect.class, "y");

		TextureAtlas.AtlasRegion img = (TextureAtlas.AtlasRegion) ReflectionHacks.getPrivate(this, TorchParticleXLEffect.class, "img");

		sb.draw(
			img,
			x,
			y,
			img.packedWidth / 2.0f,
			img.packedHeight / 2.0f,
			(float) img.packedWidth,
			(float) img.packedHeight,
			this.scale,
			this.scale,
			this.rotation);
		sb.setBlendFunction(770, 771);
	}
}
