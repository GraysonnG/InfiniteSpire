package com.blanktheevil.infinitespire.patches.evilpickle

import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.patches.utils.locators.PositionRewardsLocator
import com.blanktheevil.infinitespire.relics.EvilPickle
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.screens.CombatRewardScreen

@Suppress("unused")
@SpirePatch(clz = CombatRewardScreen::class, method = "setupItemReward")
object EvilPickleRewardPatch {
  @JvmStatic
  @SpireInsertPatch(locator = PositionRewardsLocator::class)
  fun addInterestReward(rewardScreen: CombatRewardScreen) {
    if (player.hasRelic(EvilPickle.ID)) {
      player.getRelic(EvilPickle.ID).onTrigger()
    }
  }
}