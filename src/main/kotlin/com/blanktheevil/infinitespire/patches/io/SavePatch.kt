package com.blanktheevil.infinitespire.patches.io

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.log
import com.blanktheevil.infinitespire.interfaces.Savable
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.saveAndContinue.SaveFile

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(
  clz = SaveFile::class,
  method = SpirePatch.CONSTRUCTOR,
  paramtypez = [SaveFile.SaveType::class]
)
object SavePatch {
  @JvmStatic
  @SpirePostfixPatch
  fun saveInfiniteSpireData(saveFile: SaveFile) {
    log.info("Saving Data...")
    Savable.savables.forEach {
      it.beforeConfigSave(InfiniteSpire.saveData)
    }
    InfiniteSpire.saveData.save()
  }
}