package com.blanktheevil.infinitespire.quests

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.enums.QuestType
import com.blanktheevil.infinitespire.interfaces.IInfiniteSpire
import com.blanktheevil.infinitespire.interfaces.QuestCompleteInterface
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.quests.questrewards.QuestReward
import com.blanktheevil.infinitespire.quests.questrewards.ShardReward
import com.megacrit.cardcrawl.helpers.Hitbox
import java.util.*

abstract class Quest(
  val type: QuestType,
  val questId: String,
  val questImg: String,
  val mystery: Boolean = false,
  val hb: Hitbox = Hitbox(100f, 200f),
  val uuid: String = UUID.randomUUID().toString(),
  var complete: Boolean = false,
  private val rewardID: String = ShardReward.ID
) : SpireElement, SpireClickable, IInfiniteSpire {

  init {
    subscribe()
  }

  fun abandon() {
    TODO("implement abandon")
  }

  fun getReward(): QuestReward = QuestReward.getQuestRewardById(rewardID)

  fun onQuestComplete() {
    QuestCompleteInterface.subscribers.forEach {
      it.onQuestComplete(this)
    }
  }

  override fun update() {
    if (complete) {
      getReward().receive()
    }
  }

  override fun render(sb: SpriteBatch) {
  }

  override fun getHitbox(): Hitbox {
    return hb
  }
}