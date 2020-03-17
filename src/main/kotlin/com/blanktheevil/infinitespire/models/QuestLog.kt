package com.blanktheevil.infinitespire.models

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.enums.QuestType
import com.blanktheevil.infinitespire.interfaces.Savable
import java.util.ArrayList

class QuestLog(savable: Boolean = false) : ArrayList<Quest>(), Savable {

  val redQuests = mutableListOf<Quest>()
  val blueQuests = mutableListOf<Quest>()
  val greenQuests = mutableListOf<Quest>()

  init {
    if(savable) InfiniteSpire.subscribe(this)
  }

  private fun addQuestToTypedList(element: Quest) {
    when(element.type) {
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

  override fun beforeConfigSave(config: Config) {
    InfiniteSpire.config.questLog.addAll(this)
  }

  override fun afterConfigLoad(config: Config) {
    this.addAll(config.questLog)
  }

  override fun clearData(config: Config) {
    clear()
  }
}