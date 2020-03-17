package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.actionManager
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.powers.JokerCardPower
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.monsters.AbstractMonster

class JokerCard : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Joker Card".makeID()
    private const val IMG = "jokercard"
    private val TIER = RelicTier.RARE
    private val SOUND = LandingSound.FLAT
    private const val COUNT = 15

  }

  override fun atBattleStart() {
    if (counter == COUNT - 1) {
      beginPulse()
      pulse = true
      player.hand.refreshHandLayout()
      player.applyPower(JokerCardPower(player))
    }
  }

  override fun onPlayCard(card: AbstractCard?, monster: AbstractMonster?) {
    if (player.hasPower(JokerCardPower.powerID)) return

    counter++

    if (counter == 15) {
      counter = 0
      flash()
      pulse = false
    }

    if (counter == 14) {
      beginPulse()
      pulse = true
      player.hand.refreshHandLayout()
      actionManager.addToBottom(RelicAboveCreatureAction(player, this))
      player.applyPower(JokerCardPower(player))
    }
  }
}