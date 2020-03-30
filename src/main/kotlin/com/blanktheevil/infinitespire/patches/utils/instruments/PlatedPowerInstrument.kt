package com.blanktheevil.infinitespire.patches.utils.instruments

import com.blanktheevil.infinitespire.patches.abstractpower.PlatedPowerFieldPatch
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.powers.AbstractPower
import javassist.expr.ExprEditor
import javassist.expr.NewExpr

class PlatedPowerInstrument : ExprEditor() {
  override fun edit(e: NewExpr) {
    if (e.className == RemoveSpecificPowerAction::class.java.name) {
      e.replace("{" +
          "if(${PlatedPowerInstrument::class.java.name}.spireFieldToBoolean(this)) {" +
            "${'$'}_=new ${RemoveSpecificPowerAction::class.java.name}($1,$2,\"\");" +
          "} else {" +
            "${'$'}_=${'$'}proceed($$);"+
          "}"+
        "}")
    }

    if (e.className == ReducePowerAction::class.java.name) {
      e.replace("{" +
          "if(${PlatedPowerInstrument::class.java.name}.spireFieldToBoolean(this)) {" +
            "${'$'}_=new ${ReducePowerAction::class.java.name}($1,$2,\"\",0);" +
          "} else {" +
            "${'$'}_=${'$'}proceed($$);"+
          "}"+
        "}")
    }
  }

  companion object {
    @JvmStatic
    @Suppress("unused")
    fun spireFieldToBoolean(obj: AbstractPower): Boolean = PlatedPowerFieldPatch.isPlatedPower.get(obj) as Boolean
  }
}