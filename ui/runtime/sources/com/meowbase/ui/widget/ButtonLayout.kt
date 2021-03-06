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

@file:Suppress("FunctionName")

package com.meowbase.ui.widget

import android.view.View
import com.meowbase.ui.Ui
import com.meowbase.ui.core.*
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.isUnspecified
import com.meowbase.ui.widget.implement.ButtonBar
import com.meowbase.ui.widget.implement.ButtonLayout
import com.meowbase.ui.widget.modifier.clickable
import com.meowbase.ui.widget.modifier.margin
import com.meowbase.ui.widget.style.ButtonStyle
import com.meowbase.ui.theme.currentButtons
import com.meowbase.ui.theme.currentColors
import com.meowbase.ui.theme.currentShapes


/** 创建并添加按钮视图 */
inline fun Ui.Button(
  /** 按钮按下后的回调 */
  noinline onClick: ((View) -> Unit)?,
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
  /** [ButtonStyle.color] */
  color: Color = Color.Unspecified,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unspecified,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] */
  border: Border? = null,
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
): ButtonLayout = With(::ButtonLayout) {
  it.orientation = orientation.native
  it.mainAxisAlign = mainAxisAlign
  it.crossAxisAlign = crossAxisAlign
  it.modifier = modifier
  it.update(
    color = color,
    colorRipple = colorRipple,
    colorHighlight = colorHighlight,
    colorDisabled = colorDisabled,
    border = border,
    padding = padding,
    shape = shape,
    modifier = modifier.clickable(onClick = onClick),
    style = style
  )

  if (spaceBetween.isUnspecified) {
    children(it)
    return@With
  }

  val spaceModifier = when (orientation) {
    Orientation.Horizontal -> Modifier.margin(start = spaceBetween)
    else -> Modifier.margin(top = spaceBetween)
  }
  ModifyScope(spaceModifier) { children(it) }
}


/** 创建并添加按钮视图 */
fun ButtonBar.Button(
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
  /** [ButtonStyle.color] */
  color: Color = Color.Unspecified,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unspecified,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] */
  border: Border? = null,
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
) = (this as Ui).Button(
  onClick = null,
  mainAxisAlign = mainAxisAlign,
  crossAxisAlign = crossAxisAlign,
  color = color,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  border = border,
  padding = padding,
  shape = shape,
  modifier = modifier,
  spaceBetween = spaceBetween,
  orientation = orientation,
  style = style,
  children = children
)


/** 创建并添加线框按钮视图 */
inline fun Ui.OutlinedButton(
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
  /** [ButtonStyle.border] -> [Border.color] */
  color: Color = Color.Unspecified,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unspecified,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] -> [Border.size] */
  borderSize: SizeUnit = SizeUnit.Unspecified,
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
) = Button(
  onClick = null,
  mainAxisAlign = mainAxisAlign,
  crossAxisAlign = crossAxisAlign,
  // 线框按钮不需要背景
  color = Color.Transparent,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  border = Border(borderSize, color.useOrElse { currentColors.onSurface.copy(alpha = 0.12f) }),
  padding = padding,
  shape = shape,
  modifier = modifier,
  spaceBetween = spaceBetween,
  orientation = orientation,
  style = style,
  children = children
)


/** 创建并添加线框按钮视图 */
inline fun ButtonBar.OutlinedButton(
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
  /** [ButtonStyle.border] -> [Border.color] */
  color: Color = Color.Unspecified,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unspecified,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] -> [Border.size] */
  borderSize: SizeUnit = SizeUnit.Unspecified,
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
) = (this as Ui).OutlinedButton(
  mainAxisAlign = mainAxisAlign,
  crossAxisAlign = crossAxisAlign,
  color = color,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  borderSize = borderSize,
  padding = padding,
  shape = shape,
  modifier = modifier,
  spaceBetween = spaceBetween,
  orientation = orientation,
  style = style,
  children = children
)