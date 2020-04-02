package com.blanktheevil.infinitespire.relics


import com.blanktheevil.infinitespire.extensions.actionManager
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.powers.BeetleShellPower
import com.blanktheevil.infinitespire.relics.abstracts.Relic
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard

class BeetleShell : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Beetle Shell".makeID()
    private const val IMG = "beetleshell"
    private val TIER = RelicTier.COMMON
    private val SOUND = LandingSound.SOLID
  }

  override fun onUseCard(card: AbstractCard, action: UseCardAction) {
    if (card.baseBlock > 0) {
      counter++
      if (counter == 10) {
        counter = 0
        flash()
        pulse = false
      }

      if (counter == 9) {
        beginPulse()
        pulse = true
        player.hand.refreshHandLayout()
        actionManager.addToBottom(
          RelicAboveCreatureAction(player, this)
        )
        player.applyPower(
          BeetleShellPower(player)
        )
      }
    }
  }

  override fun atBattleStart() {
    if (counter == 9) {
      beginPulse()
      pulse = true
      player.hand.refreshHandLayout()
      player.applyPower(
        BeetleShellPower(player)
      )
    }
  }
}