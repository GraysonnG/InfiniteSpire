package com.blanktheevil.infinitespire.crossover

import com.blanktheevil.infinitespire.crossover.utils.CrossoverManager
import java.util.function.Consumer

class Crossovers {
  companion object {
    fun init() {
      with(CrossoverManager) {
        addCrossover("ReplayTheSpireMod", Consumer {
          // add broken mirror

          // register abe quest
        })
        addCrossover("hubris", Consumer { /* do nothing */ })
        addCrossover("bard", Consumer {
          // add bard black cards
        })
      }
    }
  }
}