package com.blanktheevil.infinitespire.patches.shopRoom

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.quests.InsultShopKeeperQuest
import com.blanktheevil.infinitespire.ui.buttons.InsultShopKeeperButton
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.rooms.ShopRoom

@Suppress("unused")
@SpirePatch(clz = ShopRoom::class, method = "render")
object ShopRoomRenderPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun renderInsultButton(shopRoom: ShopRoom, sb: SpriteBatch) {
    if (InfiniteSpire.questLog.any { it.questId == InsultShopKeeperQuest.ID }) {
      InsultShopKeeperButton.instance.render(sb)
    }
  }
}