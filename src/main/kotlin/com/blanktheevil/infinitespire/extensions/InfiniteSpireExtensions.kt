package com.blanktheevil.infinitespire.extensions

import com.blanktheevil.infinitespire.InfiniteSpire

fun addVoidShard(count: Int) {
  InfiniteSpire.config.voidShards += count
  InfiniteSpire.voidShardDisplay.flash()
}