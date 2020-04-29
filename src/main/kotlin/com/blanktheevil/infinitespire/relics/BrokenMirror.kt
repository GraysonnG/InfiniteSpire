package com.blanktheevil.infinitespire.relics

import basemod.AutoAdd
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.megacrit.cardcrawl.powers.ReflectionPower

@AutoAdd.Ignore
class BrokenMirror : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Broken Mirror".makeID()
    private const val IMG = "brokenmirror"
    private val TIER = RelicTier.UNCOMMON
    private val SOUND = LandingSound.CLINK
  }

  override fun atBattleStart() {
    player.applyPower(
      ReflectionPower(player, 3)
    )
  }
}