package com.blanktheevil.infinitespire

import basemod.AutoAdd
import basemod.BaseMod
import basemod.interfaces.EditCardsSubscriber
import basemod.interfaces.EditStringsSubscriber
import basemod.interfaces.PostInitializeSubscriber
import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.cards.BlackCard
import com.blanktheevil.infinitespire.crossover.CrossoverManager
import com.blanktheevil.infinitespire.crossover.Crossovers
import com.blanktheevil.infinitespire.interfaces.InfiniteSpireI
import com.blanktheevil.infinitespire.interfaces.Savable
import com.blanktheevil.infinitespire.models.CardStringsKt
import com.blanktheevil.infinitespire.models.Config
import com.blanktheevil.infinitespire.models.QuestLog
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.blanktheevil.infinitespire.screens.AvhariScreen
import com.blanktheevil.infinitespire.screens.QuestLogScreen
import com.blanktheevil.infinitespire.toppanel.QuestLogButton
import com.blanktheevil.infinitespire.toppanel.VoidShardDisplay
import com.blanktheevil.infinitespire.utils.Localization
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.util.*

@SpireInitializer
class InfiniteSpire : PostInitializeSubscriber, EditCardsSubscriber, EditStringsSubscriber {
  companion object {
    val logger: Logger = LogManager.getLogger(InfiniteSpire::class.java.name)
    val PURPLE: Color = Color.valueOf("#3D00D6")
    val RED: Color = Color.valueOf("#FF4A4A")

    lateinit var name: String
    lateinit var version: String
    lateinit var modid: String
    lateinit var author: String
    lateinit var description: String


    val questLog = QuestLog(true)
    lateinit var questLogButton: QuestLogButton
    lateinit var questLogScreen: QuestLogScreen
    lateinit var avhariScreen: AvhariScreen
    lateinit var voidShardDisplay: VoidShardDisplay
    lateinit var config: Config

    lateinit var cardStringsKt: Map<String, CardStringsKt>

    @Suppress("unused")
    @JvmStatic
    fun initialize() {
      loadProperties()
      Config.init()
      BaseMod.subscribe(InfiniteSpire())
      // init infinite spire settings menu
      BaseMod.addColor(
        EnumPatches.CardColor.INFINITE_BLACK,
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Textures.cards.getString("ui/512/boss-attack.png"),
        Textures.cards.getString("ui/512/boss-skill.png"),
        Textures.cards.getString("ui/512/boss-power.png"),
        Textures.cards.getString("ui/512/boss-orb.png"),
        Textures.cards.getString("ui/1024/boss-attack.png"),
        Textures.cards.getString("ui/1024/boss-skill.png"),
        Textures.cards.getString("ui/1024/boss-power.png"),
        Textures.cards.getString("ui/1024/boss-orb.png")
      )


      Crossovers.init()
    }

    @JvmStatic
    fun <T : InfiniteSpireI> subscribe(subscriber: T) {
      if (subscriber is Savable) {
        Savable.savables.add(subscriber)
      }
    }

    fun createPath(restOfPath: String): String {
      return "img/infinitespire/$restOfPath"
    }

    fun loadProperties() {
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

  override fun receiveEditStrings() {
    Localization.loadFiles(Settings.GameLanguage.ENG)
    if (Settings.language != Settings.GameLanguage.ENG) {
      Localization.loadFiles(Settings.language)
    }
  }

  override fun receiveEditCards() {
    AutoAdd("infinitespire")
      .packageFilter(BlackCard::class.java)
      .cards()
  }

  override fun receivePostInitialize() {
    questLogButton = QuestLogButton()
    questLogScreen = QuestLogScreen(questLog, questLogButton)
    avhariScreen = AvhariScreen()
    voidShardDisplay = VoidShardDisplay()

    BaseMod.addTopPanelItem(questLogButton)
    BaseMod.addTopPanelItem(voidShardDisplay)
    // some stuff
    CrossoverManager.addCrossoverContent()
  }
}