package com.blanktheevil.infinitespire.patches.io

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.log
import com.blanktheevil.infinitespire.interfaces.Savable
import com.blanktheevil.infinitespire.models.SaveData
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame

@Suppress("unused")
@SpirePatch(clz = CardCrawlGame::class, method = "loadPlayerSave")
object LoadPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun loadData(game: CardCrawlGame, p: AbstractPlayer) {
    log.info("Infinite Spire loading data...")
    InfiniteSpire.saveData = SaveData.load()
    Savable.savables.forEach {
      it.afterConfigLoad(InfiniteSpire.saveData)
    }
  }
}