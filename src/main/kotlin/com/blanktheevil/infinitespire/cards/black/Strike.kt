package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.actions.BlackStrikeDamageAction
import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.makeID

class Strike : BlackCard(BUILDER) {

  companion object {
    private val ID = "Strike".makeID()

    private const val IMG = "spacetime.png"
    private const val DAMAGE = 20
    private const val UPGR_DAMAGE = 10

    private val BUILDER = CardBuilder(ID)
      .img(IMG)
      .cost(1)
      .attack()
      .enemy()
      .init {
        baseDamage = DAMAGE
      }
      .upgr {
        upgradeDamage(UPGR_DAMAGE)
      }
      .use { _, m ->
        addToBot(BlackStrikeDamageAction(m!!, damage))
      }
  }
}