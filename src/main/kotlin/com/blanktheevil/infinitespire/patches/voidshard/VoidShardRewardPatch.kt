package com.blanktheevil.infinitespire.patches.voidshard

import com.blanktheevil.infinitespire.acts.TheVoid
import com.blanktheevil.infinitespire.models.VoidShardCurrency
import com.blanktheevil.infinitespire.patches.utils.locators.PositionRewardsLocator
import com.blanktheevil.infinitespire.rewards.VoidShardReward
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.screens.CombatRewardScreen

@Suppress("unused")
@SpirePatch(clz = CombatRewardScreen::class, method = "setupItemReward")
object VoidShardRewardPatch {
  @JvmStatic
  @SpireInsertPatch(locator = PositionRewardsLocator::class)
  fun addVoidShards(rewardScreen: CombatRewardScreen) {
    if (AbstractDungeon.miscRng.randomBoolean(VoidShardCurrency.DROP_RATE)) {
      rewardScreen.rewards.add(
        VoidShardReward(
          if (AbstractDungeon.id == TheVoid.ID) 5 else 1
        )
      )
    }
  }
}