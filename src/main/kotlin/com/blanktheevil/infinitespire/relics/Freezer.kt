package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.defect.ChannelAction
import com.megacrit.cardcrawl.orbs.Frost

class Freezer : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Freezer".makeID()
    private const val IMG = "freezer"
    private val TIER = RelicTier.COMMON
    private val SOUND = LandingSound.CLINK
  }

  override fun atBattleStart() {
    flash()
    addToBot(ChannelAction(Frost()))
  }
}