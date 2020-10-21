package com.blanktheevil.infinitespire.patches.platedpower

import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.patches.utils.filters.StsClassFilter
import com.blanktheevil.infinitespire.patches.utils.instruments.PlatedPowerInstrument
import com.blanktheevil.infinitespire.patches.utils.locators.ReducePowerLocator
import com.blanktheevil.infinitespire.patches.utils.locators.RemovePowerLocator
import com.evacipated.cardcrawl.modthespire.Loader
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch
import com.megacrit.cardcrawl.powers.AbstractPower
import javassist.CtBehavior
import javassist.Modifier
import org.clapper.util.classutil.*
import java.io.File
import java.net.URISyntaxException
import java.util.*

@Suppress("unused")
@SpirePatch(clz = AbstractPower::class, method = SpirePatch.CONSTRUCTOR)
object PlatedPowerSmartPatch {
  @Suppress("unused")
  @JvmStatic
  @SpireRawPatch
  fun platedPowerSmartPatch(ctBehavior: CtBehavior) {
    println("\nInfinite Spire: Smart Plated Power Patch")

    val foundClasses = findClasses()

    println("\t- Done Finding Classes...\n\t- Begin Patching...")
    var cInfo: ClassInfo? = null
    try {
      foundClasses.stream()
        .map {
          cInfo = it
          ctBehavior.declaringClass.classPool.get(it.className)
        }
        .forEach { ctClass ->
          ctClass.declaredMethods.asList().stream()
            .filter {
              it != null && (
                RemovePowerLocator().Locate(it).isNotEmpty() ||
                  ReducePowerLocator().Locate(it).isNotEmpty()
                )
            }
            .forEach {
              it.instrument(PlatedPowerInstrument())
            }
        }
    } catch (e: Exception) {
      println("\t- Failed to Patch Classes!")
      e.printStackTrace()
    }
    println("\t- Done Patching...")
  }

  private fun findClasses(): List<ClassInfo> {
    val filter = AndClassFilter(
      NotClassFilter(InterfaceOnlyClassFilter()),
      NotClassFilter(AbstractClassFilter()),
      ClassModifiersClassFilter(Modifier.PUBLIC),
      StsClassFilter(AbstractPower::class.java)
    )

    val finder = ClassFinder()
    finder.add(File(Loader.STS_JAR))
    Loader.MODINFOS.asList().stream()
      .filter { it.jarURL != null }
      .forEach {
        try {
          finder.add(File(it.jarURL.toURI()))
        } catch (e: URISyntaxException) {
          doNothing()
        }
      }

    return ArrayList<ClassInfo>().also {
      finder.findClasses(it, filter)
    }
  }
}