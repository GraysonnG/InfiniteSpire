package com.blanktheevil.infinitespire.utils

import basemod.BaseMod
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.models.ActStringsKt
import com.blanktheevil.infinitespire.models.BadgeStringsKt
import com.blanktheevil.infinitespire.models.CardStringsKt
import com.google.gson.reflect.TypeToken
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.localization.*

object LocalizationManager {
  fun init() {
    loadFiles(Settings.GameLanguage.ENG)
    if (Settings.language != Settings.GameLanguage.ENG) {
      loadFiles(Settings.language)
    }
  }

  private fun loadFiles(language: Settings.GameLanguage) {
    InfiniteSpire.cardStringsKt = JSONHelper.readFileAsMapOf(
      makePath(language, "cards"),
      object : TypeToken<Map<String, CardStringsKt>>() {}
    )

    InfiniteSpire.actStringsKt = JSONHelper.readFileAsMapOf(
      makePath(language, "acts"),
      object : TypeToken<Map<String, ActStringsKt>>() {}
    )

    InfiniteSpire.badgeStringsKt = JSONHelper.readFileAsMapOf(
      makePath(language, "badges"),
      object : TypeToken<Map<String, BadgeStringsKt>>() {}
    )

    BaseMod.loadCustomStringsFile(StanceStrings::class.java, makePath(language, "stances"))
    BaseMod.loadCustomStringsFile(RelicStrings::class.java, makePath(language, "relics"))
    BaseMod.loadCustomStringsFile(BlightStrings::class.java, makePath(language, "blights"))
    BaseMod.loadCustomStringsFile(EventStrings::class.java, makePath(language, "events"))
    BaseMod.loadCustomStringsFile(MonsterStrings::class.java, makePath(language, "monsters"))
    BaseMod.loadCustomStringsFile(PotionStrings::class.java, makePath(language, "potions"))
    BaseMod.loadCustomStringsFile(PowerStrings::class.java, makePath(language, "powers"))
    BaseMod.loadCustomStringsFile(UIStrings::class.java, makePath(language, "ui"))
  }

  private fun makePath(language: Settings.GameLanguage, fileName: String): String {
    var langFolder = "${InfiniteSpire.modid}/local/"

    @Suppress("LiftReturnOrAssignment")
    when (language) {
      Settings.GameLanguage.ENG -> langFolder += "eng"
      else -> langFolder += "eng"
    }
    return "$langFolder/$fileName.json"
  }
}
