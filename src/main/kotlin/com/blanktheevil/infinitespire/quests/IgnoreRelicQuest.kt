package com.blanktheevil.infinitespire.quests

import com.blanktheevil.infinitespire.enums.QuestType
import com.blanktheevil.infinitespire.interfaces.RoomTransitionInterface
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.EmptyRoom

class IgnoreRelicQuest : Quest(
  QuestType.BLUE,
  questId = IgnoreRelicQuest::class.java.name,
  questImg = "someImage.png"
), RoomTransitionInterface {
  override fun onRoomTransition(previousRoom: AbstractRoom, nextRoom: AbstractRoom) {
    when {
      previousRoom is EmptyRoom -> return
      AbstractDungeon.combatRewardScreen.rewards.any {
        it.type == RewardItem.RewardType.RELIC
      } -> complete = true
      else -> return
    }
  }
}