package com.blanktheevil.infinitespire.quests

import com.blanktheevil.infinitespire.enums.QuestType
import com.blanktheevil.infinitespire.interfaces.QuestLogCloseInterface
import com.blanktheevil.infinitespire.models.QuestLog
import com.blanktheevil.infinitespire.quests.questrewards.RelicReward

class CloseQuest : Quest(
  QuestType.RED,
  questId = CloseQuest::class.java.name,
  questImg = "someImg.png",
  rewardID = RelicReward.ID), QuestLogCloseInterface {
  companion object {
    const val TIMES_TO_COMPLETE = 20
  }

  var timesClosed = 0

  override fun onQuestLogClose(questLog: QuestLog) {
    timesClosed++
    if (timesClosed == TIMES_TO_COMPLETE) {
      complete = true
    }
  }
}