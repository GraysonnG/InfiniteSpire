package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.actions.DrawCardAndUpgrade
import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.vfx.FinalStrikeVfx
import com.blanktheevil.infinitespire.vfx.UltimateFormVfx
import com.megacrit.cardcrawl.actions.animations.VFXAction

class Collect : BlackCard(BUILDER) {
  companion object {
    val ID = "Collect".makeID()
    private const val MAGIC = 3
    private const val UPG_MAGIC = 1
    private val BUILDER = CardBuilder(ID)
      .img("collect.png")
      .cost(0)
      .skill()
      .self()
      .init {
        baseMagicNumber = MAGIC
        magicNumber = MAGIC
      }
      .upgr {
        upgradeMagicNumber(UPG_MAGIC)
      }
      .use { p, _ ->
        addToBot(DrawCardAndUpgrade(p!!, magicNumber))
      }
  }
}