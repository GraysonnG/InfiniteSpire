package com.blanktheevil.infinitespire.rewards

import basemod.abstracts.CustomReward
import com.blanktheevil.infinitespire.extensions.addVoidShard
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.blanktheevil.infinitespire.textures.Textures

class VoidShardReward(
  val amountOfShards: Int = 1
) : CustomReward(ICON, getDesc(amountOfShards), EnumPatches.RewardType.VOID_SHARD) {
  companion object {
    private val ICON = Textures.ui.get("topPanel/avhari/voidShard.png")
    private val strings = languagePack.getUIString("VoidShard")

    private fun getDesc(value: Int): String {
      return if (value > 1)
        "$value${strings.TEXT[4]}"
      else
        "$value${strings.TEXT[3]}"
    }
  }

  override fun claimReward(): Boolean {
    addVoidShard(amountOfShards)
    return true
  }
}