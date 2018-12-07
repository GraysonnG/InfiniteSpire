package infinitespire.effects.uniqueVFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import infinitespire.InfiniteSpire;

import java.util.ArrayList;


public class MenacingEffect extends AbstractGameEffect {
	private static final float PARTICLE_SIZE = 0.3f;
	private static final float PARTICLE_SIZE_SUB = 0.1f;
	private static final float PARTICLE_MAX_SIZE = 0.45f;
	private static final float PARTICLE_MIN_SIZE = 0.1f;
	private static final float PARTICLE_JITTER_SIZE = 0.025f;


	private int amountOfMenace;
	private ArrayList<MenaceParticle> particles = new ArrayList<>();

	public MenacingEffect() {
		this.amountOfMenace = 3;

		for(int i = 0; i < amountOfMenace; i++){
			Vector2 pos = getPositionOnCircle(i);
			MenaceParticle particle = new MenaceParticle(pos.x, pos.y, PARTICLE_SIZE * Settings.scale - (PARTICLE_SIZE_SUB * Settings.scale * (i)), 0.5f);
			particles.add(particle);
		}
	}

	private Vector2 getPositionOnCircle(int pos){
		Vector2 result = new Vector2();
		result.x = AbstractDungeon.player.hb.cX + (AbstractDungeon.player.hb.width / 4f) * (float) Math.cos(15.0 * pos);
		result.y = AbstractDungeon.player.hb.cY + (AbstractDungeon.player.hb.height / 4f) * (float) Math.sin(15.0 * pos);
		return result;
	}

	@Override
	public void update() {
		for(MenaceParticle particle : particles){
			particle.update();
		}
		particles.removeIf(MenaceParticle::isDead);
		if(particles.size() <= 0) {
			this.isDone = true;
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		for(MenaceParticle particle : particles){
			particle.render(sb);
		}
	}

	private static class MenaceParticle {
		public float x, y, size, lifeSpan;

		public MenaceParticle(float x, float y, float size, float lifeSpan) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.lifeSpan = lifeSpan;
		}

		public void update() {
			this.lifeSpan -= Gdx.graphics.getDeltaTime();
			size = (new Random()).randomBoolean() ? size - PARTICLE_JITTER_SIZE * Settings.scale : size + PARTICLE_JITTER_SIZE * Settings.scale;
			if(size < PARTICLE_MIN_SIZE * Settings.scale){
				size = PARTICLE_MIN_SIZE * Settings.scale;
			}else if(size > PARTICLE_MAX_SIZE * Settings.scale){
				size = PARTICLE_MAX_SIZE * Settings.scale;
			}
		}

		public boolean isDead(){
			return lifeSpan <= 0.0f;
		}

		public void render(SpriteBatch sb) {
			sb.setColor(Color.WHITE.cpy());

			sb.draw(InfiniteSpire.getTexture("img/infinitespire/vfx/menacing.png"), this.x, this.y, 128f, 128f, 256f, 256f, size, size, 1f, 0, 0, 256, 256, false, false);
		}
	}

	@Override
	public void dispose() {

	}
}
