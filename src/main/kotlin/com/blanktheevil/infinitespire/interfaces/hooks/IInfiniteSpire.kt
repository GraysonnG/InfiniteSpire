package com.blanktheevil.infinitespire.interfaces.hooks

import com.blanktheevil.infinitespire.InfiniteSpire

interface IInfiniteSpire {
  fun subscribe() = InfiniteSpire.subscribe(this)
  fun unsubscribe() = InfiniteSpire.unsubscribe(this)
}