package com.blanktheevil.infinitespire.patches.abstractcard

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.relics.abstracts.BottleRelic
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame


@Suppress("unused")
@SpirePatch(clz = AbstractCard::class, method = "renderCard")
object RenderBottleRelicOnCardPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun renderBottleOnCard(card: AbstractCard, sb: SpriteBatch, b1: Boolean, b2: Boolean) {
    if (CardCrawlGame.isInARun()) {
      player.relics.asSequence()
        .filter { it is BottleRelic }
        .map { it as BottleRelic }
        .forEach {
          if (it.isCardBottled(card)) {
            it.makeCopy().also { preview ->
              preview.currentX = card.current_x
                .plus(390f.times(card.drawScale).div(3f).scale())
              preview.currentY = card.current_y
                .plus(546f.times(card.drawScale).div(3f).scale())
              preview.scale = card.drawScale
              preview.renderOutline(sb, false)
              preview.render(sb)
            }
          }
        }
    }
  }
}