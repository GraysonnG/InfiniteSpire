package com.blanktheevil.infinitespire.patches.proceedbutton

import com.blanktheevil.infinitespire.interfaces.RoomTransitionInterface
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.saveAndContinue.SaveFile


@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = AbstractDungeon::class, method = "nextRoomTransition", paramtypez = [SaveFile::class])
object RoomTransitionPatch {
  @JvmStatic
  @SpirePrefixPatch
  fun onRoomNodeClicked(instance: AbstractDungeon, saveFile: SaveFile?) {
    if (
      AbstractDungeon.currMapNode != null &&
      AbstractDungeon.getCurrRoom() != null &&
      AbstractDungeon.nextRoom != null &&
      AbstractDungeon.nextRoom.room != null
    ) {
      RoomTransitionInterface.subscribers.forEach {
        it.onRoomTransition(AbstractDungeon.getCurrRoom(), AbstractDungeon.nextRoom.room)
      }
    }
  }
}