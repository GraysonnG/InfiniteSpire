package com.blanktheevil.infinitespire

import basemod.BaseMod
import basemod.interfaces.EditCardsSubscriber
import basemod.interfaces.EditRelicsSubscriber
import basemod.interfaces.EditStringsSubscriber
import basemod.interfaces.PostInitializeSubscriber
import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.acts.utils.ActManager
import com.blanktheevil.infinitespire.cards.utils.CardManager
import com.blanktheevil.infinitespire.crossover.utils.CrossoverManager
import com.blanktheevil.infinitespire.interfaces.IInfiniteSpire
import com.blanktheevil.infinitespire.interfaces.utils.SubscriberManager
import com.blanktheevil.infinitespire.models.ActStringsKt
import com.blanktheevil.infinitespire.models.CardStringsKt
import com.blanktheevil.infinitespire.models.QuestLog
import com.blanktheevil.infinitespire.models.SaveData
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.blanktheevil.infinitespire.quests.utils.QuestManager
import com.blanktheevil.infinitespire.relics.utils.RelicManager
import com.blanktheevil.infinitespire.rewards.EndlessUnlockReward
import com.blanktheevil.infinitespire.rewards.InterestReward
import com.blanktheevil.infinitespire.rewards.VoidShardReward
import com.blanktheevil.infinitespire.rewards.utils.RewardManager
import com.blanktheevil.infinitespire.screens.AvhariScreen
import com.blanktheevil.infinitespire.screens.PowerSelectScreen
import com.blanktheevil.infinitespire.screens.QuestLogScreen
import com.blanktheevil.infinitespire.screens.TargetMonsterScreen
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.toppanel.QuestLogButton
import com.blanktheevil.infinitespire.toppanel.VoidShardDisplay
import com.blanktheevil.infinitespire.ui.campfire.VoidOption
import com.blanktheevil.infinitespire.utils.LocalizationManager
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.rewards.RewardSave
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.util.*

@SpireInitializer
class InfiniteSpire : PostInitializeSubscriber, EditCardsSubscriber, EditStringsSubscriber, EditRelicsSubscriber {
  companion object {
    val logger: Logger = LogManager.getLogger(InfiniteSpire::class.java.name)
    val PURPLE: Color = Color.valueOf("#3D00D6")
    val RED: Color = Color.valueOf("#FF4A4A")
    val questLog = QuestLog(true)

    lateinit var name: String
    lateinit var version: String
    lateinit var modid: String
    lateinit var author: String
    lateinit var description: String
    lateinit var questLogButton: QuestLogButton
    lateinit var questLogScreen: QuestLogScreen
    lateinit var avhariScreen: AvhariScreen
    lateinit var powerSelectScreen: PowerSelectScreen
    lateinit var targetMonsterScreen: TargetMonsterScreen
    lateinit var voidShardDisplay: VoidShardDisplay
    lateinit var voidOption: VoidOption
    lateinit var saveData: SaveData
    lateinit var cardStringsKt: Map<String, CardStringsKt>
    lateinit var actStringsKt: Map<String, ActStringsKt>

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
  private fun addQuests() = QuestManager.addAllQuests()

  override fun receivePostInitialize() {
    addQuests()

    questLogButton = QuestLogButton()
    questLogScreen = QuestLogScreen(questLog, questLogButton)
    avhariScreen = AvhariScreen()
    voidShardDisplay = VoidShardDisplay()
    powerSelectScreen = PowerSelectScreen()
    targetMonsterScreen = TargetMonsterScreen()
    voidOption = VoidOption()

    RewardManager.registerRewards()

    BaseMod.addTopPanelItem(questLogButton)
    BaseMod.addTopPanelItem(voidShardDisplay)
    // some stuff
    CrossoverManager.addCrossoverContent()
    ActManager.init()
  }
}