package com.blanktheevil.infinitespire.patches.io

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.utils.log
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.screens.options.ConfirmPopup

@Suppress("unused")
@SpirePatch(clz = ConfirmPopup::class, method = "effect")
object ClearPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun saveAndExit(popup: ConfirmPopup) {
    if (popup.type == ConfirmPopup.ConfirmType.ABANDON) {
      InfiniteSpire.saveData.clear()
      InfiniteSpire.saveData.save()
    }
  }
}