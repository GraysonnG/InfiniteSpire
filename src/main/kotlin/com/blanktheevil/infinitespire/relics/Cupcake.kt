package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.powers.DeEnergizedPower

class Cupcake : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Cupcake".makeID()
    private const val IMG = "cupcake"
    private val TIER = RelicTier.BOSS
    private val SOUND = LandingSound.MAGICAL
  }

  private var tryToAddPower = false

  init {
    counter = 3
  }

  override fun onEquip() {
    player.energy.energyMaster++
  }

  override fun onUnequip() {
    player.energy.energyMaster--
  }

  override fun atBattleStart() {
    counter = 3
    tryToAddPower = true
  }

  override fun onVictory() {
    counter = 3
  }

  override fun onPlayerEndTurn() {
    if (counter > 0) counter--
    if (counter == 0) {
      if (!player.hasPower(DeEnergizedPower.powerID) && tryToAddPower) {
        player.applyPower(
          DeEnergizedPower(player, 1),
          amount = 1
        )
      }
      tryToAddPower = false
    }
  }
}