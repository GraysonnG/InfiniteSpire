package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.extensions.applyHeal
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.utils.CardBuilder

@Suppress("unused")
class Starlight : BlackCard(BUILDER) {
  companion object {
    val ID = "Starlight".makeID()
    private const val MAGIC = 10
    private const val UPG_MAGIC = 5
    private val BUILDER = CardBuilder(id = ID)
      .img("starlight.png")
      .cost(1)
      .skill()
      .self()
      .use { p,_ ->
        p?.applyHeal(magicNumber)
      }
      .init {
        this.baseMagicNumber = MAGIC
        this.magicNumber = MAGIC
        this.exhaust = true
      }
      .upgr {
        upgradeMagicNumber(UPG_MAGIC)
      }
  }
}