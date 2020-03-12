package com.blanktheevil.infinitespire.crossover

import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.Loader
import java.lang.Exception
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
          InfiniteSpire.logger.info("Found Mod: ${it.value.modid}")
          InfiniteSpire.logger.info("|\tLoading Crossover Content...")
          try {
            it.value.addContent.accept(null)
            it.value.isLoaded = true
            InfiniteSpire.logger.info("|\tDone Adding Content!\n")
          } catch (e: Exception) {
            e.printStackTrace()
          }
        }
    }
  }
}