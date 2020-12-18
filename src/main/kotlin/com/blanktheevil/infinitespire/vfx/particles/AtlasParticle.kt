package com.blanktheevil.infinitespire.vfx.particles

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.megacrit.cardcrawl.scenes.AbstractScene

class AtlasParticle(
  private val atlas: TextureAtlas
) : Particle() {
  override fun isDead(): Boolean = false

  override fun update() {
//    atlas.regions.get
  }

  override fun render(sb: SpriteBatch) {
  }
}