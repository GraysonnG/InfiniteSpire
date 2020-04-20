package com.blanktheevil.infinitespire.rooms

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.ShopRoom

class AvhariRoom : AbstractRoom() {
  init {
    this.phase = RoomPhase.COMPLETE
    this.mapSymbol = "AVR"
    this.mapImg = ImageMaster.MAP_NODE_MERCHANT
    this.mapImgOutline = ImageMaster.MAP_NODE_MERCHANT_OUTLINE
  }


  override fun onPlayerEntry() {
    playBGM("SHOP")
    AbstractDungeon.overlayMenu.proceedButton.setLabel(ShopRoom.TEXT[0])
  }
}