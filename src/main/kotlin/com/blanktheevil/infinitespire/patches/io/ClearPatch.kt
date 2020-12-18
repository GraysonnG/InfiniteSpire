package com.blanktheevil.infinitespire.patches.io

import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.screens.options.ConfirmPopup

@Suppress("unused")
@SpirePatch(clz = ConfirmPopup::class, method = "yesButtonEffect")
object ClearPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun saveAndExit(popup: ConfirmPopup) {
    if (popup.type == ConfirmPopup.ConfirmType.ABANDON_MID_RUN || popup.type == ConfirmPopup.ConfirmType.ABANDON_MAIN_MENU) {
      InfiniteSpire.saveData.clear()
      InfiniteSpire.saveData.save()
    }
  }
}