package com.blanktheevil.infinitespire.interfaces

import com.megacrit.cardcrawl.rooms.AbstractRoom

interface RoomTransitionInterface : IInfiniteSpire {
  fun onRoomTransition(previousRoom: AbstractRoom, nextRoom: AbstractRoom)

  companion object {
    val subscribers = mutableListOf<RoomTransitionInterface>()
  }
}