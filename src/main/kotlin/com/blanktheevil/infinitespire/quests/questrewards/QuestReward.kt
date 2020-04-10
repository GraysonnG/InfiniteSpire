package com.blanktheevil.infinitespire.quests.questrewards

import com.blanktheevil.infinitespire.extensions.log

abstract class QuestReward(val id: String) {
  companion object {
    val questRewardTypes = mutableMapOf<String, QuestReward>()

    fun getQuestRewardById(id: String): QuestReward {
      return when {
        questRewardTypes.containsKey(id) -> questRewardTypes[id]!!.makeCopy()
        else -> {
          log.error("Could not find quest reward by ID, using ${ShardReward.ID} instead!")
          ShardReward()
        }
      }
    }
  }

  abstract fun receive()
  fun makeCopy(): QuestReward = this::class.java.newInstance()
}