package com.blanktheevil.infinitespire.models

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.enums.QuestType
import com.blanktheevil.infinitespire.interfaces.ActCompleteInterface
import com.blanktheevil.infinitespire.interfaces.Savable
import java.util.*

class QuestLog(savable: Boolean = false) : ArrayList<Quest>(), Savable, ActCompleteInterface {

  val redQuests = mutableListOf<Quest>()
  val blueQuests = mutableListOf<Quest>()
  val greenQuests = mutableListOf<Quest>()

  var selectedQuest: Quest? = null

  init {
    if (savable) subscribe()
  }

  fun isQuestSelected(): Boolean = selectedQuest != null

  private fun addQuestToTypedList(element: Quest) {
    when (element.type) {
      QuestType.RED -> redQuests.add(element)
      QuestType.BLUE -> blueQuests.add(element)
      QuestType.GREEN -> greenQuests.add(element)
    }
  }

  override fun add(element: Quest): Boolean {
    addQuestToTypedList(element)
    return super.add(element)
  }

  override fun add(index: Int, element: Quest) {
    addQuestToTypedList(element)
    super.add(index, element)
  }

  override fun addAll(elements: Collection<Quest>): Boolean {
    elements.forEach { addQuestToTypedList(it) }
    return super.addAll(elements)
  }

  override fun addAll(index: Int, elements: Collection<Quest>): Boolean {
    elements.forEach { addQuestToTypedList(it) }
    return super.addAll(index, elements)
  }

  override fun onActCompleted(actId: String) {
    selectedQuest = null
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
}