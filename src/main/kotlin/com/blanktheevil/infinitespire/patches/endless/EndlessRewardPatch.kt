package com.blanktheevil.infinitespire.patches.endless

import com.blanktheevil.infinitespire.acts.TheVoid
import com.blanktheevil.infinitespire.patches.utils.locators.PositionRewardsLocator
import com.blanktheevil.infinitespire.rewards.EndlessUnlockReward
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss
import com.megacrit.cardcrawl.screens.CombatRewardScreen

@Suppress("unused")
@SpirePatch(clz = CombatRewardScreen::class, method = "setupItemReward")
object EndlessRewardPatch {
  @JvmStatic
  @SpireInsertPatch(locator = PositionRewardsLocator::class)
  fun addEndlessReward(rewardScreen: CombatRewardScreen) {
    if(!Settings.isEndless && AbstractDungeon.id == TheVoid.ID && AbstractDungeon.currMapNode.room != null && AbstractDungeon.getCurrRoom() is MonsterRoomBoss) {
      rewardScreen.rewards.add(
        EndlessUnlockReward()
      )
    }
  }
}