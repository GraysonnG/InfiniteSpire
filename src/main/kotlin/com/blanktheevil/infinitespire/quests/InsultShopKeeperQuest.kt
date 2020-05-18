package com.blanktheevil.infinitespire.quests

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.textures.Textures

class InsultShopKeeperQuest : Quest(
  questId = "InsultQuest".makeID(),
  questImg = Textures.ui.getString("questlog2/mystery.png"),
  color = Color.RED.cpy()
) {
  val randInsult = MathUtils.random(1, strings.DESCRIPTIONS.size.minus(2))

  override fun makeCopy(): Quest = InsultShopKeeperQuest()

  override fun makeDescription(): String {
    return strings.DESCRIPTIONS[0] + strings.DESCRIPTIONS[randInsult]
  }
}