package com.blanktheevil.infinitespire.patches.endlessscreen

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.log
import com.blanktheevil.infinitespire.screens.EndlessScreen
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.ui.buttons.ProceedButton

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = ProceedButton::class, method = "goToNextDungeon")
class ProceedButtonPatch {
  companion object {
    @SpirePrefixPatch
    @JvmStatic
    fun openEndlessScreen(proceedButton: ProceedButton, room: AbstractRoom) {
      InfiniteSpire.saveData.actNumber++
      if (InfiniteSpire.saveData.actNumber.rem(2) == 0 && EndlessScreen.shouldPrompt) {
        InfiniteSpire.endlessScreen.open() {
          log.info("Closed Screen")
        }
      }
    }
  }
}