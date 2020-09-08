package com.blanktheevil.infinitespire.crossover.utils

import basemod.BaseMod
import basemod.helpers.RelicType
import com.blanktheevil.infinitespire.cards.utils.CardManager
import com.blanktheevil.infinitespire.crossover.Crossover
import com.blanktheevil.infinitespire.crossover.cards.black.DarkHarmony
import com.blanktheevil.infinitespire.utils.log
import com.blanktheevil.infinitespire.relics.BrokenMirror
import com.blanktheevil.infinitespire.relics.EvilPickle
import com.evacipated.cardcrawl.modthespire.Loader

object CrossoverManager {
  private val crossovers = mutableListOf<Crossover>()

  fun init() {
    addCrossover("ReplayTheSpireMod") {
      BaseMod.addRelic(BrokenMirror(), RelicType.SHARED)
      BaseMod.addRelic(EvilPickle(), RelicType.SHARED)
    }
    addCrossover("hubris")
    addCrossover("bard") {
      CardManager.addBlackCard(DarkHarmony())
    }
  }

  private fun addCrossover(modid: String, content: () -> Unit = {}) {
    crossovers.add(Crossover(modid, content))
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