package com.blanktheevil.infinitespire.cards.black

import basemod.AutoAdd.Ignore
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.cards.Card
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.vfx.BlackCardParticle
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.random.Random
import kotlin.math.ceil

@Ignore
abstract class BlackCard(
  id: String,
  img: String,
  type: CardType,
  target: CardTarget,
  cost: Int,
  use: Card.(player: AbstractPlayer?, monster: AbstractMonster?) -> Unit = {_,_ -> },
  init: Card.() -> Unit = {},
  upgr: Card.() -> Unit = {}
) : Card(
  id,
  img,
  type,
  target,
  RARITY,
  EnumPatches.CardColor.INFINITE_BLACK,
  cost,
  use,
  init,
  upgr
) {
  companion object {
    private val RARITY = EnumPatches.CardRarity.BLACK
    private val FPS_SCALE: Int = ceil(240.0.div(Settings.MAX_FPS)).toInt()
    private const val MAX_PARTICLES: Int = 150
  }

  private val particles = mutableListOf<BlackCardParticle>()

  constructor(builder: CardBuilder):
      this(builder.id, builder.img, builder.type, builder.target, builder.cost, builder.use, builder.init, builder.upgr)

  init {
    setOrbAndBanner()
  }

  private fun setOrbAndBanner() {
    setOrbTexture(
      Textures.cards.getString("ui/512/boss-orb.png"),
      Textures.cards.getString("ui/1024/boss-orb.png")
    )
    setBannerTexture(
      Textures.cards.getString("ui/512/boss-banner.png"),
      Textures.cards.getString("ui/1024/boss-banner.png")
    )
  }

  override fun update() {
    super.update()

    particles.stream()
      .forEach { it.update() }
    particles.removeIf {
      it.isDead()
    }

    if (particles.size < MAX_PARTICLES) {
      for (i in 1..FPS_SCALE.times(2)) {
        particles.add(BlackCardParticle(generateRandomPointAlongEdgeOfHitbox(), drawScale, upgraded))
      }
    }
  }

  override fun render(sb: SpriteBatch) {
    with(sb) {
      color = Color.WHITE.cpy()
      particles.stream()
        .forEach { it.render(this) }
      super.render(this)
    }
  }

  private fun generateRandomPointAlongEdgeOfHitbox(): Vector2 {
    return Vector2().also {
      with(Random()) {
        val topOrBottom = randomBoolean()
        val leftOrRight = randomBoolean()
        val verticalOrHorizontal = randomBoolean()

        if (verticalOrHorizontal) {
          it.x = random(
            hb.cX.minus(hb.width.div(2f)),
            hb.cX.plus(hb.width.div(2f)))
          it.y = if (topOrBottom)
            hb.cY.plus(hb.height.div(2f)) else
            hb.cY.minus(hb.height.div(2f))
        } else {
          it.x = if (leftOrRight)
            hb.cX.plus(hb.width.div(2f)) else
            hb.cX.minus(hb.width.div(2f))
          it.y = random(
            hb.cY.minus(hb.height.div(2f)),
            hb.cY.plus(hb.height.div(2f))
          )
        }
      }
    }
  }
}