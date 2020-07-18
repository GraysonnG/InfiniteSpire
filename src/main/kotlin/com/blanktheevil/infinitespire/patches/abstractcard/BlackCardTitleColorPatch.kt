package com.blanktheevil.infinitespire.patches.abstractcard

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.patches.utils.locators.SetScaleLocator
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.helpers.FontHelper

@Suppress("unused")
@SpirePatch(clz = AbstractCard::class, method = "renderTitle")
object BlackCardTitleColorPatch {
  private val TITLE_COLOR = Color(1f, 0.15f, 0.15f, 1f)

  @JvmStatic
  @SpireInsertPatch(locator = SetScaleLocator::class, localvars = ["font", "renderColor"])
  fun adjustColor(card: AbstractCard, sb: SpriteBatch, font: BitmapFont, renderColor: Color): SpireReturn<Void> {
    return if (card is BlackCard && card.upgraded) {
      val color = TITLE_COLOR
      color.a = renderColor.a
      FontHelper.renderRotatedText(
        sb,
        font,
        card.name,
        card.current_x,
        card.current_y,
        0f,
        175f.times(card.drawScale).scale(),
        card.angle,
        false,
        color
      )
      SpireReturn.Return(null)
    } else SpireReturn.Continue()
  }
}