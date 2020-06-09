package com.blanktheevil.infinitespire.quests.utils

import basemod.AutoAdd
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.blanktheevil.infinitespire.patches.utils.filters.NotPackageFilter
import com.blanktheevil.infinitespire.quests.IgnoreRelicQuest
import com.blanktheevil.infinitespire.quests.Quest
import com.blanktheevil.infinitespire.quests.questrewards.QuestReward

@Suppress("unused")
@AutoAdd.Ignore
object QuestManager {
  private val quests = mutableListOf<Quest>()

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

    quests.add(IgnoreRelicQuest())
  }

  fun getRandomQuest(): Quest =
    (quests.getRandomItem(InfiniteSpire.questRng) ?: TestQuest()).makeCopy()

  fun getQuestByID(questId: String) : Quest =
    quests.first { it.questId == questId }.makeCopy()
}