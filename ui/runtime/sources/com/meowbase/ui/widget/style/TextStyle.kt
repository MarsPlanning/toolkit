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

@file:Suppress("MemberVisibilityCanBePrivate", "FunctionName")

package com.meowbase.ui.widget.style

import android.util.TypedValue
import com.meowbase.toolkit.graphics.loadedTypefaces
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.core.text.Font
import com.meowbase.ui.core.text.FontStyle
import com.meowbase.ui.core.text.TextDecoration
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.TextUnit
import com.meowbase.ui.core.unit.toIntPxOrNull
import com.meowbase.ui.core.unit.toPxOrNull
import com.meowbase.ui.widget.Text
import com.meowbase.ui.widget.implement.Text
import com.meowbase.ui.theme.Colors
import com.meowbase.ui.theme.PingFangFont
import com.meowbase.ui.theme.Typography
import com.meowbase.ui.theme.Typography.Companion.resolveTypography

/*
 * author: 凛
 * date: 2020/8/8 1:09 PM
 * github: https://github.com/RinOrz
 * description: 通用的 TextView 样式
 */
data class TextStyle(
  /** 文本颜色, 默认 [Colors.onSurface] */
  internal val color: Color = Color.Unspecified,

  /** 文本字体 */
  internal val font: Font? = null,

  /** 字体风格 */
  internal val fontStyle: FontStyle = FontStyle.Normal,

  /** 文本大小，如果为 [TextUnit.Inherit] 则不设置 */
  internal val fontSize: SizeUnit = TextUnit.Inherit,

  /** 字体文件名称，null 为用 [Text] 默认的或手动设置 [Text.setTypeface] */
  internal val fontName: String? = PingFangFont.Regular,

  /** 文字之间的距离， */
  internal val letterSpacing: SizeUnit = SizeUnit.Unspecified,

  /** 每一行文本的高度（不会影响文本大小），如果为 [TextUnit.Inherit] 则不设置 */
  internal val lineHeight: SizeUnit = SizeUnit.Unspecified,

  /** 文本外观装饰 */
  internal val decoration: TextDecoration? = null,
) {
  /** [Typography.resolveTypography] */
  internal var id: Int = -1

  fun apply(view: Text) {
    font?.also { view.setFont(it, fontStyle) } ?: view.setFont(style = fontStyle)
    decoration?.also(view::decorate)
    lineHeight.toIntPxOrNull(view.textSize)?.also(view::setLineHeight)
    fontSize.toPxOrNull(view.textSize)?.also { view.setTextSize(TypedValue.COMPLEX_UNIT_PX, it) }
    letterSpacing.toPxOrNull(view.letterSpacing)?.also { view.letterSpacing = it / view.textSize }
    fontName?.also { view.typeface = loadedTypefaces[it] }
    view.setTextColor(color)
  }

  /** 将当前 [TextStyle] 与 [other] 进行合并后得到一个新的样式 */
  fun merge(other: TextStyle): TextStyle = TextStyle(
    color = other.color.useOrElse { this.color },
    decoration = other.decoration,
    font = other.font ?: this.font,
    fontStyle = other.fontStyle,
    fontSize = if ((other.fontSize as? TextUnit)?.isInherit == false) other.fontSize else this.fontSize,
    fontName = other.fontName ?: this.fontName,
    lineHeight = if ((other.lineHeight as? TextUnit)?.isInherit == false) other.lineHeight else this.lineHeight,
    letterSpacing = if ((other.letterSpacing as? TextUnit)?.isInherit == false) other.letterSpacing else this.letterSpacing,
  ).also { if (this != it) it.id = id }

  /** 创建一个副本并传入给定的 Id 值 */
  internal fun new(id: Int) = copy().also { it.id = id }
}