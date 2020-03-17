package com.blanktheevil.infinitespire.patches.abstractcard

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.relics.BottleRelic
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.cards.AbstractCard


@Suppress("unused")
@SpirePatch(clz = AbstractCard::class, method = "renderCard")
class RenderBottleRelicOnCardPatch {
  companion object {
    @JvmStatic
    @SpirePostfixPatch
    fun renderBottleOnCard(card: AbstractCard, sb: SpriteBatch, b1: Boolean, b2: Boolean) {
      player.relics.stream()
        .filter { it is BottleRelic }
        .map { it as BottleRelic }
        .forEach {
          if (it.isCardBottled(card)) {
            val previewRelic = it.makeCopy()
            previewRelic.currentX = card.current_x
              .plus(390f.times(card.drawScale).div(3f).scale())
            previewRelic.currentY = card.current_y
              .plus(546f.times(card.drawScale).div(3f).scale())
            previewRelic.scale = card.drawScale
            previewRelic.renderOutline(sb, false)
            previewRelic.render(sb)
          }
        }
    }
  }
}