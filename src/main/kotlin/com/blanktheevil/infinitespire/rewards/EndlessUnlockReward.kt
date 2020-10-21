package com.blanktheevil.infinitespire.rewards

import basemod.abstracts.CustomReward
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

class EndlessUnlockReward : CustomReward(ICON, strings.TEXT[0], EnumPatches.RewardType.ENDLESS) {
  companion object {
    private val strings = languagePack.getUIString("EndlessReward".makeID())
    private val ICON = Textures.ui.get("topPanel/endless.png")
  }

  override fun claimReward(): Boolean {
    Settings.isEndless = true
    AbstractDungeon.topPanel.setPlayerName()
    CardCrawlGame.sound.play("UNLOCK_PING")
    return true
  }
}