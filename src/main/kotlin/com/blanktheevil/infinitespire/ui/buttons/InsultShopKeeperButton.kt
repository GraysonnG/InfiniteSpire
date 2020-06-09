package com.blanktheevil.infinitespire.ui.buttons

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.quests.InsultShopKeeperQuest
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster

open class InsultShopKeeperButton : SpireElement, SpireClickable {
  companion object {
    private val X_POS = Settings.WIDTH.div(4f)
    private val Y_POS = Settings.HEIGHT.div(2f)
    private val HB_W = 260.scale()
    private val HB_H = 80.scale()
    private val IMG by lazy {
      ImageMaster.REWARD_SCREEN_TAKE_BUTTON.asAtlasRegion()
    }
    val instance by lazy {
      InsultShopKeeperButton()
    }
  }

  val hb = Hitbox(0f, 0f, HB_W, HB_H)

  override fun update() {
    hb.update()
    hb.move(
      X_POS.plus(IMG.packedWidth.div(2f)),
      Y_POS
    )
    if (leftClicked()) {
      CardCrawlGame.sound.play("UI_CLICK_1")
      InfiniteSpire.questLog.asSequence()
        .filter { it.questId == InsultShopKeeperQuest.ID }
        .forEach { it.complete = true }
    }
  }

  override fun render(sb: SpriteBatch) {
    sb.color = Color.WHITE.cpy()
    sb.draw(
      IMG,
      X_POS,
      Y_POS.minus(IMG.packedHeight.div(2f)),
      IMG.packedWidth.div(2f),
      IMG.packedHeight.div(2f),
      IMG.packedWidth.toFloat(),
      IMG.packedHeight.toFloat(),
      scale,
      scale,
      0f
    )

    FontHelper.renderFontCentered(
      sb,
      FontHelper.topPanelInfoFont,
      "Insult",
      X_POS + IMG.packedWidth.div(2f),
      Y_POS
    )

    getHitbox().render(sb)
  }

  override fun getHitbox(): Hitbox = hb
}