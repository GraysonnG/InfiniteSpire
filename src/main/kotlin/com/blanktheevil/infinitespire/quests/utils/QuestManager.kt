package com.blanktheevil.infinitespire.quests.utils

import basemod.AutoAdd
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.quests.questrewards.QuestReward

object QuestManager {
  fun addAllQuests() {
    AutoAdd(InfiniteSpire.modid)
      .packageFilter(QuestReward::class.java)
      .any(QuestReward::class.java) { _, rewardType ->
        with (rewardType) {
          InfiniteSpire.logger.info("Added Quest Reward Type: $id")
          QuestReward.questRewardTypes[id] = this
        }
      }
  }
}