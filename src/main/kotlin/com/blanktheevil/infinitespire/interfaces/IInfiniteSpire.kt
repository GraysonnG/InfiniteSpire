package com.blanktheevil.infinitespire.interfaces

import com.blanktheevil.infinitespire.InfiniteSpire

interface IInfiniteSpire {
  fun subscribe() = InfiniteSpire.subscribe(this)
  fun unsubscribe() = InfiniteSpire.unsubscribe(this)
}