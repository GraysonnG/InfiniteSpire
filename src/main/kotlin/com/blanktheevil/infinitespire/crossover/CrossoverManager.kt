package com.blanktheevil.infinitespire.crossover

import com.blanktheevil.infinitespire.extensions.log
import com.evacipated.cardcrawl.modthespire.Loader
import java.util.function.Consumer

class CrossoverManager {
  companion object {
    private val crossovers = mutableMapOf<String, Crossover>()

    fun addCrossover(modid: String, addContent: Consumer<Void?>) {
      crossovers[modid] = Crossover(modid, addContent)
    }

    fun addCrossoverContent() {
      crossovers.entries.stream()
        .filter { Loader.isModLoaded(it.value.modid) }
        .forEach {
          log.info("Found Mod: ${it.value.modid}")
          log.info("|\tLoading Crossover Content...")
          try {
            it.value.addContent.accept(null)
            it.value.isLoaded = true
            log.info("|\tDone Adding Content!\n")
          } catch (e: Exception) {
            e.printStackTrace()
          }
        }
    }
  }
}