package com.blanktheevil.infinitespire.crossover.utils

import com.blanktheevil.infinitespire.cards.utils.CardManager
import com.blanktheevil.infinitespire.crossover.Crossover
import com.blanktheevil.infinitespire.crossover.cards.black.DarkHarmony
import com.blanktheevil.infinitespire.extensions.log
import com.evacipated.cardcrawl.modthespire.Loader

object CrossoverManager {
  private val crossovers = mutableListOf<Crossover>()

  fun init() {
    addCrossover(Crossover("ReplayTheSpireMod"))
    addCrossover(Crossover("hubris"))
    addCrossover(Crossover("bard") {
      CardManager.addBlackCard(DarkHarmony())
    })
  }

  private fun addCrossover(crossover: Crossover) {
    crossovers.add(crossover)
  }

  fun addCrossoverContent() {
    crossovers.stream()
      .filter {
        Loader.isModLoaded(it.modid)
      }
      .forEach {
        log.info("Found Mod: ${it.modid}")
        log.info("|\tLoading Crossover Content...")
        try {
          it.addContent.invoke()
          it.isLoaded = true
          log.info("|\tDone Adding Content!\n")
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
  }
}