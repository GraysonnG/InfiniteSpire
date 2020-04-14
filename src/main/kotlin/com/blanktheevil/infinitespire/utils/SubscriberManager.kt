package com.blanktheevil.infinitespire.utils

import com.blanktheevil.infinitespire.interfaces.*

class SubscriberManager {
  companion object {
    fun <T : IInfiniteSpire> subscribe(subscriber: T) {
      if (subscriber is Savable) {
        Savable.savables.add(subscriber)
      }

      if (subscriber is ActCompleteInterface) {
        ActCompleteInterface.subscribers.add(subscriber)
      }

      if (subscriber is QuestLogCloseInterface) {
        QuestLogCloseInterface.subscribers.add(subscriber)
      }

      if (subscriber is QuestCompleteInterface) {
        QuestCompleteInterface.subscribers.add(subscriber)
      }

      if (subscriber is RoomTransitionInterface) {
        RoomTransitionInterface.subscribers.add(subscriber)
      }
    }
  }
}