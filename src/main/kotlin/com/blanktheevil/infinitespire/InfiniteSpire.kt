package com.blanktheevil.infinitespire

import basemod.BaseMod
import basemod.interfaces.*
import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.acts.utils.ActManager
import com.blanktheevil.infinitespire.badges.utils.BadgeManager
import com.blanktheevil.infinitespire.cards.utils.CardManager
import com.blanktheevil.infinitespire.crossover.utils.CrossoverManager
import com.blanktheevil.infinitespire.interfaces.hooks.IInfiniteSpire
import com.blanktheevil.infinitespire.interfaces.utils.SubscriberManager
import com.blanktheevil.infinitespire.models.*
import com.blanktheevil.infinitespire.relics.utils.RelicManager
import com.blanktheevil.infinitespire.rewards.utils.RewardManager
import com.blanktheevil.infinitespire.screens.AvhariScreen
import com.blanktheevil.infinitespire.screens.PowerSelectScreen
import com.blanktheevil.infinitespire.screens.TargetMonsterScreen
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.toppanel.VoidShardDisplay
import com.blanktheevil.infinitespire.ui.campfire.VoidOption
import com.blanktheevil.infinitespire.ui.overlays.BadgeOverlay
import com.blanktheevil.infinitespire.utils.DebugControls
import com.blanktheevil.infinitespire.utils.LocalizationManager
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.random.Random
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.util.*

@SpireInitializer
class InfiniteSpire : PostInitializeSubscriber, EditCardsSubscriber, EditStringsSubscriber, EditRelicsSubscriber, PreStartGameSubscriber {
  companion object {

    val logger: Logger = LogManager.getLogger(InfiniteSpire::class.java.name)
    val PURPLE: Color = Color.valueOf("#3D00D6")
    val RED: Color = Color.valueOf("#FF4A4A")

    lateinit var name: String
    lateinit var version: String
    lateinit var modid: String
    lateinit var author: String
    lateinit var description: String
    lateinit var avhariScreen: AvhariScreen
    lateinit var powerSelectScreen: PowerSelectScreen
    lateinit var targetMonsterScreen: TargetMonsterScreen
    lateinit var badgeOverlay: BadgeOverlay
    lateinit var voidShardDisplay: VoidShardDisplay
    lateinit var voidOption: VoidOption
    lateinit var debugControls: DebugControls
    lateinit var saveData: SaveData
    lateinit var cardStringsKt: Map<String, CardStringsKt>
    lateinit var actStringsKt: Map<String, ActStringsKt>
    lateinit var badgeStringsKt: Map<String, BadgeStringsKt>
    lateinit var questRng: Random

    var pauseGame = false

    @Suppress("unused")
    @JvmStatic
    fun initialize() {
      Settings.isDebug = true
      loadProperties()
      Textures.missingTexturePath = Textures.ui.getString("missingtexture.png", true)
      saveData = SaveData.init()
      BaseMod.subscribe(InfiniteSpire())
      // init infinite spire settings menu
      CardManager.addBlackCardColor()
      CrossoverManager.init()
    }

    @JvmStatic
    fun <T : IInfiniteSpire> subscribe(subscriber: T) {
      SubscriberManager.subscribe(subscriber)
    }

    fun <T : IInfiniteSpire> unsubscribe(subscriber: T) {
      SubscriberManager.unsubscribe(subscriber)
    }

    fun createPath(restOfPath: String): String {
      return "$modid/images/$restOfPath"
    }

    private fun loadProperties() {
      try {
        Properties().apply {
          this.load(InfiniteSpire::class.java.getResourceAsStream("/META-INF/infinite-spire.prop"))
          name = getProperty("name")
          version = getProperty("version")
          modid = getProperty("id")
          author = getProperty("author")
          description = getProperty("description")
        }
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

  override fun receiveEditStrings() = LocalizationManager.init()
  override fun receiveEditCards() = CardManager.addAllCards()
  override fun receiveEditRelics() = RelicManager.addAllRelics()

  override fun receivePostInitialize() {
    BadgeManager.addAllBadges()
    avhariScreen = AvhariScreen()
    voidShardDisplay = VoidShardDisplay()
    powerSelectScreen = PowerSelectScreen()
    targetMonsterScreen = TargetMonsterScreen()
    badgeOverlay = BadgeOverlay()
    voidOption = VoidOption()
    debugControls = DebugControls()

    RewardManager.registerRewards()

    BaseMod.addTopPanelItem(voidShardDisplay)
    CrossoverManager.addCrossoverContent()
    ActManager.init()
  }

  override fun receivePreStartGame() {
    val seed = Settings.seed ?: 0
    questRng = Random(seed)
  }
}