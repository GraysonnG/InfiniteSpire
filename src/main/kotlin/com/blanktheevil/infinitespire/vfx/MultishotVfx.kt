package com.blanktheevil.infinitespire.vfx


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.vfx.particles.SparkParticle
import com.blanktheevil.infinitespire.vfx.particlesystems.ParticleSystem
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.MathHelper
import com.megacrit.cardcrawl.helpers.ScreenShake
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import kotlin.math.*

class MultishotVfx(
  private val sourceHb: Hitbox,
  private val targetHb: Hitbox,
  private val maxSlots: Int = 56
) : AbstractGameEffect() {
  companion object Jerry {
    val TEXTURE = Textures.vfx.get("multistrike/diamond.png").asAtlasRegion()
    val DETAIL = Textures.vfx.get("multistrike/detail.png").asAtlasRegion()
  }

  val startSlots = maxSlots.div(2)

  private enum class Phase(val duration: Float) {
    SETUP(1.25f),
    WAIT(0.2f),
    CHARGE(0.5f),
    ROTATE(999f),
    CHARGE2(0.25f),
    DELAY(0.2f),
    FIRE(1f),
    CLEANUP(0.5f)
  }

  private val startingPoints = List(startSlots) {
    Vector2(sourceHb.cX, sourceHb.cY)
  }
  private val setupPoints = List(startSlots) { index ->
    getPoint(
      Vector2(
        sourceHb.cX,
        sourceHb.cY
      ),
      250f.scale(),
      90f,
      index,
      startSlots
    )
  }
  private val setupFinishedPoints = List(startSlots) { index ->
    getPoint(
      Vector2(
        sourceHb.cX,
        sourceHb.cY
      ),
      175f.scale(),
      90f,
      index,
      startSlots
    )
  }
  private val targetPoints = List(startSlots) {
    Vector2(targetHb.cX, targetHb.cY)
  }
  private var globalOffset = 0f
  private val distOffset = 0.1f.scale()
  private var offsetAngle = 0f
  private val angleOffset = List(startSlots) {
    MathUtils.random(0f, 360f)
  }
  private val points = List(startSlots) {
    startingPoints[it].cpy()
  }
  private val angles = MutableList(startSlots) {
    0f
  }
  private val phaseEffects = MutableList(startSlots) {
    mutableListOf(
      false, // hit sounds
      false, // launch sounds
      false, // charge finished sounds
      false, // charge started sounds
      false, // setup finished sounds
      false, // setup started sounds
    )
  }
  private val particleSystems = List(maxSlots.div(2)) {
    ParticleSystem(
      numberOfParticlesPerSpawn = 4,
      shouldCreateParticle = { phase != Phase.CLEANUP },
      createNewParticle = {
        SparkParticle(
          points[it].cpy(),
          pointScale,
          color = if (MathUtils.randomBoolean(0.85f)) InfiniteSpire.PURPLE.cpy().also { it.a = .33f } else InfiniteSpire.RED.cpy()
        )
      }
    )
  }
  private var pointScale = 0f
  private var detailScale = 0f
  private var detail2Scale = 1.25f
  private var detail2AngleOffset = 0f
  private var phase = Phase.SETUP
  private val targetOnRight = targetHb.cX - sourceHb.cX > 0f
  private var startDamageDealt = false

  init {
    this.duration = phase.duration
    for (i in 0 until startSlots) {
      angles[i] = 360f.div(startSlots).times(i)
    }
    CardCrawlGame.music.fadeOutBGM()
    CardCrawlGame.screenShake.shake(
      ScreenShake.ShakeIntensity.MED,
      ScreenShake.ShakeDur.MED,
      false
    )
  }

  override fun dispose() = doNothing()

  override fun update() {
    duration -= deltaTime
    val interpPercent = phase.duration.minus(duration).div(phase.duration).clamp(0f, 1f)
    val indivMaxDuration = phase.duration.div(maxSlots.div(2))
    updateOffsets()
    points.forEachIndexed { index, it ->
      val revIndex = startSlots - 1 - index
      particleSystems[index].update()
      // make them perform the interpolation one after another
      val indivInterpPercent = indivMaxDuration
        .plus(indivMaxDuration.times(index))
        .minus(duration)
        .div(indivMaxDuration).clamp(0f, 1f)
      applyOffset(index)
      when (phase) {
        Phase.SETUP -> {
          if (indivInterpPercent > 0f && !phaseEffects[index][4]) {
            phaseEffects[index][4] = true
            playMagicSound()
          }
          pointScale = Interpolation.circleIn.apply(0f, .75f, indivInterpPercent)
          detail2Scale = Interpolation.circleOut.apply(0f, 1.25f, interpPercent)
          it.x = Interpolation.circle.apply(startingPoints[index].x, setupPoints[index].x, indivInterpPercent)
          it.y = Interpolation.circle.apply(startingPoints[index].y, setupPoints[index].y, indivInterpPercent)
        }
        Phase.WAIT -> {
          points[revIndex].x = setupPoints[revIndex].x
          points[revIndex].y = setupPoints[revIndex].y
          angles[revIndex] = getAngleToTarget(points[revIndex], sourceHb).plus(90f)
        }
        Phase.CHARGE -> {
          // fx triggers
          if (indivInterpPercent > 0f && !phaseEffects[index][3]) {
            phaseEffects[index][3] = true
            CardCrawlGame.sound.playAV("ORB_DARK_EVOKE", 1f, 0.5f)
          }

          // logic
          detailScale = Interpolation.sineIn.apply(0f, 0.5f, indivInterpPercent)
          points[revIndex].x = Interpolation.sine.apply(setupPoints[revIndex].x, setupFinishedPoints[revIndex].x, indivInterpPercent)
          points[revIndex].y = Interpolation.sine.apply(setupPoints[revIndex].y, setupFinishedPoints[revIndex].y, indivInterpPercent)
        }
        Phase.ROTATE -> {
          if (interpPercent > 0f && !phaseEffects[index][2]) {
            phaseEffects[index][2] = true
            CardCrawlGame.sound.playAV("MONSTER_COLLECTOR_DEBUFF", 2f, 0.3f)
          }
          detail2AngleOffset = Interpolation.circleOut.apply(0f, 90f, globalOffset / 360f)
          val rotatedPoint = getPoint(
            Vector2(sourceHb.cX, sourceHb.cY),
            175f.scale(),
            90f + globalOffset,
            index,
            startSlots
          )
          setupFinishedPoints[index].x = rotatedPoint.x
          setupFinishedPoints[index].y = rotatedPoint.y
          it.x = setupFinishedPoints[index].x
          it.y = setupFinishedPoints[index].y
          angles[index] = getAngleToTarget(it, sourceHb).plus(90f)
        }
        Phase.CHARGE2 -> {
          // logic
          detailScale = Interpolation.sineOut.apply(0.5f, 1f, indivInterpPercent)
          pointScale = Interpolation.circleIn.apply(.75f, 1f, indivInterpPercent)
          angles[revIndex] = Interpolation.sine.apply(
            getAngleToTarget(points[revIndex], sourceHb).plus(90f),
            getAngleToTarget(points[revIndex], targetHb).plus(90f),
            interpPercent
          )
        }
        Phase.DELAY -> {
          points[revIndex].x = setupFinishedPoints[revIndex].x
          points[revIndex].y = setupFinishedPoints[revIndex].y
          angles[revIndex] = getAngleToTarget(points[revIndex], targetHb).plus(90f)
        }
        Phase.FIRE -> {
          // fx start triggers
          if (indivInterpPercent > 0f && !phaseEffects[index][5]) {
            phaseEffects[index][5] = true
            explodeParticles(revIndex)
          }

          // generate "trails"
          fun genLineParticle() = VFXManager.generatePointAtDistanceWithAngle(
            setupFinishedPoints[revIndex].x,
            setupFinishedPoints[revIndex].y,
            MathUtils.random() * setupFinishedPoints[revIndex].dst(points[revIndex]),
            getAngleToTarget(setupFinishedPoints[revIndex], targetHb)
          )

          particleSystems[revIndex].addParticles(5, createNewParticle = {
            SparkParticle(
              genLineParticle(),
              pointScale.times(.33f),
              color = if (MathUtils.randomBoolean(0.85f)) InfiniteSpire.PURPLE.cpy().also { it.a = .33f } else InfiniteSpire.RED.cpy()
            )
          })

          // logic
          angles[revIndex] = getAngleToTarget(points[revIndex], targetHb).plus(90f)
          points[revIndex].x = Interpolation.pow2In.apply(setupFinishedPoints[revIndex].x, targetPoints[revIndex].x, indivInterpPercent)
          points[revIndex].y = Interpolation.pow2In.apply(setupFinishedPoints[revIndex].y, targetPoints[revIndex].y, indivInterpPercent)

          // fx end tiggers
          if (indivInterpPercent == 1f && !phaseEffects[index][0]) {
            explodeParticles(revIndex)
            phaseEffects[index][0] = true
            playMagicSound()
            startDamageDealt = true
          }
        }
        Phase.CLEANUP -> {
          detail2Scale = Interpolation.circleOut.apply(1.25f, 0f, interpPercent)
        }
      }
    }
    updatePhase()
  }

  private fun updateOffsets() {
    if (phase == Phase.ROTATE) globalOffset = MathHelper.fadeLerpSnap(globalOffset, 360f)
    if (globalOffset >= 359.95f) {
      globalOffset = 0f
      detail2AngleOffset = 90f
      onPhaseComplete()
    }
    offsetAngle += deltaTime
    if (offsetAngle > 360f) offsetAngle -= 360f
  }

  private fun explodeParticles(index: Int) {
    particleSystems[index].addParticles(100, createNewParticle = {
      SparkParticle(
        points[index].cpy(),
        pointScale,
        (if (targetOnRight) Vector2(
          MathUtils.random(-1f, -0.2f),
          MathUtils.random(-1f, 1f)
        )
        else Vector2(
          MathUtils.random(.2f, 1f),
          MathUtils.random(-1f, 1f)
        )).nor().scl(MathUtils.random(100f.scale(), 200f.scale())),
        color = if (MathUtils.randomBoolean()) Settings.GLOW_COLOR.cpy() else Settings.GOLD_COLOR.cpy()
      )
    })
  }

  private fun applyOffset(index: Int) {
    startingPoints[index].x = startingPoints[index].x + cos(offsetAngle + angleOffset[index]).times(distOffset)
    startingPoints[index].y = startingPoints[index].y + sin(offsetAngle + angleOffset[index]).times(distOffset)

    setupPoints[index].x = setupPoints[index].x + cos(offsetAngle + angleOffset[index]).times(distOffset)
    setupPoints[index].y = setupPoints[index].y + sin(offsetAngle + angleOffset[index]).times(distOffset)

    setupFinishedPoints[index].x = setupFinishedPoints[index].x + cos(offsetAngle + angleOffset[index]).times(distOffset)
    setupFinishedPoints[index].y = setupFinishedPoints[index].y + sin(offsetAngle + angleOffset[index]).times(distOffset)
  }

  override fun render(sb: SpriteBatch) {
    particleSystems.forEach { it.render(sb) }
    points.forEachIndexed { index, it ->
      sb.additiveMode()
      val sPoint = setupFinishedPoints[index]
      val angleToSource = getAngleToTarget(sPoint, sourceHb)
      sb.color = InfiniteSpire.RED.cpy().also { it.a = pointScale.clamp(0f, 1f) }
      sb.draw(
        DETAIL,
        sPoint.x.minus(DETAIL.halfWidth),
        sPoint.y.minus(DETAIL.halfHeight),
        DETAIL.halfWidth,
        DETAIL.halfHeight,
        DETAIL.width,
        DETAIL.height,
        detail2Scale,
        1.25f,
        angleToSource + detail2AngleOffset
      )
      sb.color = InfiniteSpire.PURPLE.cpy()
      sb.draw(
        DETAIL,
        it.x.minus(DETAIL.halfWidth),
        it.y.minus(DETAIL.halfHeight),
        DETAIL.halfWidth,
        DETAIL.halfHeight,
        DETAIL.width,
        DETAIL.height,
        detailScale,
        1f,
        angles[index]
      )
      sb.color = Color.WHITE.also { it.a = 1f }.cpy()
      sb.draw(
        TEXTURE,
        it.x.minus(TEXTURE.halfWidth),
        it.y.minus(TEXTURE.halfHeight),
        TEXTURE.halfWidth,
        TEXTURE.halfHeight,
        TEXTURE.width,
        TEXTURE.height,
        pointScale,
        pointScale,
        angles[index]
      )
      sb.normalMode()
    }
  }

  private fun updatePhase() {
    if (duration < 0f) {
      onPhaseComplete()
    }
  }

  private fun onPhaseComplete() {
    when (phase) {
      Phase.SETUP -> {
        this.phase = Phase.WAIT
        this.duration = Phase.WAIT.duration
      }
      Phase.WAIT -> {
        this.phase = Phase.CHARGE
        this.duration = Phase.CHARGE.duration
      }
      Phase.CHARGE -> {
        this.phase = Phase.ROTATE
        this.duration = Phase.ROTATE.duration
      }
      Phase.ROTATE -> {
        this.phase = Phase.CHARGE2
        this.duration = Phase.CHARGE2.duration
      }
      Phase.CHARGE2 -> {
        this.phase = Phase.DELAY
        this.duration = Phase.DELAY.duration
      }
      Phase.DELAY -> {
        this.phase = Phase.FIRE
        this.duration = Phase.FIRE.duration
        CardCrawlGame.music.changeBGM(AbstractDungeon.id)
        CardCrawlGame.screenShake.shake(
          ScreenShake.ShakeIntensity.MED,
          ScreenShake.ShakeDur.MED,
          false
        )
      }
      Phase.FIRE -> {
        this.phase = Phase.CLEANUP
        this.duration = Phase.CLEANUP.duration
      }
      Phase.CLEANUP -> {
        duration = 0f
        isDone = true
      }
    }
  }

  private fun getPoint(position: Vector2, distance: Float, offset: Float, index: Int, max: Int): Vector2 {
    val angle = 360f.div(max.toFloat()).times(index).plus(offset)
    return VFXManager.generatePointAtDistanceWithAngle(position.x, position.y, distance, angle)
  }

  private fun getAngleToTarget(point: Vector2, target: Hitbox) = atan2(
    target.cY.minus(point.y).toDouble(),
    target.cX.minus(point.x).toDouble()
  ).times(180f.div(PI)).toFloat()

  private fun playMagicSound() {
    CardCrawlGame.sound.playA(when (MathUtils.random(0f, 2f).roundToInt()) {
      0 -> "ATTACK_MAGIC_FAST_1"
      1 -> "ATTACK_MAGIC_FAST_2"
      else -> "ATTACK_MAGIC_FAST_3"
    }, 1.5f)
  }

  fun shouldStartDamageAction(): Boolean {
    val output = startDamageDealt
    startDamageDealt = false
    return output
  }
}