package infinitespire.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.HealVerticalLineEffect;
import infinitespire.InfiniteSpire;
import infinitespire.effects.simpleVFX.YellowHeartParticleEffect;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class TempHPIntentPatch {

	@SpirePatch(clz = AbstractMonster.class, method = "getIntentImg")
	public static class GetIntentImg {
		@SpirePrefixPatch
		public static SpireReturn<Texture> getCustomIntentImg(AbstractMonster __instance) {
			if(__instance.intent == IntentEnumPatch.INFINITE_TEMPHP) {
				return SpireReturn.Return(InfiniteSpire.Textures.getUITexture("intents/tempHp_L.png"));
			} else if(__instance.intent == IntentEnumPatch.ATTACK_INFINITE_TEMPHP) {
				Texture attackTexture = null;

				try {
					Method method = AbstractMonster.class.getDeclaredMethod("getAttackIntent");
					method.setAccessible(true);
					attackTexture = (Texture) method.invoke(__instance);
				} catch (Exception e) {
					e.printStackTrace();
					return SpireReturn.Continue();
				}

				return SpireReturn.Return(attackTexture);
			} else {
				return SpireReturn.Continue();
			}
		}
	}

	// this is actually coded like garbage
	@SpirePatch(clz = AbstractMonster.class, method = "updateIntentTip")
	public static class UpdateIntentTip {
		@SpirePrefixPatch
		public static SpireReturn<Void> getCustomIntentTip(AbstractMonster __instance) {
			UIStrings strings = getStringsForTip(__instance.intent);
			if(strings != null) {
				PowerTip monsterTips = (PowerTip)ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "intentTip");

				monsterTips.header = strings.TEXT[0];
				monsterTips.body = strings.TEXT[1] + (
					__instance.intent == IntentEnumPatch.ATTACK_INFINITE_TEMPHP ?
						(int) ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "intentDmg") + " damage." :
						"");
				monsterTips.img = InfiniteSpire.Textures.getUITexture(strings.TEXT[2]);

				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}
	}


	private static final float X_JITTER = 64f * Settings.scale;
	private static final float Y_JITTER = 64f * Settings.scale;

	//Lordy that's alot of reflection lmfao
	@SpirePatch(clz = AbstractMonster.class, method = "updateIntentVFX")
	public static class UpdateIntentVFX {
		@SuppressWarnings("unchecked")
		@SpirePrefixPatch
		public static SpireReturn<Void> updateVFX(AbstractMonster __instance) {
			if(__instance.intent == IntentEnumPatch.ATTACK_INFINITE_TEMPHP || __instance.intent == IntentEnumPatch.INFINITE_TEMPHP) {
				ArrayList<AbstractGameEffect> vfx = (ArrayList<AbstractGameEffect>) ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "intentVfx");
				float intentParticleTimer = (float) ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "intentParticleTimer");
				ReflectionHacks.setPrivate(__instance, AbstractMonster.class, "intentParticleTimer", intentParticleTimer - Gdx.graphics.getDeltaTime());
				if (intentParticleTimer < 0.0F) {
					ReflectionHacks.setPrivate(__instance, AbstractMonster.class, "intentParticleTimer", 0.5f);
					vfx.add(new YellowHeartParticleEffect(__instance.intentHb.cX, __instance.intentHb.cY));
					for(int i = 0; i < 3; i++) {
						vfx.add(new HealVerticalLineEffect(
							__instance.intentHb.cX + MathUtils.random(-X_JITTER, X_JITTER),
							__instance.intentHb.cY + MathUtils.random(-Y_JITTER, Y_JITTER * 1.25f)));

					}
				}

				return SpireReturn.Return(null);
			}


			return SpireReturn.Continue();
		}
	}

	//haha what am i doing this is so stupid
	private static UIStrings getStringsForTip(AbstractMonster.Intent intent) {
		if(intent == IntentEnumPatch.INFINITE_TEMPHP) return CardCrawlGame.languagePack.getUIString(InfiniteSpire.createID("TempHPIntent"));
		if(intent == IntentEnumPatch.ATTACK_INFINITE_TEMPHP) return CardCrawlGame.languagePack.getUIString(InfiniteSpire.createID("AttackTempHPIntent"));
		return null;
	}
}
