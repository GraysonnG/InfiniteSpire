package com.blanktheevil.infinitespire.quests.questrewards

import com.blanktheevil.infinitespire.extensions.makeID

class HealthReward : QuestReward(ID) {
  companion object {
    val ID = "HealthReward".makeID()
  }

  override fun receive() {
  }
}