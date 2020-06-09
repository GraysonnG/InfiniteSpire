package com.blanktheevil.infinitespire.patches.puzzlecube

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.extensions.inPuzzleCube
import com.blanktheevil.infinitespire.extensions.isBottled
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.relics.PuzzleCube
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame

@Suppress("unused")
@SpirePatch(clz = AbstractCard::class, method = "renderCard")
object RenderPuzzleCubeOnCardPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun renderPuzzleCubeOnCard(card: AbstractCard, sb: SpriteBatch, b1: Boolean, b2: Boolean) {
    if (CardCrawlGame.isInARun()) {
      player.relics.stream()
        .filter { it is PuzzleCube }
        .map { it as PuzzleCube }
        .forEach {
          if (card.inPuzzleCube) {
            it.makeCopy().also { preview ->
              preview.currentX = card.current_x
                .plus(390f.times(card.drawScale).div(3f).scale())
              preview.currentY =
                if (card.isBottled())
                  card.current_y.plus(350f.times(card.drawScale).div(3f).scale())
                else
                  card.current_y.plus(546f.times(card.drawScale).div(3f).scale())
              preview.scale = card.drawScale
              preview.renderOutline(sb, false)
              preview.render(sb)
            }

          }
        }
    }
  }
}