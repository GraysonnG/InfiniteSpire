package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.powers.DrawPower

class NeuralNetwork : BlackCard(BUILDER) {
  companion object {
    val ID = "NeuralNetwork".makeID()
    private const val MAGIC = 2
    private val BUILDER = CardBuilder(ID)
      .img("neuralnetwork.png")
      .cost(1)
      .power()
      .self()
      .init {
        baseMagicNumber = MAGIC
        magicNumber = MAGIC
      }
      .upgr {
        isInnate = true
        rawDescription = strings(ID).UPGRADE_DESCRIPTION
        initializeDescription()
      }
      .use { player, _ ->
        player?.applyPower(
          DrawPower(player, magicNumber)
        )
      }
  }
}