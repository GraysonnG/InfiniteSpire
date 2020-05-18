package com.blanktheevil.infinitespire.quests

import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.enums.QuestType
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.interfaces.RoomTransitionInterface
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.EmptyRoom

class IgnoreRelicQuest : Quest(
  color = Color.BLUE.cpy(),
  questId = "IgnoreRelicQuest".makeID(),
  questImg = Textures.ui.getString("questlog2/treasure.png")
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

  override fun makeCopy(): Quest {
    return IgnoreRelicQuest()
  }

  override fun makeDescription(): String {
    return strings.DESCRIPTIONS[0]
  }
}