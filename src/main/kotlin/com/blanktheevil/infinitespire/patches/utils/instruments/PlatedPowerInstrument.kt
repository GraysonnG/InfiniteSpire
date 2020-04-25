package com.blanktheevil.infinitespire.patches.utils.instruments

import com.blanktheevil.infinitespire.patches.platedpower.PlatedPowerFieldPatch
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.powers.AbstractPower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr

class PlatedPowerInstrument : ExprEditor() {
  override fun edit(e: NewExpr) {
    when (e.className) {
      RemoveSpecificPowerAction::class.java.name ->
        getReplaceString(
          e.className,
          "(\$1,\$2,\"\")"
        )
      ReducePowerAction::class.java.name ->
        getReplaceString(
          e.className,
          "(\$1,\$2,\"\",0)"
        )
      else ->
        ""
    }.also {
      if (it.isNotEmpty()) e.replace(it)
    }
  }

  private fun getReplaceString(className: String, args: String): String =
    "{" +
        "if($spireFieldBooleanString) {" +
        "\$_=new $className$args;" +
        "} else {" +
        "\$_=\$proceed(\$\$);" +
        "}" +
        "}"

  private val spireFieldBooleanString: String =
    "${PlatedPowerInstrument::class.java.name}.spireFieldToBoolean(this)"

  companion object {
    @JvmStatic
    @Suppress("unused")
    fun spireFieldToBoolean(obj: AbstractPower): Boolean =
      PlatedPowerFieldPatch.isPlatedPower.get(obj) as Boolean
  }
}