package com.blanktheevil.infinitespire.vfx.particles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.enums.ShapeType
import com.blanktheevil.infinitespire.extensions.clamp
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.normalMode
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.esotericsoftware.spine.*
import com.megacrit.cardcrawl.core.AbstractCreature.sr
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.Hitbox

class ShapeMonsterParticle(
  private val shapeType: ShapeType = VFXManager.getRandomShapeType(),
  x: Float = Settings.WIDTH.div(2f),
  y: Float = Settings.WIDTH.div(2f),
  dist: Float = 300f,
  val color: Color = Color.WHITE.cpy(),
  val scale: Float = 1f
) : Particle() {
  constructor(
    shapeType: ShapeType = VFXManager.getRandomShapeType(),
    hitbox: Hitbox,
    color: Color,
    scale: Float
  ) : this(shapeType, hitbox.cX, hitbox.cY, hitbox.height.div(2), color, scale)

  private val pos: Vector2
  private var vel = Vector2(0f, 0f)
  private lateinit var atlas: TextureAtlas
  private lateinit var stateData: AnimationStateData
  private lateinit var state: AnimationState
  private lateinit var skeleton: Skeleton
  private var rotation = MathUtils.random(0f, 359f)
  private val rotationSpeed = MathUtils.random(-15f, 15f)
  private var lifeSpan = MathUtils.random(5f, 25f)
  private val maxLife = lifeSpan

  init {
    val randomDist = MathUtils.random(0f, dist)
    pos = VFXManager.generateRandomPointAlongEdgeOfCircle(x, y, randomDist)
    val textureName = when (shapeType) {
      ShapeType.EXPLODER -> "exploder"
      ShapeType.SPIKER -> "spiker"
      ShapeType.REPULSER -> "repulser"
    }
    initShapeData(textureName)
    skeleton.rootBone.rotation = rotation

    state.setAnimation(0, "idle", true).also {
      it.time = it.endTime * MathUtils.random()
    }
  }

  private fun initShapeData(name: String) {
    atlas = getAtlas(name)
    val json = SkeletonJson(this.atlas)
    json.scale = Settings.scale.div(1)
    val skeletonData = json.readSkeletonData(Gdx.files.internal("images/monsters/theForest/${name}/skeleton.json"))
    skeleton = Skeleton(skeletonData)
    skeleton.color = color
    stateData = AnimationStateData(skeletonData)
    state = AnimationState(stateData)
  }

  private fun getAtlas(name: String) =
    TextureAtlas(Gdx.files.internal("images/monsters/theForest/${name}/skeleton.atlas"))

  override fun update() {
    rotation += rotationSpeed.times(deltaTime)

    lifeSpan -= deltaTime
    lifeSpan = lifeSpan.clamp(0f, maxLife)

    state.update(deltaTime)
    state.apply(skeleton)
    pos.add(vel.scl(deltaTime).scl(Settings.scale))
    skeleton.setPosition(pos.x, pos.y)
    skeleton.rootBone.rotation = rotation
    skeleton.color = this.color

    val lifeDuration = maxLife.minus(lifeSpan)

    val scaleInterp = when {
      lifeDuration < maxLife.div(6) ->
        Interpolation.pow5In.apply(0f, 1f, (lifeDuration.times(6).clamp(0f, maxLife)).div(maxLife))
      lifeDuration > maxLife.div(6).times(5) ->
        Interpolation.pow5Out.apply(0f, 1f, (lifeSpan.times(6).clamp(0f, maxLife)).div(maxLife))
      else ->
        1f
    }

    val finalScale = scaleInterp.toDouble().times(scale.toDouble()).toFloat()

    scaleBone(skeleton.rootBone, finalScale)
    skeleton.setFlip(false, false)
    skeleton.findBone("shadow").setScale(0f)
    skeleton.updateWorldTransform()
  }

  fun setDying() {
    val dyingNum = maxLife.div(6f)
    if (lifeSpan > dyingNum)
      this.lifeSpan = dyingNum
  }

  private fun scaleBone(bone: Bone, scale: Float) {
    bone.setScale(scale)
    if (bone.children.size > 0) {
      bone.children.forEach {
        scaleBone(it, scale)
      }
    }
  }

  override fun render(sb: SpriteBatch) {
    sb.end()
    CardCrawlGame.psb.begin()
    sr.draw(CardCrawlGame.psb, skeleton)
    CardCrawlGame.psb.end()
    sb.begin()
    sb.normalMode()
  }

  override fun isDead() = lifeSpan <= 0.0f
}