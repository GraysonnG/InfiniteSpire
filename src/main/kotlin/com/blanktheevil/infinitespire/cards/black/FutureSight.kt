package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.common.GainEnergyAction

class FutureSight : BlackCard(BUILDER) {
  companion object {
    val ID = "FutureSight".makeID()
    private const val MAGIC = 4
    private val BUILDER = CardBuilder(id = ID)
      .img("futuresight.png")
      .black()
      .skill()
      .self()
      .cost(1)
      .use { _, _ ->
        addToBot(GainEnergyAction(magicNumber))
      }
      .init {
        this.baseMagicNumber = MAGIC
        this.magicNumber = MAGIC
        this.exhaust = true
      }
      .upgr {
        upgradeBaseCost(0)
      }
  }
}