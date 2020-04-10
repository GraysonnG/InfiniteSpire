package com.blanktheevil.infinitespire.interfaces

import com.blanktheevil.infinitespire.quests.Quest

interface QuestCompleteInterface : IInfiniteSpire {
  fun onQuestComplete(quest: Quest)

  companion object {
    val subscribers = mutableListOf<QuestCompleteInterface>()
  }
}