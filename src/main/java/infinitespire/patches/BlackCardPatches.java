package infinitespire.patches;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.TitleFontSize;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.helpers.CardHelper;
import infinitespire.util.TextureLoader;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class BlackCardPatches {
	public static final Color TITLE_COLOR = new Color(1f, 0.15f, 0.15f, 1f);

	//Title Patches
	@SpirePatch(clz = AbstractCard.class, method = "renderTitle")
	public static class RenderTitle {
		//Inserted after: font.getData().setScale(this.drawScale);
		@SpireInsertPatch(locator=Locator.class, localvars = {"font", "renderColor"})
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
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher matcher = new Matcher.MethodCallMatcher(
						BitmapFont.BitmapFontData.class,"setScale"
				);

				int[] lines = LineFinder.findAllInOrder(ctMethodToPatch, matcher);

				return new int[] {lines[lines.length - 1] + 1};
			}
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderTitle")
	public static class RenderSingleCardPopupTitle {
		@SpirePrefixPatch
		public static SpireReturn<Void> blackCardTitleColorAdjust(SingleCardViewPopup __instance, SpriteBatch sb) {
			AbstractCard card = (AbstractCard) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "card");
			TitleFontSize.UseCustomFontSize.Insert(__instance, sb, card);
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
					Color renderColor = (Color) ReflectionHacks.getPrivate(__instance, AbstractCard.class, "renderColor");

					Method renderHelperMethod = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
					renderHelperMethod.setAccessible(true);

					switch(__instance.type) {
						case ATTACK:
							renderHelperMethod.invoke(
								__instance,
								sb,
								renderColor,
								TextureLoader.getTextureAsAtlasRegion("img/infinitespire/cards/ui/512/boss-frame-attack.png"),
								x,
								y);
							break;
						case CURSE:
						case STATUS:
						case SKILL:
							renderHelperMethod.invoke(
								__instance,
								sb,
								renderColor,
								TextureLoader.getTextureAsAtlasRegion("img/infinitespire/cards/ui/512/boss-frame-skill.png"),
								x,
								y);
							break;
						case POWER:
							renderHelperMethod.invoke(__instance,
								sb,
								renderColor,
								TextureLoader.getTextureAsAtlasRegion("img/infinitespire/cards/ui/512/boss-frame-power.png"),
								x,
								y);
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
			localvars = {"card", "tmpImg"}
		)
		public static void blackCardFrameRender(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card, @ByRef TextureAtlas.AtlasRegion[] region){
			if(card instanceof BlackCard) {
				TextureAtlas.AtlasRegion img = null;
				switch(card.type){
					case ATTACK:
						img = TextureLoader.getTextureAsAtlasRegion("img/infinitespire/cards/ui/1024/boss-frame-attack.png");
						break;
					case POWER:
						img = TextureLoader.getTextureAsAtlasRegion("img/infinitespire/cards/ui/1024/boss-frame-power.png");
						break;
					case SKILL:
					default:
						img = TextureLoader.getTextureAsAtlasRegion("img/infinitespire/cards/ui/1024/boss-frame-skill.png");
						break;
				}


				if(img != null) region[0] = img;
			}
		}

		private static class Locator extends SpireInsertLocator{
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(
					SingleCardViewPopup.class, "renderHelper");

				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			}
		}
	}

	//Label Patches
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

	//Purge color Patch
	@SpirePatch(clz = PurgeCardEffect.class, method = "initializeVfx")
	public static class PurgeVFXPatch {
		@SpirePostfixPatch
		public static void setBlackCardColor(PurgeCardEffect __instance){
			AbstractCard card = (AbstractCard) ReflectionHacks.getPrivate(__instance, PurgeCardEffect.class, "card");

			if(card instanceof BlackCard){
				Color rarityColor = Colors.get(InfiniteSpire.GDX_INFINITE_RED_NAME).cpy();
				Color color = Colors.get(InfiniteSpire.GDX_INFINITE_PURPLE_NAME).cpy();

				rarityColor.a = 0.1f;
				color.a = 0.1f;

				ReflectionHacks.setPrivate(__instance, PurgeCardEffect.class, "rarityColor", rarityColor);
				ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", color);
			}

		}
	}

	//Transform Patch
	@SpirePatch(clz = AbstractDungeon.class, method = "returnTrulyRandomCardFromAvailable", paramtypes = {"com.megacrit.cardcrawl.cards.AbstractCard", "com.megacrit.cardcrawl.random.Random"})
	public static class ReturnTrulyRandomCardFromAvailable {
		@SpirePrefixPatch
		public static SpireReturn<AbstractCard> InsertBlackCardTransform(AbstractCard prohibited, Random rng){
			ArrayList<AbstractCard> list = new ArrayList<>();

			if(prohibited instanceof BlackCard){
				for(BlackCard card : CardHelper.getBlackCards()){
					if(!Objects.equals(card.cardID, prohibited.cardID))
						list.add(card);
				}

				return SpireReturn.Return(list.get(rng.random(list.size() - 1)));
			}
			return SpireReturn.Continue();
		}
	}
}
