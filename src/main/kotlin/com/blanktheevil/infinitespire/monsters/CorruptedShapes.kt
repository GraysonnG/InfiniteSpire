package com.blanktheevil.infinitespire.monsters

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.vfx.ShapeMonsterVFX
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.monsters.AbstractMonster

class CorruptedShapes : AbstractMonster(
  "Corrupted Shapes",
  ID,
  60,
  0.0f,
  0.0f,
  300f,
  300f,
  null
) {
  companion object {
    val ID = "CorruptedShapes".makeID()
    private val strings = languagePack.getMonsterStrings(ID)
  }

  private val frontShapes = mutableListOf<ShapeMonsterVFX>()
  private val middleShapes = mutableListOf<ShapeMonsterVFX>()
  private val backShapes = mutableListOf<ShapeMonsterVFX>()
  private val shapes = mutableListOf(backShapes, middleShapes, frontShapes)

  private var poke = 3
  private var explode = 30
  private var blockbase = 3

  init {
    this.img = Textures.monsters.get("massofshapes/massofshapes.png")

    this.damage.also {
      it.add(DamageInfo(this, poke))
      it.add(DamageInfo(this, explode))
    }
  }

  override fun update() {
    super.update()
    shapes.forEachIndexed() { index, list ->
      if (list.size < 10 && !(this.isDying || this.isDead)) {
        for (i in 0 until 10 - list.size) {
          list.add(when (index) {
            2 -> ShapeMonsterVFX(
              hitbox = this.hb,
              color = InfiniteSpire.PURPLE.cpy(),
              scale = 1f
            )
            1 -> ShapeMonsterVFX(
              hitbox = this.hb,
              color = InfiniteSpire.PURPLE.cpy().mul(Color.GRAY),
              scale = .9f
            )
            else -> ShapeMonsterVFX(
              hitbox = this.hb,
              color = InfiniteSpire.PURPLE.cpy().mul(Color.DARK_GRAY).cpy(),
              scale = .75f
            )
          })
        }
      }
      if (this.isDying || this.isDead) {
        list.forEach { it.setDying() }
      }
      list.forEach { it.update() }
      list.removeIf { it.isDead() }
    }
  }

  override fun render(sb: SpriteBatch) {
    shapes.forEach { list ->
      list.forEach { it.render(sb) }
    }
    super.render(sb)
  }

  override fun getMove(roll: Int) {
    setMove(0.toByte(), Intent.MAGIC)
  }

  override fun takeTurn() {

  }
}