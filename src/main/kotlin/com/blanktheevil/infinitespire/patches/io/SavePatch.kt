package com.blanktheevil.infinitespire.patches.io

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.interfaces.Savable
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.screens.options.ConfirmPopup

@Suppress("unused")
@SpirePatch(clz = ConfirmPopup::class, method = "effect")
class SavePatch {
  companion object {
    @JvmStatic
    @SpirePostfixPatch
    fun saveAndExit(popup: ConfirmPopup) {
      if (popup.type == ConfirmPopup.ConfirmType.EXIT) {
        InfiniteSpire.logger.info("Infinite Spire saving data...")
        Savable.savables.forEach {
          it.beforeConfigSave()
        }
        InfiniteSpire.config.save()
      } else if (popup.type == ConfirmPopup.ConfirmType.ABANDON) {
        InfiniteSpire.logger.info("Infinite Spire clearing run data...")
        Savable.savables.forEach {
          it.clearData()
          it.beforeConfigSave()
        }
        InfiniteSpire.config.save()
      }
    }
  }
}