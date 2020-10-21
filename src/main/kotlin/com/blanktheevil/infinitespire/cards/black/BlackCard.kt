package com.blanktheevil.infinitespire.cards.black

import basemod.AutoAdd.Ignore
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.cards.Card
import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.vfx.particles.BlackCardParticle
import com.blanktheevil.infinitespire.vfx.particlesystems.ParticleSystem
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster

@Ignore
abstract class BlackCard(
  id: String,
  img: String,
  type: CardType,
  target: CardTarget,
  cost: Int,
  exhaust: Boolean,
  use: Card.(player: AbstractPlayer?, monster: AbstractMonster?) -> Unit = { _, _ -> },
  init: Card.() -> Unit = {},
  upgr: Card.() -> Unit = {}
) : Card(id, img, type, target, RARITY, EnumPatches.CardColor.INFINITE_BLACK, cost, exhaust, use, init, upgr) {
  companion object {
    private val RARITY = EnumPatches.CardRarity.BLACK
  }

  private val particleSystem = ParticleSystem(
    createNewParticle = {
      BlackCardParticle(
        VFXManager.generateRandomPointAlongEdgeOfHitbox(hb),
        drawScale,
        upgraded,
        glow = false
      )
    }
  )

  constructor(builder: CardBuilder) :
    this(builder.id, builder.img, builder.type, builder.target, builder.cost, builder.exhaust, builder.use, builder.init, builder.upgr)

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
    this.glowColor = InfiniteSpire.PURPLE.cpy()
    super.update()
    particleSystem.update()
  }

  override fun render(sb: SpriteBatch) {
    with(sb) {
      color = Color.WHITE.cpy()
      particleSystem.render(sb)
      super.render(this)
    }
  }
}