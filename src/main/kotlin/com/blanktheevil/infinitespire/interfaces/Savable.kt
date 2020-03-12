package com.blanktheevil.infinitespire.interfaces

import com.blanktheevil.infinitespire.models.Config

/**
 * This interface is used to manage the config
 */
interface Savable : IInfiniteSpire{
  fun beforeConfigSave()
  fun afterConfigLoad(config: Config)
  fun clearData()

  companion object {
    val savables = mutableListOf<Savable>()
  }
}