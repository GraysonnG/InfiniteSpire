package infinitespire.abstracts;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import infinitespire.effects.BlackCardEffect;
import infinitespire.patches.CardColorEnumPatch;
import infinitespire.util.TextureLoader;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BlackCard extends Card {

	public static final CardRarity RARITY = CardRarity.SPECIAL;
	public static final CardColor COLOR = CardColorEnumPatch.CardColorPatch.INFINITE_BLACK;
	public static final Color TITLE_COLOR = new Color(1f, 0.15f, 0.15f, 1f);
	
	public BlackCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardTarget target) {
		super(id, name, img, cost, rawDescription, type, COLOR, RARITY, target);
		this.setOrbTexture("img/infinitespire/cards/ui/512/boss-orb.png", "img/infinitespire/cards/ui/1024/boss-orb.png");
		this.setBannerTexture("img/infinitespire/cards/ui/512/boss-banner.png", "img/infinitespire/cards/ui/1024/boss-banner.png");
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
