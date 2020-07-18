package com.blanktheevil.infinitespire.patches.abstractcard

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.textures.Textures
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.cards.AbstractCard
import java.lang.reflect.Method

@Suppress("unused")
@SpirePatch(clz = AbstractCard::class, method = "renderPortraitFrame")
object BlackCardRenderFramePatch {
  private val ATTACK_TEXTURE: TextureAtlas.AtlasRegion by lazy {
    Textures.cards.get("ui/512/boss-frame-attack.png").asAtlasRegion()
  }

  private val SKILL_TEXTURE: TextureAtlas.AtlasRegion by lazy {
    Textures.cards.get("ui/512/boss-frame-skill.png").asAtlasRegion()
  }

  private val POWER_TEXTURE: TextureAtlas.AtlasRegion by lazy {
    Textures.cards.get("ui/512/boss-frame-power.png").asAtlasRegion()
  }

  private val HELPER_METHOD: Method by lazy {
    AbstractCard::class.java.getDeclaredMethod("renderHelper", SpriteBatch::class.java, Color::class.java, TextureAtlas.AtlasRegion::class.java, Float::class.java, Float::class.java)
  }

  private var RENDER_COLOR: Color? = null

  @SpirePrefixPatch
  @JvmStatic
  fun renderFrame(card: AbstractCard, sb: SpriteBatch, x: Float, y: Float): SpireReturn<Void> {
    return if (card is BlackCard) {
      try {
        HELPER_METHOD.isAccessible = true

        val texture = when (card.type) {
          AbstractCard.CardType.ATTACK -> ATTACK_TEXTURE
          AbstractCard.CardType.POWER -> POWER_TEXTURE
          else -> SKILL_TEXTURE
        }

        HELPER_METHOD.invoke(card, sb, getColor(card), texture, x, y)
      } catch (e: Exception) {
        e.printStackTrace()
      }
      SpireReturn.Return(null)
    } else SpireReturn.Continue()
  }

  private fun getColor(card: AbstractCard): Color {
    if (RENDER_COLOR == null) {
      RENDER_COLOR = ReflectionHacks.getPrivate(card, AbstractCard::class.java, "renderColor") as Color
    }
    return RENDER_COLOR!!
  }
}