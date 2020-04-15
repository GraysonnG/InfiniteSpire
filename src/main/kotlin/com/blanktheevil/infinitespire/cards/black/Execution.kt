package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.powers.SuperSlowPower

class Execution : BlackCard(BUILDER) {
  companion object {
    val ID = "Execution".makeID()
    private const val UPG_COST = 1
    private val BUILDER = CardBuilder(ID)
      .img("execution.png")
      .cost(2)
      .skill()
      .enemy()
      .exhaust()
      .upgr {
        upgradeBaseCost(UPG_COST)
      }
      .use { player, monster ->
        monster?.applyPower(
          source = player!!,
          power = SuperSlowPower(monster, 0),
          amount = 0
        )
      }
  }
}