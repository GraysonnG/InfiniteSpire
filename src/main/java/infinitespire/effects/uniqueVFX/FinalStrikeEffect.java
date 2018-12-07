package infinitespire.effects.uniqueVFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;
import infinitespire.InfiniteSpire;

public class FinalStrikeEffect extends AbstractGameEffect{
	private boolean impactHook;
	private static TextureAtlas.AtlasRegion img;
	private float x, y, targetY;

	public FinalStrikeEffect(float x, float y) {
		this.impactHook = false;

		if(img == null) {
			img = ImageMaster.vfxAtlas.findRegion("combat/weightyImpact");
		}

		this.scale = Settings.scale;
		this.x = x - img.packedWidth / 2f;
		this.y = Settings.HEIGHT - img.packedHeight / 2f;
		this.duration = 1.0f;
		this.targetY = y - 180.0f * Settings.scale;
		this.rotation = MathUtils.random(-1.0f, 1.0f);
		this.color = Color.WHITE.cpy();
	}

	@Override
	public void update() {
		this.y = Interpolation.fade.apply(Settings.HEIGHT, this.targetY, 1.0f - this.duration / 1.0f);
		this.scale += Gdx.graphics.getDeltaTime();
		this.duration -= Gdx.graphics.getDeltaTime();

		if(this.duration < 0.0f) {
			this.isDone = true;
			CardCrawlGame.sound.playA("ATTACK_IRON_2",-0.5f);
		}else if(this.duration < 0.2f) {
			if(!this.impactHook){
				this.impactHook = true;
				AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Colors.get(InfiniteSpire.GDX_INFINITE_PURPLE_NAME).cpy()));
				CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);
				for(int i = 0; i < 5; i++){
					//Might do a custom this with cooler color
					AbstractDungeon.effectsQueue.add(new DamageImpactCurvyEffect(this.x + img.packedWidth / 2f, this.y + img.packedHeight / 2f));
				}

				for (int i = 0; i < 30; i ++){
					//Might also custom this with cooler color
					AbstractDungeon.effectsQueue.add(new UpgradeShineParticleEffect(
						this.x + MathUtils.random(-100.0F, 100.0F) * Settings.scale + img.packedWidth / 2.0F,
						this.y + MathUtils.random(-50f, 50f) * Settings.scale + img.packedHeight / 2.0f));
				}
			}
			this.color.a = Interpolation.fade.apply(0.0f, 0.5f, 0.2f / this.duration);
		}else{
			this.color.a = Interpolation.pow2Out.apply(0.6f, 0.0f, this.duration / 1.0f);
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setBlendFunction(770, 771);
		sb.setColor(Color.BLACK.cpy());
		sb.draw(img,
			this.x,
			this.y,
			img.packedWidth / 2.0f,
			img.packedHeight / 2.0f,
			img.packedWidth,
			img.packedHeight * (this.duration + 0.2f) * 5.0f,
			this.scale * MathUtils.random(0.99f, 1.01f),
			this.scale * MathUtils.random(0.99f, 1.01f) * (this.duration + 0.8f), this.rotation);

		sb.setBlendFunction(770, 1);
		sb.setColor(Colors.get(InfiniteSpire.GDX_INFINITE_PURPLE_NAME).cpy());
		sb.draw(img, this.x, this.y + 40.0f * Settings.scale,
			img.packedWidth / 2.0f,
			img.packedHeight / 2.0f,
			img.packedWidth,
			img.packedHeight * (this.duration + 0.2f) * 5.0f,
			this.scale * MathUtils.random(0.99f, 1.01f) * 0.7f,
			this.scale * MathUtils.random(0.99f, 1.01f) * 1.3f * (this.duration + 0.8f), this.rotation);

		sb.setColor(Colors.get(InfiniteSpire.GDX_INFINITE_RED_NAME).cpy());
		sb.draw(img,
			this.x,
			this.y + 140.0f * Settings.scale,
			img.packedWidth / 2.0f,
			img.packedHeight / 2.0f,
			img.packedWidth,
			img.packedHeight * (this.duration + 0.2f) * 5.0f,
			this.scale * MathUtils.random(0.99f, 1.01f) * 0.3f,
			this.scale * MathUtils.random(0.99f, 1.01f) * 2.0f * (this.duration + 0.8f), this.rotation);
		sb.setBlendFunction(770, 771);
	}

	@Override
	public void dispose() {

	}
}
