package infinitespire.abstracts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import basemod.ReflectionHacks;
import infinitespire.patches.CardColorEnumPatch;

public abstract class BlackCard extends Card {

	public static final CardRarity RARITY = CardRarity.SPECIAL;
	public static final CardColor COLOR = CardColorEnumPatch.CardColorPatch.INFINITE_BLACK;
	public static final Color TITLE_COLOR = new Color(1f, 0.15f, 0.15f, 1f);
	
	public BlackCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardTarget target) {
		super(id, name, img, cost, rawDescription, type, COLOR, RARITY, target);
		this.setOrbTexture("img/infinitespire/cards/ui/512/boss-orb.png", "img/infinitespire/cards/ui/1024/boss-orb.png");
		this.setBannerTexture("img/infinitespire/cards/ui/512/boss-banner.png", "img/infinitespire/cards/ui/1024/boss-banner.png");
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "renderTitle")
	public static class renderTitlePatch{
		@SpireInsertPatch(rloc=64, localvars = {"font", "renderColor"})
		public static SpireReturn<?> Insert(AbstractCard __instance, SpriteBatch sb, BitmapFont font, Color renderColor) {
			if(__instance instanceof BlackCard) {
				Color color = Settings.CREAM_COLOR.cpy();
				if(__instance.upgraded) {
					color = TITLE_COLOR.cpy();
				}
				color.a = renderColor.a;
				FontHelper.renderRotatedText(sb, font, __instance.name, __instance.current_x, __instance.current_y, 0f, 175f * __instance.drawScale * Settings.scale, __instance.angle, false, color);
				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(cls = "com.megacrit.cardcrawl.screens.SingleCardViewPopup", method = "renderTitle")
	public static class renderSingleCardPopupTitle {
		public static SpireReturn<?> Prefix(SingleCardViewPopup __instance, SpriteBatch sb) {
			AbstractCard card = (AbstractCard) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "card");
			if(card instanceof BlackCard) {
				if(card.isLocked) {
					FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, SingleCardViewPopup.TEXT[4], Settings.WIDTH / 2f, Settings.HEIGHT / 2.0f + 338.0f * Settings.scale, Settings.CREAM_COLOR);
				}else if(card.isSeen) {
					if(card.upgraded) {
						FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, card.name, Settings.WIDTH / 2f, Settings.HEIGHT / 2.0f + 338.0f * Settings.scale, TITLE_COLOR);
					} else {
						FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, card.name, Settings.WIDTH / 2f, Settings.HEIGHT / 2.0f + 338.0f * Settings.scale, Settings.CREAM_COLOR);
					}
				}else {
					FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, SingleCardViewPopup.TEXT[5], Settings.WIDTH / 2f, Settings.HEIGHT / 2.0f + 338.0f * Settings.scale, Settings.CREAM_COLOR);
				}
				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}
	}
}
