package com.blanktheevil.infinitespire.rooms.avhari.shop

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.subVoidShard
import com.blanktheevil.infinitespire.rooms.avhari.interfaces.ShopElement
import com.blanktheevil.infinitespire.rooms.avhari.interfaces.ShopElementBase
import com.blanktheevil.infinitespire.rooms.avhari.utils.AvhariManager
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.helpers.Hitbox

class ShopCard(private val card: BlackCard, cost: Int) : ShopElementBase(cost) {
  companion object {
    private const val SCALE = 0.65f
    private const val HOVER_SCALE = 0.85f
    private val SHARD_TEXTURE by lazy { Textures.ui.get("topPanel/avhari/voidShard.png").asAtlasRegion() }
  }

  private val lockAlpha = Color(1f, 1f, 1f, 1f)

  override fun update() {
    card.update()
    card.hb.update()
    renderOnTop = card.hb.hovered

    if (card.hb.hovered) {
      card.targetDrawScale = HOVER_SCALE
    } else {
      card.targetDrawScale = SCALE
    }
  }

  override fun purchace() {
    if (!purchaced) {
      CardCrawlGame.sound.play("SHOP_PURCHASE")
      subVoidShard(cost)
      purchaced = true
    }
  }

  override fun render(sb: SpriteBatch) {
    card.render(sb)
//    renderPrice(sb)
  }

  override fun renderPrice(sb: SpriteBatch) {
    with (SHARD_TEXTURE) {
      val shardCx = packedWidth.div(2f)
      val shardCy = packedHeight.div(2f)

      sb.color = lockAlpha
      sb.draw(
        this,
        card.current_x + shardCx,
        card.current_y + shardCy,
        shardCx,
        shardCy,
        packedWidth.toFloat(),
        packedHeight.toFloat(),
        1.0f,
        1.0f,
        0f
      )
    }
  }

  override fun getHitbox(): Hitbox = card.hb

  override fun placeAtPoint(position: Vector2, distance: Float, rotation: Float, index: Int, size: Int) {
    val point = AvhariManager.getPoint(position, distance, rotation, index, size)
    card.target_x = point.x
    card.target_y = point.y
  }
}