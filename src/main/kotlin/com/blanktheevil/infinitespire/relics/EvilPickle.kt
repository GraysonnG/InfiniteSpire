package com.blanktheevil.infinitespire.relics

import basemod.AutoAdd
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.rewards.InterestReward
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import kotlin.math.round

@AutoAdd.Ignore
class EvilPickle : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "EvilPickle".makeID()
    private const val IMG = "evilpickle"
    private val TIER = RelicTier.SPECIAL
    private val SOUND = LandingSound.FLAT
  }

  override fun onTrigger() {
    this.flash()
    AbstractDungeon.combatRewardScreen.rewards.add(
      InterestReward(
        round(player.gold.times(0.065f)).toInt()
      )
    )
  }
}