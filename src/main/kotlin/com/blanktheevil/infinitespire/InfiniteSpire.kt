package com.blanktheevil.infinitespire

import basemod.AutoAdd
import basemod.BaseMod
import basemod.interfaces.EditCardsSubscriber
import basemod.interfaces.EditRelicsSubscriber
import basemod.interfaces.EditStringsSubscriber
import basemod.interfaces.PostInitializeSubscriber
import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.cards.Card
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.crossover.CrossoverManager
import com.blanktheevil.infinitespire.crossover.Crossovers
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.interfaces.*
import com.blanktheevil.infinitespire.models.CardStringsKt
import com.blanktheevil.infinitespire.models.SaveData
import com.blanktheevil.infinitespire.models.QuestLog
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.blanktheevil.infinitespire.quests.IgnoreRelicQuest
import com.blanktheevil.infinitespire.quests.Quest
import com.blanktheevil.infinitespire.quests.questrewards.QuestReward
import com.blanktheevil.infinitespire.relics.Relic
import com.blanktheevil.infinitespire.screens.*
import com.blanktheevil.infinitespire.toppanel.QuestLogButton
import com.blanktheevil.infinitespire.toppanel.VoidShardDisplay
import com.blanktheevil.infinitespire.utils.CardHelper
import com.blanktheevil.infinitespire.utils.Localization
import com.blanktheevil.infinitespire.utils.CardBuilder
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.unlock.UnlockTracker
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
    lateinit var infiniteScreen: InfiniteScreen
    lateinit var voidShardDisplay: VoidShardDisplay
    lateinit var saveData: SaveData
    lateinit var cardStringsKt: Map<String, CardStringsKt>

    @Suppress("unused")
    @JvmStatic
    fun initialize() {
      loadProperties()
      saveData = SaveData.init()
      BaseMod.subscribe(InfiniteSpire())
      // init infinite spire settings menu
      addBlackCardColor()
      Crossovers.init()
    }

    @JvmStatic
    fun <T : IInfiniteSpire> subscribe(subscriber: T) {
      if (subscriber is Savable) {
        Savable.savables.add(subscriber)
      }

      if (subscriber is ActCompleteInterface) {
        ActCompleteInterface.subscribers.add(subscriber)
      }

      if (subscriber is QuestLogCloseInterface) {
        QuestLogCloseInterface.subscribers.add(subscriber)
      }

      if (subscriber is QuestCompleteInterface) {
        QuestCompleteInterface.subscribers.add(subscriber)
      }

      if (subscriber is RoomTransitionInterface) {
        RoomTransitionInterface.subscribers.add(subscriber)
      }
    }

    @JvmStatic
    fun addBlackCardColor() {
      BaseMod.addColor(
        EnumPatches.CardColor.INFINITE_BLACK,
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Textures.cards.getString("ui/512/boss-attack.png", true),
        Textures.cards.getString("ui/512/boss-skill.png", true),
        Textures.cards.getString("ui/512/boss-power.png", true),
        Textures.cards.getString("ui/512/boss-orb.png", true),
        Textures.cards.getString("ui/1024/boss-attack.png", true),
        Textures.cards.getString("ui/1024/boss-skill.png", true),
        Textures.cards.getString("ui/1024/boss-power.png", true),
        Textures.cards.getString("ui/1024/boss-orb.png", true)
      )
    }

    fun createPath(restOfPath: String): String {
      return "img/infinitespire/$restOfPath"
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

  override fun receiveEditStrings() {
    Localization.loadFiles(Settings.GameLanguage.ENG)
    if (Settings.language != Settings.GameLanguage.ENG) {
      Localization.loadFiles(Settings.language)
    }
  }

  override fun receiveEditCards() {
    AutoAdd(modid)
      .packageFilter(Card::class.java)
      .any(Card::class.java) { info, card ->
        logger.info("Added Card: ${card.cardID}")
        CardHelper.addCard(card)
        if (info.seen) {
          UnlockTracker.markCardAsSeen(card.cardID)
        }
      }
  }

  override fun receiveEditRelics() {
    AutoAdd(modid)
      .packageFilter(Relic::class.java)
      .any(Relic::class.java) { info, relic ->
        logger.info("Added Relic: ${relic.relicId}")
        RelicLibrary.add(relic)
        if (info.seen) {
          UnlockTracker.markRelicAsSeen(relic.relicId)
        }
      }
  }

  override fun receivePostInitialize() {
    addQuests()

    questLogButton = QuestLogButton()
    questLogScreen = QuestLogScreen(questLog, questLogButton)
    avhariScreen = AvhariScreen()
    voidShardDisplay = VoidShardDisplay()
    powerSelectScreen = PowerSelectScreen()
    targetMonsterScreen = TargetMonsterScreen()
    infiniteScreen = InfiniteScreen()

    BaseMod.addTopPanelItem(questLogButton)
    BaseMod.addTopPanelItem(voidShardDisplay)
    // some stuff
    CrossoverManager.addCrossoverContent()
  }

  lateinit var livingQuest: Quest

  private fun addQuests() {
    AutoAdd(modid)
      .packageFilter(QuestReward::class.java)
      .any(QuestReward::class.java) { _, rewardType ->
        with (rewardType) {
          logger.info("Added Quest Reward Type: $id")
          QuestReward.questRewardTypes[id] = this
        }
      }
    livingQuest = IgnoreRelicQuest()
  }
}