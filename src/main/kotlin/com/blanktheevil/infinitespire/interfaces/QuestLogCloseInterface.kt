package com.blanktheevil.infinitespire.interfaces

import com.blanktheevil.infinitespire.models.QuestLog

interface QuestLogCloseInterface : IInfiniteSpire {
  fun onQuestLogClose(questLog: QuestLog)

  companion object {
    val subscribers = mutableListOf<QuestLogCloseInterface>()
  }
}