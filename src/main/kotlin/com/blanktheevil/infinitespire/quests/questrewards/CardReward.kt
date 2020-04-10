package com.blanktheevil.infinitespire.quests.questrewards

import com.blanktheevil.infinitespire.extensions.makeID

class CardReward : QuestReward(ID) {
  companion object {
    val ID = "CardReward".makeID()
  }

  override fun receive() {
  }
}