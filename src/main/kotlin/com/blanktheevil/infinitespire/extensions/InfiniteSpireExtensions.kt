package com.blanktheevil.infinitespire.extensions

import com.blanktheevil.infinitespire.InfiniteSpire
import org.apache.logging.log4j.Logger

fun addVoidShard(count: Int) {
  InfiniteSpire.config.voidShards.count += count
  InfiniteSpire.voidShardDisplay.flash()
}

val log: Logger get() = InfiniteSpire.logger