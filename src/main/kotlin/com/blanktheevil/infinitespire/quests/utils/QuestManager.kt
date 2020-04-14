package com.blanktheevil.infinitespire.quests.utils

import basemod.AutoAdd
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.patches.utils.filters.NotPackageFilter
import com.blanktheevil.infinitespire.quests.questrewards.QuestReward

@AutoAdd.Ignore
object QuestManager {
  fun addAllQuests() {
    AutoAdd(InfiniteSpire.modid)
      .packageFilter(QuestReward::class.java)
      .filter(NotPackageFilter(QuestManager::class.java))
      .any(QuestReward::class.java) { _, rewardType ->
        with(rewardType) {
          InfiniteSpire.logger.info("Added Quest Reward Type: $id")
          QuestReward.questRewardTypes[id] = this
        }
      }
  }
}