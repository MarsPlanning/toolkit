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

@file:Suppress("OverridingDeprecatedMember")

package com.meowbase.ui.widget.modifier

import android.view.View
import android.view.ViewGroup
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.toIntPxOrNull

/** 单独调整 View 的四个方向的内边距 */
fun Modifier.padding(
  start: SizeUnit? = null,
  top: SizeUnit? = null,
  end: SizeUnit? = null,
  bottom: SizeUnit? = null,
) = +PaddingModifier(start, top, end, bottom)

/** 调整 View 的横纵向内边距 */
fun Modifier.padding(
  horizontal: SizeUnit? = null,
  vertical: SizeUnit? = null,
) = padding(
  start = horizontal,
  top = vertical,
  end = horizontal,
  bottom = vertical,
)

/** 调整 View 四个方向的内边距 */
fun Modifier.padding(padding: Padding) =
  padding(padding.start, padding.top, padding.end, padding.bottom)

/** 调整 View 四个方向的内边距 */
fun Modifier.padding(all: SizeUnit) =
  padding(all, all)

/** 调整 View 的左右边缘的内边距 */
fun Modifier.paddingHorizontal(size: SizeUnit) =
  padding(horizontal = size)

/** 调整 View 的上下边缘的内边距 */
fun Modifier.paddingVertical(size: SizeUnit) =
  padding(vertical = size)


/** View 内边距调整的具体实现 */
private data class PaddingModifier(
  val _start: SizeUnit? = null,
  val _top: SizeUnit? = null,
  val _end: SizeUnit? = null,
  val _bottom: SizeUnit? = null,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    setPaddingRelative(
      _start?.toIntPxOrNull() ?: paddingStart,
      _top?.toIntPxOrNull() ?: paddingTop,
      _end?.toIntPxOrNull() ?: paddingEnd,
      _bottom?.toIntPxOrNull() ?: paddingBottom,
    )
  }
}