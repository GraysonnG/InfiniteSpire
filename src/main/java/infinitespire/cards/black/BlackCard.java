package infinitespire.cards.black;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import basemod.ReflectionHacks;
import infinitespire.cards.Card;
import infinitespire.cards.Neurotoxin;
import infinitespire.patches.CardColorEnumPatch;

public abstract class BlackCard extends Card {

	public static final CardRarity RARITY = CardRarity.SPECIAL;
	public static final CardColor COLOR = CardColorEnumPatch.CardColorPatch.INFINITE_BLACK;
	public static final Color TITLE_COLOR = new Color(128f / 255f, 80f / 255f, 1f, 1f);
	public static final Color TITLE_UPGRADE_COLOR = new Color(1f, 0.15f, 0.15f, 1f);
	
	public BlackCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardTarget target) {
		super(id, name, img, cost, rawDescription, type, COLOR, RARITY, target);
		setBackgroundByType();
		this.setBannerTexture("img/cards/ui/512/boss-banner.png", "img/cards/ui/1024/boss-banner.png");
		this.setOrbTexture("img/cards/ui/512/boss-orb.png", "img/cards/ui/1024/boss-orb.png");
	}

	private void setBackgroundByType() {
		switch(this.type) {
		case ATTACK:
		case POWER:
		case SKILL:
			this.setBackgroundTexture("img/cards/ui/512/boss-" + this.type.toString().toLowerCase() + ".png", "img/cards/ui/1024/boss-" + this.type.toString().toLowerCase() + ".png");
			break;
		default:
			this.setBackgroundTexture("img/cards/ui/512/boss-skill.png", "img/cards/ui/1024/boss-skill.png");
			break;
		}
	}
	
	
	@Override
	protected void upgradeName() {
		++this.timesUpgraded;
		this.upgraded = true;
		this.name += "^2";
		this.initializeTitle();
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "renderTitle")
	public static class renderTitlePatch{
		@SpireInsertPatch(rloc=64, localvars = {"font", "renderColor"})
		public static SpireReturn<?> Insert(AbstractCard __instance, SpriteBatch sb, BitmapFont font, Color renderColor) {
			if(__instance instanceof BlackCard || __instance.cardID.equals(Neurotoxin.ID)) {
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
			if(card instanceof BlackCard || card.cardID.equals(Neurotoxin.ID)) {
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
