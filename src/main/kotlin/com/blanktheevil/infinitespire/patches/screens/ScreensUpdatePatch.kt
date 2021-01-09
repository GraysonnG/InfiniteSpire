package com.blanktheevil.infinitespire.patches.screens

import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = AbstractDungeon::class, method = "update")
object ScreensUpdatePatch {
  @SpirePrefixPatch
  @JvmStatic
  fun run(dungeon: AbstractDungeon): SpireReturn<Void> {
    InfiniteSpire.badgeOverlay.update()
    InfiniteSpire.powerSelectScreen.update()
    return SpireReturn.Continue()
  }
}