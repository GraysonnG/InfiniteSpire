package infinitespire.screens;

import basemod.BaseMod;
import basemod.interfaces.PostRenderSubscriber;
import basemod.interfaces.PreDungeonUpdateSubscriber;
import basemod.interfaces.PreRoomRenderSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import infinitespire.InfiniteSpire;
import infinitespire.effects.simpleVFX.ColoredFloatyEffect;
import infinitespire.util.TextureLoader;

import java.util.ArrayList;

public class LordBackgroundEffect implements PreDungeonUpdateSubscriber, PreRoomRenderSubscriber, PostRenderSubscriber {

	private boolean isActive = false;
	private boolean isStopping = false;
	private static final int NUM_OF_PARTICLES = 50;
	private ArrayList<ColoredFloatyEffect> particles = new ArrayList<>();;
	private Color lordBgBlack;
	private Color lordBgAlpha = Color.WHITE.cpy();
	private float duration;
	private float stopDuration;

	public LordBackgroundEffect() {
		BaseMod.subscribe(this);
	}

	public void beginEffect() {
		this.isActive = true;
		this.particles = new ArrayList<>();
		this.lordBgBlack = Color.BLACK.cpy();
		this.lordBgAlpha = Color.WHITE.cpy();
		this.duration = 1.5f;
	}

	public void stopEffect(){
		this.isStopping = true;
		this.stopDuration = 2f;
	}

	@Override
	public void receivePreDungeonUpdate() {
		update();
	}

	@Override
	public void receivePostRender(SpriteBatch sb) {
		if(!isActive) return;
		sb.setBlendFunction(770, 771);
		sb.setColor(lordBgBlack);
		sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0f, 0.0f, Settings.WIDTH, Settings.HEIGHT);
	}

	@Override
	public void receivePreRoomRender(SpriteBatch sb) {
		render(sb);
	}

	public void update() {
		if(isActive) updateWhileActive();
		updateParticles();
	}

	private void updateWhileActive(){
		float progress = Math.min(1f, duration / 1.5f);
		this.lordBgBlack.a = Interpolation.fade.apply(progress);

		if(this.duration > 0.0f){
			duration -= Gdx.graphics.getDeltaTime();
		}else{
			this.duration = 0.0f;
		}

		if(this.isStopping){
			if(this.stopDuration > 0.0f){
				this.stopDuration -= Gdx.graphics.getDeltaTime();
				this.lordBgAlpha.a = this.stopDuration / 2.0f;
			}else{
				this.stopDuration = 0.0f;
				this.isActive = false;
				this.isStopping = false;
				return;
			}
		}


		if(AbstractDungeon.getCurrRoom() == null || !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)){
			if(!isStopping){
				stopEffect();
			}
		}
	}

	private void updateParticles(){
		if(isActive && this.particles.size() < NUM_OF_PARTICLES) {
			if((new Random()).randomBoolean()) {
				this.particles.add(new ColoredFloatyEffect(Color.BLACK));
			} else {
				if((new Random()).randomBoolean(0.6f)) {
					this.particles.add(new ColoredFloatyEffect(Colors.get(InfiniteSpire.GDX_INFINITE_PURPLE_NAME)));
				}else{
					this.particles.add(new ColoredFloatyEffect(Colors.get(InfiniteSpire.GDX_INFINITE_RED_NAME)));
				}
			}
		}

		for(ColoredFloatyEffect particle : particles){
			particle.update();
		}

		particles.removeIf((particle) -> particle.isDone);
	}

	public void render(SpriteBatch sb) {
		if(isActive) renderBackground(sb);

		if(particles.size() > 0) {
			for(ColoredFloatyEffect particle : particles){
				particle.render(sb);
			}
		}

		if(isActive) renderForeground(sb);
	}

	public void renderBackground(SpriteBatch sb){
		sb.setColor(lordBgAlpha);
		sb.setBlendFunction(770, 771);

		sb.draw(TextureLoader.getTexture(
			"img/infinitespire/ui/lordofannihilation/background.png"),
			0f, 0f, 1920 * Settings.scale, 1080 * Settings.scale);
	}

	public void renderForeground(SpriteBatch sb){
		sb.setBlendFunction(770, 771);
		sb.setColor(Color.BLACK.cpy());
		sb.draw(ImageMaster.BORDER_GLOW_2,0f ,0f , 1920 * Settings.scale, 1080 * Settings.scale);
	}
}
