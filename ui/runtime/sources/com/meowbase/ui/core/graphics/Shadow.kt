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

package com.meowbase.ui.core.graphics

import android.graphics.Bitmap
import androidx.annotation.FloatRange
import com.meowbase.ui.core.unit.SizeUnit

/*
 * author: 凛
 * date: 2020/9/28 下午5:58
 * github: https://github.com/RinOrz
 * description: 定义一个阴影数据
 */
data class Shadow(
  val color: Color,
  val y: SizeUnit,
  val x: SizeUnit,
  val spread: SizeUnit,
  @FloatRange(from = .0, to = 1.0) val alpha: Float,
) {
  constructor(
    color: Color,
    spread: SizeUnit,
    y: SizeUnit,
    x: SizeUnit,
    blur: Float,
    @FloatRange(from = .0, to = 1.0) alpha: Float,
  ) : this(color, y, x, spread, alpha) {
    this.blur = blur
  }

  /** 阴影半径 */
  @FloatRange(from = .0, to = 1.0) var blur: Float = DefaultBlur

  /** 渲染后的阴影位图 */
  var bitmap: Bitmap? = null

  companion object {
    const val DefaultBlur = 0.4f
  }
}