package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.stances.UltimateStance
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction

class UltimateForm : BlackCard(BUILDER) {
  companion object {
    val ID = "UltimateForm".makeID()
    private const val UPGR_COST = 4
    private val BUILDER = CardBuilder(ID)
      .img("ultimateform.png")
      .cost(5)
      .power()
      .self()
      .upgr {
        upgradeBaseCost(UPGR_COST)
      }
      .use { _, _ ->
        addToBot(
          ChangeStanceAction(
            UltimateStance()
          )
        )
      }
  }
}