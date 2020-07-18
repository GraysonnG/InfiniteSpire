package com.blanktheevil.infinitespire.models

import basemod.BaseMod
import basemod.interfaces.PostDungeonUpdateSubscriber
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.interfaces.ActCompleteInterface
import com.blanktheevil.infinitespire.interfaces.Savable
import com.blanktheevil.infinitespire.quests.InsultShopKeeperQuest
import com.blanktheevil.infinitespire.quests.Quest
import com.blanktheevil.infinitespire.quests.utils.QuestManager
import com.blanktheevil.infinitespire.quests.utils.TestQuest
import com.megacrit.cardcrawl.core.CardCrawlGame
import java.util.*

class QuestLog(savable: Boolean = false) : ArrayList<Quest>(), Savable, ActCompleteInterface, PostDungeonUpdateSubscriber {

  companion object {
    const val MAX_QUESTS = 10
  }

  init {
    if (savable) subscribe()
    BaseMod.subscribe(this)
  }

  override fun beforeConfigSave(saveData: SaveData) {
    InfiniteSpire.saveData.questLog.addAll(this)
  }

  override fun afterConfigLoad(saveData: SaveData) {
    this.addAll(saveData.questLog)
  }

  override fun clearData(saveData: SaveData) {
    clear()
  }

  override fun onActCompleted(actId: String) {
  }

  override fun receivePostDungeonUpdate() {
    this.forEach {
      it.update()
      if (it.complete) {
        // add vfx?
        CardCrawlGame.sound.play("UNLOCK_PING")
        // add a new quest
      }
    }
    this.removeIf { it.complete }
  }
}