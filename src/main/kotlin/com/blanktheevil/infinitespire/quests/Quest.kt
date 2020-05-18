package com.blanktheevil.infinitespire.quests

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.enums.QuestType
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.interfaces.IInfiniteSpire
import com.blanktheevil.infinitespire.interfaces.QuestCompleteInterface
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.models.QuestStringsKt
import com.blanktheevil.infinitespire.quests.questrewards.QuestReward
import com.blanktheevil.infinitespire.quests.questrewards.ShardReward
import com.blanktheevil.infinitespire.textures.TextureLoaderKt
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.helpers.Hitbox
import java.util.*

abstract class Quest(
  val color: Color,
  val questId: String,
  val questImg: String,
  val hb: Hitbox = Hitbox(48f, 48f),
  val uuid: String = UUID.randomUUID().toString(),
  var complete: Boolean = false,
  private val rewardID: String = ShardReward.ID
) : SpireElement, SpireClickable, IInfiniteSpire {

  val img = TextureLoaderKt.getTexture(questImg).asAtlasRegion()
  val strings = InfiniteSpire.questStringsKt[questId] ?: QuestStringsKt()
  val name = strings.NAME

  init {
    subscribe()
  }

  fun getReward(): QuestReward = QuestReward.getQuestRewardById(rewardID)

  fun onQuestComplete() {
    QuestCompleteInterface.subscribers.forEach {
      it.onQuestComplete(this)
    }
  }

  abstract fun makeCopy() : Quest
  abstract fun makeDescription(): String

  override fun update() {
    if (complete) {
      getReward().receive()
    }
  }

  override fun render(sb: SpriteBatch) {
    // for custom render
  }

  override fun getHitbox(): Hitbox {
    return hb
  }
}