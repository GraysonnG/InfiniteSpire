package com.blanktheevil.infinitespire.patches.questlog

import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = AbstractDungeon::class, method = "update")
class QuestLogUpdatePatch {
  companion object {
    @SpirePrefixPatch
    @JvmStatic
    fun run(dungeon: AbstractDungeon) {
      InfiniteSpire.questLogScreen.update()
    }
  }
}