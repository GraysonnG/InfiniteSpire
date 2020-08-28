package com.blanktheevil.infinitespire.rooms.avhari

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.rooms.avhari.shop.Shop
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.ShopRoom

class AvhariRoom : AbstractRoom() {
  var avhari: Avhari? = null
  var shop = Shop()

  init {
    this.phase = RoomPhase.COMPLETE
    this.mapSymbol = "AVR"
    this.mapImg = ImageMaster.MAP_NODE_MERCHANT
    this.mapImgOutline = ImageMaster.MAP_NODE_MERCHANT_OUTLINE
  }

  override fun onPlayerEntry() {
    avhari = Avhari()
    playBGM("SHOP")
    AbstractDungeon.overlayMenu.proceedButton.setLabel(ShopRoom.TEXT[0])
  }

  override fun update() {
    super.update()
    if (avhari != null) {
      avhari!!.update()
    }
    shop.update()
  }

  override fun render(sb: SpriteBatch) {
    super.render(sb)
    if (avhari != null) {
      avhari!!.render(sb)
    }
    shop.render(sb)
  }
}