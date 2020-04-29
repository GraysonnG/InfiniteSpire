package com.blanktheevil.infinitespire.rewards.utils

import basemod.BaseMod
import basemod.abstracts.CustomReward
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.blanktheevil.infinitespire.rewards.EndlessUnlockReward
import com.blanktheevil.infinitespire.rewards.InterestReward
import com.blanktheevil.infinitespire.rewards.VoidShardReward
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.rewards.RewardSave

object RewardManager {
  fun registerRewards() {
    registerReward(
      EnumPatches.RewardType.VOID_SHARD,
      { VoidShardReward(it.amount) },
      {
        RewardSave(
          it.type.toString(),
          null,
          (it as VoidShardReward).amountOfShards,
          0
        )
      }
    )

    registerReward(
      EnumPatches.RewardType.ENDLESS,
      { EndlessUnlockReward() },
      { RewardSave(it.type.toString(), null) }
    )

    registerReward(
      EnumPatches.RewardType.INTEREST,
      { InterestReward(it.amount) },
      {
        RewardSave(
          it.type.toString(),
          null,
          (it as InterestReward).amount,
          0
        )
      }
    )
  }

  private fun registerReward(
    type: RewardItem.RewardType,
    load: (save: RewardSave) -> CustomReward,
    save: (reward: CustomReward) -> RewardSave
  ) {
    BaseMod.registerCustomReward(
      type,
      BaseMod.LoadCustomReward {
        return@LoadCustomReward load.invoke(it)
      },
      BaseMod.SaveCustomReward {
        return@SaveCustomReward save.invoke(it)
      }
    )
  }
}