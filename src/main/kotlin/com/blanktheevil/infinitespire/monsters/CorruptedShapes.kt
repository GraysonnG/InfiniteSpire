package com.blanktheevil.infinitespire.monsters

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.monsters.utils.Move
import com.blanktheevil.infinitespire.monsters.utils.setMove
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.vfx.particles.ShapeMonsterParticle
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction
import com.megacrit.cardcrawl.cards.status.Dazed
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.random.Random
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect

class CorruptedShapes : Monster(
  "Corrupted Shapes",
  ID,
  80,
  0.0f,
  0.0f,
  300f,
  300f
) {
  companion object {
    val ID = "CorruptedShapes".makeID()
    private val strings = languagePack.getMonsterStrings(ID)
  }

  private val frontShapes = mutableListOf<ShapeMonsterParticle>()
  private val middleShapes = mutableListOf<ShapeMonsterParticle>()
  private val backShapes = mutableListOf<ShapeMonsterParticle>()
  private val shapes = mutableListOf(backShapes, middleShapes, frontShapes)

  private var dazedCount = 3
  private val explodeDamage = 30
  private val pokeDamage = 2
  private val pokeMultiplier = 10
  private val dazedDamage = 15

  private val EXPLODE = Move(Intent.ATTACK, explodeDamage) {
    com.blanktheevil.infinitespire.extensions.addToBot(
      DamageAction(player, it.damage[this.getByte().toInt()], AbstractGameAction.AttackEffect.FIRE)
    )
    effectsQueue.add(ExplosionSmallEffect(it.hb.cX, it.hb.cY))
  }
  private val DAZED = Move(Intent.ATTACK_DEBUFF, dazedDamage) {
    it.dealDamage(player, this.damage)
    com.blanktheevil.infinitespire.extensions.addToBot(MakeTempCardInDrawPileAction(Dazed().makeCopy(), dazedCount, true, true))
  }
  private val POKE = Move(Intent.ATTACK, pokeDamage, multiplier = pokeMultiplier, isMultiDamage = true) {
    for (i in 0 until this.multiplier) {
      val effect = when(Random().random(2))  {
        0 -> AbstractGameAction.AttackEffect.FIRE
        1 -> AbstractGameAction.AttackEffect.BLUNT_HEAVY
        else -> AbstractGameAction.AttackEffect.BLUNT_LIGHT
      }
      com.blanktheevil.infinitespire.extensions.addToBot(
        DamageAction(player, it.damage[this.getByte().toInt()], effect)
      )
    }
  }

  init {
    this.img = Textures.monsters.get("massofshapes/massofshapes.png")
    
    dazedCount = if (AbstractDungeon.ascensionLevel >= 7) 4 else 3

    if (AbstractDungeon.ascensionLevel >= 2) {
      EXPLODE.modify(damage = explodeDamage + 5)
      POKE.modify(multiplier = pokeMultiplier + 2)
      DAZED.modify(damage = dazedDamage + 5)
    }

    registerMove(EXPLODE)
    registerMove(DAZED)
    registerMove(POKE)
  }

  override fun update() {
    super.update()
    shapes.forEachIndexed { index, list ->
      if (list.size < 10 && !(this.isDying || this.isDead)) {
        for (i in 0 until 10 - list.size) {
          list.add(when (index) {
            2 -> ShapeMonsterParticle(
              hitbox = this.hb,
              color = InfiniteSpire.PURPLE.cpy(),
              scale = 1f
            )
            1 -> ShapeMonsterParticle(
              hitbox = this.hb,
              color = InfiniteSpire.PURPLE.cpy().mul(Color.GRAY),
              scale = .9f
            )
            else -> ShapeMonsterParticle(
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
    when {
      roll < 25 -> setMove(EXPLODE)
      roll in 26..65 -> setMove(DAZED)
      else -> setMove(POKE)
    }
  }
}