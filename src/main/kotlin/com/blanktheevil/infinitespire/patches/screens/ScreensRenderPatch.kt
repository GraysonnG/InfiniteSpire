package com.blanktheevil.infinitespire.patches.screens

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = AbstractDungeon::class, method = "render")
object ScreensRenderPatch {
  @SpirePostfixPatch
  @JvmStatic
  fun run(dungeon: AbstractDungeon, sb: SpriteBatch) {
    InfiniteSpire.powerSelectScreen.render(sb)
    InfiniteSpire.questLogScreen.render(sb)
  }
}