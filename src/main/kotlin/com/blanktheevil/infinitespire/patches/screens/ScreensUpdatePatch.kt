package com.blanktheevil.infinitespire.patches.screens

import com.badlogic.gdx.Gdx
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.utils.DebugControls
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
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
    InfiniteSpire.debugControls.update()

    if (InfiniteSpire.pauseGame && !InfiniteSpire.debugControls.stepForward.isJustPressed) return SpireReturn.Return(null)

    return SpireReturn.Continue()
  }
}