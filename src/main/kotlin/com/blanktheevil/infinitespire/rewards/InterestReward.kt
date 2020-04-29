package com.blanktheevil.infinitespire.rewards

import basemod.abstracts.CustomReward
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.helpers.ImageMaster

class InterestReward(val amount: Int) : CustomReward(ICON, getDesc(amount), EnumPatches.RewardType.INTEREST) {
  companion object {
    private val ICON by lazy {
      ImageMaster.UI_GOLD
    }
    private val strings = languagePack.getUIString("RewardItems".makeID())

    private fun getDesc(amount: Int) : String {
      return strings.TEXT[1] + amount + strings.TEXT[2]
    }
  }

  override fun claimReward(): Boolean {
    CardCrawlGame.sound.play("GOLD_GAIN")
    player.gainGold(amount)
    return true
  }
}