package com.blanktheevil.infinitespire.interfaces.utils

import basemod.BaseMod
import basemod.interfaces.ISubscriber
import com.blanktheevil.infinitespire.interfaces.hooks.*

object SubscriberManager {
  fun <T : IInfiniteSpire> subscribe(subscriber: T) {
    if (subscriber is Savable) {
      Savable.savables.add(subscriber)
    }

    if (subscriber is ActCompleteInterface) {
      ActCompleteInterface.subscribers.add(subscriber)
    }

    if (subscriber is OnMonsterDeathInterface) {
      OnMonsterDeathInterface.subscribers.add(subscriber)
    }

    if (subscriber is AfterTurnEndInterface) {
      AfterTurnEndInterface.subscribers.add(subscriber)
    }

    if (subscriber is ISubscriber) {
      BaseMod.subscribe(subscriber)
    }
  }

  fun <T : IInfiniteSpire> unsubscribe(subscriber: T) {
    if (subscriber is Savable) {
      Savable.savables.remove(subscriber)
    }

    if (subscriber is ActCompleteInterface) {
      ActCompleteInterface.subscribers.remove(subscriber)
    }

    if (subscriber is OnMonsterDeathInterface) {
      OnMonsterDeathInterface.subscribers.remove(subscriber)
    }

    if (subscriber is AfterTurnEndInterface) {
      AfterTurnEndInterface.subscribers.remove(subscriber)
    }

    if (subscriber is ISubscriber) {
      BaseMod.unsubscribe(subscriber)
    }
  }
}