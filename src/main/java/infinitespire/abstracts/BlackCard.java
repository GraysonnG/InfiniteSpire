package infinitespire.abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import infinitespire.InfiniteSpire;
import infinitespire.effects.BlackCardEffect;
import infinitespire.patches.CardColorEnumPatch;

import java.util.ArrayList;
import java.util.Date;

public abstract class BlackCard extends Card {

	public static final CardRarity RARITY = CardRarity.SPECIAL;
	public static final CardColor COLOR = CardColorEnumPatch.CardColorPatch.INFINITE_BLACK;
	private static final float FPS_SCALE = (240f / Settings.MAX_FPS);
	private static final int MAX_PARTICLES = 150;

	public ArrayList<BlackParticle> particles;
	
	public BlackCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardTarget target) {
		super(id, name, img, cost, rawDescription, type, COLOR, RARITY, target);
		this.setOrbTexture("img/infinitespire/cards/ui/512/boss-orb.png", "img/infinitespire/cards/ui/1024/boss-orb.png");
		this.setBannerTexture("img/infinitespire/cards/ui/512/boss-banner.png", "img/infinitespire/cards/ui/1024/boss-banner.png");
		this.particles = new ArrayList<>();
	}

	public void useWithEffect(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

	}

	//Use useWithEffect instead unless you have made your own VFX for your black card!
	@Deprecated
	@Override
	public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
		AbstractDungeon.actionManager.addToBottom(new VFXAction(new BlackCardEffect(), 0.15f));
		useWithEffect(abstractPlayer, abstractMonster);
	}

	@Override
	public void update(){
		super.update();

		particles.parallelStream()
			.forEach(BlackParticle::update);
		particles.removeIf(BlackParticle::isDead);

		if(this.particles.size() < MAX_PARTICLES && InfiniteSpire.shouldDoParticles && !Settings.DISABLE_EFFECTS){
			for(int i = 0; i < 2 * FPS_SCALE; i++){
				Vector2 point = generateRandomPointAlongEdgeOfHitbox();
				particles.add(new BlackParticle(point.x, point.y, this.drawScale, this.upgraded));
			}
		}
	}

	private Vector2 generateRandomPointAlongEdgeOfHitbox() {
		Vector2 result = new Vector2();
		Random random = new Random();
		boolean topOrBottom = random.randomBoolean();
		boolean leftOrRight = random.randomBoolean();
		boolean tbOrLr = random.randomBoolean();

		if(tbOrLr){
			result.x = random.random(this.hb.cX - (this.hb.width / 2f), this.hb.cX + (this.hb.width / 2f));
			result.y = topOrBottom ? this.hb.cY + (this.hb.height / 2f) : this.hb.cY - (this.hb.height / 2f);
		} else {
			result.x = leftOrRight ? this.hb.cX + (this.hb.width / 2f) : this.hb.cX - (this.hb.width / 2f);
			result.y = random.random(this.hb.cY - (this.hb.height / 2f), this.hb.cY + (this.hb.height / 2f));
		}

		return result;
	}

	@Override
	public void render(SpriteBatch sb){
		sb.setColor(Color.WHITE.cpy());
		particles.stream()
			.forEach(blackParticle -> blackParticle.render(sb));
		super.render(sb);
	}

	public static class BlackParticle {
		private Vector2 pos;
		private Vector2 vel;
		private float lifeSpan;
		private Color color;
		private float drawScale;
		private boolean upgraded;

		private static TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("img/infinitespire/vfx/particle.atlas"));

		public BlackParticle(float x, float y, float drawScale, boolean upgraded) {
			pos = new Vector2(x, y);
			this.drawScale = drawScale;
			this.upgraded = upgraded;

			float speedScale = MathUtils.clamp(
				Gdx.graphics.getDeltaTime() * 240f,
				FPS_SCALE - 0.2f,
				FPS_SCALE + 0.2f);
			float maxV = 2.0f * drawScale;
			maxV = MathUtils.clamp(maxV, 0.01f, FPS_SCALE * 2f);

			float velX = MathUtils.random(-maxV * speedScale / 2f, maxV * speedScale / 2f);
			float velY = MathUtils.random(0.01f, maxV * speedScale);

			vel = new Vector2(velX, velY);

			lifeSpan = MathUtils.random(0.1f, 0.5f);

			color = Color.BLACK.cpy();

			if(Math.random() < 0.25) {
				color = Colors.get(InfiniteSpire.GDX_INFINITE_PURPLE_NAME).cpy();
				makeColorEasterEgg();
			}

			if(Math.random() < 0.05 && upgraded){
				color = Colors.get(InfiniteSpire.GDX_INFINITE_RED_NAME).cpy();
				makeColorEasterEgg();
			}


		}

		public void update() {
			this.lifeSpan -= Gdx.graphics.getDeltaTime();
			this.pos.x += this.vel.x;
			this.pos.y += this.vel.y;
		}

		public void makeColorEasterEgg() {
			Date date = new Date();
			if(date.getMonth() == 5) {
				int i = MathUtils.random(5);
				switch (i) {
					case 0:
						color = Color.RED.cpy();
						break;
					case 1:
						color = Color.ORANGE.cpy();
						break;
					case 2:
						color = Color.YELLOW.cpy();
						break;
					case 3:
						color = Color.GREEN.cpy();
						break;
					case 4:
						color = Color.BLUE.cpy();
						break;
					case 5:
						color = Color.PURPLE.cpy();
						break;
				}
			}
		}

		public void render(SpriteBatch sb) {
			sb.setColor(color);
			sb.draw(textureAtlas.findRegion("cardParticle"),
				pos.x - 40f,
				pos.y - 40f,
				40f,
				40f,
				80f,
				80f,
				drawScale * (lifeSpan / 0.25f),
				drawScale * (lifeSpan / 0.25f),
				0f);
		}

		public boolean isDead() {
			return lifeSpan <= 0f;
		}
	}
}
