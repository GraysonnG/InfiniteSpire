package com.blanktheevil.infinitespire.patches.screens

import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.ui.panels.TopPanel

@Suppress("unused")
@SpirePatch(clz = TopPanel::class, method = "update")
class TopPanelInputUpdateSuppressor {
  companion object {
    @SpireInsertPatch(
      rloc = 0
    )
    @JvmStatic
    fun suppressInputs(): SpireReturn<Void> {
      return if (
        InfiniteSpire.questLogScreen.isOpen() ||
        InfiniteSpire.powerSelectScreen.show ||
        InfiniteSpire.infiniteScreen.show
      ) {
        SpireReturn.Return(null)
      } else {
        SpireReturn.Continue()
      }
    }
  }
}