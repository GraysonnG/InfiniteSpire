package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower
import com.megacrit.cardcrawl.powers.LoseStrengthPower
import com.megacrit.cardcrawl.powers.StrengthPower

class TheBestDefense : BlackCard(BUILDER) {
  companion object {
    val ID = "TheBestDefense".makeID()
    private val BUILDER = CardBuilder(ID)
      .img("bestdefense.png")
      .cost(1)
      .skill()
      .self()
      .upgr {
        upgradeBaseCost(0)
      }
      .use { player, _ ->
        val amountOfBlock = player?.currentBlock ?: 0
        player?.applyPower(IntangiblePlayerPower(player, 1))
        addToBot(RemoveAllBlockAction(player, player))
        player?.applyPower(StrengthPower(player, amountOfBlock))
        player?.applyPower(LoseStrengthPower(player, amountOfBlock))
      }
  }
}