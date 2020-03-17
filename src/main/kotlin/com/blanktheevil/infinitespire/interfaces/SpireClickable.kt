package com.blanktheevil.infinitespire.interfaces

import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.input.InputHelper

interface SpireClickable {
  fun getHitbox(): Hitbox

  fun leftClicked(): Boolean = InputHelper.justClickedLeft && getHitbox().hovered
  fun rightClicked(): Boolean = InputHelper.justClickedRight && getHitbox().hovered
  fun leftMouseDown(): Boolean = InputHelper.isMouseDown && getHitbox().hovered
  fun rightMouseDown(): Boolean = InputHelper.isMouseDown_R && getHitbox().hovered
}