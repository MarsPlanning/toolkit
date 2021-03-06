/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

@file:Suppress("NOTHING_TO_INLINE")

package com.meowbase.ui.core.graphics.geometry

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.RectF

inline fun Path.addRoundRect(
  rectF: RectF,
  topLeftCornerRadius: CornerRadius,
  topRightCornerRadius: CornerRadius,
  bottomRightCornerRadius: CornerRadius,
  bottomLeftCornerRadius: CornerRadius,
) {
  val radii = FloatArray(8)
  radii[0] = topLeftCornerRadius.x
  radii[1] = topLeftCornerRadius.y

  radii[2] = topRightCornerRadius.x
  radii[3] = topRightCornerRadius.y

  radii[4] = bottomRightCornerRadius.x
  radii[5] = bottomRightCornerRadius.y

  radii[6] = bottomLeftCornerRadius.x
  radii[7] = bottomLeftCornerRadius.y
  addRoundRect(rectF, radii, Path.Direction.CCW)
}

inline fun Path.addRoundRect(roundRect: RoundRect) {
  val radii = FloatArray(8)
  val rectF = RectF()
  rectF.set(roundRect.left, roundRect.top, roundRect.right, roundRect.bottom)
  radii[0] = roundRect.topLeftCornerRadius.x
  radii[1] = roundRect.topLeftCornerRadius.y

  radii[2] = roundRect.topRightCornerRadius.x
  radii[3] = roundRect.topRightCornerRadius.y

  radii[4] = roundRect.bottomRightCornerRadius.x
  radii[5] = roundRect.bottomRightCornerRadius.y

  radii[6] = roundRect.bottomLeftCornerRadius.x
  radii[7] = roundRect.bottomLeftCornerRadius.y
  addRoundRect(rectF, radii, Path.Direction.CCW)
}

inline fun Path.addPath(path: Path, offset: Offset) =
  addPath(path, offset.x, offset.y)

inline fun Path.translate(offset: Offset) = Matrix().let {
  it.reset()
  it.setTranslate(offset.x, offset.y)
  transform(it)
}
