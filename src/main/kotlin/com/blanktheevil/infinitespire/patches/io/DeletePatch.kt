package com.blanktheevil.infinitespire.patches.io

import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = SaveAndContinue::class, method = "deleteSave")
object DeletePatch {
  @JvmStatic
  @SpirePostfixPatch
  fun clearInfiniteSpire(player: AbstractPlayer) {
    InfiniteSpire.saveData.clear()
    InfiniteSpire.saveData.save()
  }
}