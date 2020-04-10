package com.blanktheevil.infinitespire.patches.proceedbutton

import com.blanktheevil.infinitespire.extensions.log
import com.blanktheevil.infinitespire.interfaces.RoomTransitionInterface
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.saveAndContinue.SaveFile


@Suppress("unused")
@SpirePatch(clz = AbstractDungeon::class, method = "nextRoomTransition", paramtypez = [SaveFile::class])
class RoomTransitionPatch {
  companion object {
    @JvmStatic
    @SpirePrefixPatch
    fun onRoomNodeClicked(instance: AbstractDungeon, saveFile: SaveFile?) {
      RoomTransitionInterface.subscribers.forEach {
        it.onRoomTransition(AbstractDungeon.getCurrRoom(), AbstractDungeon.nextRoom.room)
      }
    }
  }
}