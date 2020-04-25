package com.blanktheevil.infinitespire.extensions

import com.blanktheevil.infinitespire.InfiniteSpire
import org.apache.logging.log4j.Logger
import java.lang.Math.PI

fun addVoidShard(count: Int) {
  InfiniteSpire.saveData.voidShards.count += count
  InfiniteSpire.voidShardDisplay.flash()
}

fun Float.toRadians(): Float = this.times(PI.div(180f)).toFloat()

val log: Logger get() = InfiniteSpire.logger