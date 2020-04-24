package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.common.GainBlockAction

class Fortify : BlackCard(BUILDER) {
  companion object {
    val ID = "Fortify".makeID()
    private const val BLOCK = 3
    private const val MAGIC = 5
    private const val UPGR_MAGIC = 1
    private val BUILDER = CardBuilder(ID)
      .img("fortify.png")
      .cost(1)
      .skill()
      .self()
      .init {
        baseBlock = BLOCK
        baseMagicNumber = MAGIC
        magicNumber = MAGIC
      }
      .upgr {
        upgradeMagicNumber(UPGR_MAGIC)
      }
      .use { player, _ ->
        for (i in 0 until magicNumber) {
          addToBot(
            GainBlockAction(player, player, block, true)
          )
        }
      }
  }
}