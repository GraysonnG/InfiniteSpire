package com.blanktheevil.infinitespire.patches.io

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.interfaces.Savable
import com.blanktheevil.infinitespire.models.Config
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame

@Suppress("unused")
@SpirePatch(clz = CardCrawlGame::class, method = "loadPlayerSave")
class LoadPatch {
  companion object {
    @JvmStatic
    @SpirePostfixPatch
    fun loadData(game: CardCrawlGame, p: AbstractPlayer) {
      InfiniteSpire.logger.info("Infinite Spire loading data...")
      InfiniteSpire.config = Config.load()
      Savable.savables.forEach {
        it.afterConfigLoad(InfiniteSpire.config)
      }
    }
  }
}