package infinitespire.abstracts;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import infinitespire.InfiniteSpire;
import infinitespire.effects.BlackCardEffect;
import infinitespire.patches.CardColorEnumPatch;
import infinitespire.util.TextureLoader;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class BlackCard extends Card {

	public static final CardRarity RARITY = CardRarity.SPECIAL;
	public static final CardColor COLOR = CardColorEnumPatch.CardColorPatch.INFINITE_BLACK;
	public static final Color TITLE_COLOR = new Color(1f, 0.15f, 0.15f, 1f);
	private static final float FPS_SCALE = (240f / Settings.MAX_FPS);
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

		for(BlackParticle p : particles){
			p.update();
		}
		particles.removeIf(BlackParticle::isDead);

		if(this.particles.size() < 150 && InfiniteSpire.shouldDoParticles && !Settings.DISABLE_EFFECTS){
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
		for(BlackParticle p : particles){
			p.render(sb);
		}
		super.render(sb);
	}

	private static class BlackParticle {
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
			}

			if(Math.random() < 0.05 && upgraded){
				color = Colors.get(InfiniteSpire.GDX_INFINITE_RED_NAME).cpy();
			}
		}

		public void update() {
			this.lifeSpan -= Gdx.graphics.getDeltaTime();
			this.pos.x += this.vel.x;
			this.pos.y += this.vel.y;
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

	@SpirePatch(clz = AbstractCard.class, method = "renderTitle")
	public static class RenderTitle {
		//Inserted after: font.getData().setScale(this.drawScale);
		@SpireInsertPatch(rloc = 64, localvars = {"font", "renderColor"})
		public static SpireReturn<?> blackCardTitleColorAdjust(AbstractCard __instance, SpriteBatch sb, BitmapFont font, Color renderColor) {
			if(__instance instanceof BlackCard) {
				Color color = Settings.CREAM_COLOR.cpy();
				if(__instance.upgraded) {
					color = TITLE_COLOR.cpy();
				}
				color.a = renderColor.a;
				FontHelper.renderRotatedText(sb, font, __instance.name, __instance.current_x, __instance.current_y,
						0f, 175f * __instance.drawScale * Settings.scale, __instance.angle, false, color);
				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException  {
				Matcher matcher = new Matcher.MethodCallMatcher(
					"com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData","setScale"
				);

				int[] lines = LineFinder.findInOrder(ctMethodToPatch, matcher);

				return new int[] {lines[lines.length - 1]};
			}
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderTitle")
	public static class RenderSingleCardPopupTitle {
		@SpirePrefixPatch
		public static SpireReturn<?> blackCardTitleColorAdjust(SingleCardViewPopup __instance, SpriteBatch sb) {
			AbstractCard card = (AbstractCard) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "card");
			if(card instanceof BlackCard) {
				if(card.isLocked) {
					FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, SingleCardViewPopup.TEXT[4], Settings.WIDTH / 2f, Settings.HEIGHT / 2.0f + 338.0f * Settings.scale, Settings.CREAM_COLOR);
				}else if(card.isSeen) {
					if(card.upgraded) {
						FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small,
								card.name, Settings.WIDTH / 2f, Settings.HEIGHT / 2.0f + 338.0f * Settings.scale, TITLE_COLOR);
					} else {
						FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small,
								card.name, Settings.WIDTH / 2f, Settings.HEIGHT / 2.0f + 338.0f * Settings.scale, Settings.CREAM_COLOR);
					}
				}else {
					FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small,
							SingleCardViewPopup.TEXT[5], Settings.WIDTH / 2f, Settings.HEIGHT / 2.0f + 338.0f * Settings.scale, Settings.CREAM_COLOR);
				}
				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderPortraitFrame")
	public static class RenderCardFrame {
		@SpirePrefixPatch
		public static SpireReturn<Void> blackCardFrameRender(AbstractCard __instance, SpriteBatch sb, float x, float y) {
			if(__instance instanceof BlackCard){
				try {
					Method renderHelperMethod = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, Texture.class, float.class, float.class);
					Color renderColor = (Color) ReflectionHacks.getPrivate(__instance, AbstractCard.class, "renderColor");
					renderHelperMethod.setAccessible(true);
					switch(__instance.type) {
						case ATTACK:
							renderHelperMethod.invoke(__instance, sb, renderColor, TextureLoader.getTexture("img/infinitespire/cards/ui/512/boss-frame-attack.png"), x, y);
							break;
						case CURSE:
						case STATUS:
						case SKILL:
							renderHelperMethod.invoke(__instance, sb, renderColor, TextureLoader.getTexture("img/infinitespire/cards/ui/512/boss-frame-skill.png"), x, y);
							break;
						case POWER:
							renderHelperMethod.invoke(__instance, sb, renderColor, TextureLoader.getTexture("img/infinitespire/cards/ui/512/boss-frame-power.png"), x, y);
							break;
					}
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				return SpireReturn.Return(null);
			}


			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame")
	public static class RenderSingleCardFrame {

		@SpireInsertPatch(
			locator = Locator.class,
			localvars = {"card"}
		)
		public static SpireReturn<Void> blackCardFrameRender(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card){
			if(card instanceof BlackCard) {
				Texture img = null;
				switch(card.type){
					case ATTACK:
						img = TextureLoader.getTexture("img/infinitespire/cards/ui/1024/boss-frame-attack.png");
						break;
					case POWER:
						img = TextureLoader.getTexture("img/infinitespire/cards/ui/1024/boss-frame-power.png");
						break;
					case SKILL:
						default:
							img = TextureLoader.getTexture("img/infinitespire/cards/ui/1024/boss-frame-skill.png");
							break;
				}


				if(img != null)
					sb.draw(img, Settings.WIDTH / 2.0F - 512.0F, Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}

		private static class Locator extends SpireInsertLocator{
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(
					"com.badlogic.gdx.graphics.g2d.SpriteBatch","draw");

				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderType")
	public static class RenderType {
		@SpireInsertPatch(locator = Locator.class,
		localvars = {"font", "text", "current_x", "current_y", "drawScale", "angle", "renderColor"})
		public static SpireReturn<Void> blackCardTypeColorAdjust(AbstractCard __instance, SpriteBatch sb, BitmapFont font, String text, float curX, float curY, float dScale, float angle, Color renderColor) {
			if(__instance instanceof BlackCard) {
				Color textColor = Color.valueOf("d0beff").cpy();
				textColor.a = renderColor.a;
				FontHelper.renderRotatedText(sb, font, text, curX, curY - 22.0f * dScale * Settings.scale, 0.0f, -1.0f * dScale * Settings.scale, angle, false, textColor);
				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(
					"com.megacrit.cardcrawl.helpers.FontHelper", "renderRotatedText"
				);

				return LineFinder.findInOrder(ctBehavior, matcher);
			}
		}

	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardTypeText")
	public static class RenderSingleCardType {

		@SpireInsertPatch(locator = Locator.class,
		localvars = {"card", "label"})
		public static SpireReturn<Void> blackCardTypeColorAdjust(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card, String label) {
			if(card instanceof BlackCard) {
				FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTypeFont, label, Settings.WIDTH / 2.0f + 3.0f * Settings.scale, Settings.HEIGHT / 2.0f - 40.0f * Settings.scale, Color.valueOf("d0beff"));
				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws Exception {
				Matcher matcher = new Matcher.MethodCallMatcher(
					"com.megacrit.cardcrawl.helpers.FontHelper","renderFontCentered"
				);

				return LineFinder.findInOrder(ctBehavior, matcher);
			}
		}
	}
}
