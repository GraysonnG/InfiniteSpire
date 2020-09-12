package com.blanktheevil.infinitespire.interfaces.hooks

import com.blanktheevil.infinitespire.models.SaveData

/**
 * This interface is used to manage the saveData
 */
interface Savable : IInfiniteSpire {
  fun beforeConfigSave(saveData: SaveData)
  fun afterConfigLoad(saveData: SaveData)
  fun clearData(saveData: SaveData)

  companion object {
    val savables = mutableListOf<Savable>()
  }
}