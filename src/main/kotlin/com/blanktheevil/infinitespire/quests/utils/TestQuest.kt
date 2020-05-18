package com.blanktheevil.infinitespire.quests.utils

import basemod.AutoAdd
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.quests.Quest
import com.blanktheevil.infinitespire.quests.questrewards.ShardReward
import com.blanktheevil.infinitespire.textures.Textures

@AutoAdd.Ignore
class TestQuest : Quest(
  color = getRandomColor(),
  questId = "TestQuest".makeID(),
  rewardID = ShardReward.ID,
  questImg = getRandomTexture()
) {
  companion object {
    fun getRandomColor(): Color {
      return when (MathUtils.random(0, 5)) {
        0 -> Color.RED
        1 -> Color.YELLOW
        2 -> Color.LIME
        3 -> Color.CYAN
        4 -> Color.MAGENTA
        else -> Color.ORANGE
      }
    }

    fun getRandomTexture(): String {
      return when (MathUtils.random(0, 2)) {
        0 -> Textures.ui.getString("questlog2/monster.png")
        1 -> Textures.ui.getString("questlog2/mystery.png")
        else -> Textures.ui.getString("questlog2/treasure.png")
      }
    }
  }

  override fun makeCopy(): Quest {
    return TestQuest()
  }

  override fun makeDescription(): String {
    return strings.DESCRIPTIONS[0]
  }
}