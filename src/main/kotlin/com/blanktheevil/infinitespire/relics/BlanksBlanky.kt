package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player

class BlanksBlanky : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Blanks Blanky".makeID()
    private val IMG = "blanksblanky"
    private val TIER = RelicTier.SPECIAL
    private val SOUND = LandingSound.MAGICAL
  }

  override fun onRest() {
    player.currentHealth = player.maxHealth
  }
}