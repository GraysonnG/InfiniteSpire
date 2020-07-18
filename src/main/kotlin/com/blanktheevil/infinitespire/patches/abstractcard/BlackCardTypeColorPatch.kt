package com.blanktheevil.infinitespire.patches.abstractcard

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.patches.utils.locators.RenderRotatedTextLocator
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.helpers.FontHelper

@Suppress("unused")
@SpirePatch(clz = AbstractCard::class, method = "renderType")
object BlackCardTypeColorPatch {
  @JvmStatic
  @SpireInsertPatch(
    locator = RenderRotatedTextLocator::class,
    localvars = [
    "font",
    "text",
    "current_x",
    "current_y",
    "drawScale",
    "angle",
    "renderColor"
    ]
  )
  fun adjustColor(card: AbstractCard, sb: SpriteBatch, font: BitmapFont, text: String, curX: Float, curY: Float, dScale: Float, angle: Float, renderColor: Color): SpireReturn<Void> {
        return if (card is BlackCard) {
          val textColor = Color.valueOf("d0beff").cpy()
          textColor.a = renderColor.a

          FontHelper.renderRotatedText(
            sb,
            font,
            text,
            curX,
            curY.minus(22f.times(dScale).scale()),
            0f,
            -1f.times(dScale).scale(),
            angle,
            false,
            textColor
          )

          SpireReturn.Return(null);
        } else SpireReturn.Continue();
  }
}