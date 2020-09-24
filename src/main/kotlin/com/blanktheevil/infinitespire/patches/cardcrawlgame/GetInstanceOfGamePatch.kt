package com.blanktheevil.infinitespire.patches.cardcrawlgame

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.core.CardCrawlGame

@SpirePatch(clz = CardCrawlGame::class, method = "create")
object GetInstanceOfGamePatch {
  lateinit var game: CardCrawlGame

  @JvmStatic
  @SpirePostfixPatch
  fun getGame(instance: CardCrawlGame) {
    game = instance
  }
}