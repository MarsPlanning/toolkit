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
import com.meowbase.toolkit.graphics.drawable.setRadius
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.UpdatableModifier
import com.meowbase.ui.core.decoupling.ForegroundProvider
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.drawable.Drawable
import com.meowbase.ui.core.graphics.drawable.NativeDrawable
import com.meowbase.ui.core.graphics.drawable.RippleDrawable
import com.meowbase.ui.core.graphics.drawable.drawWith
import com.meowbase.ui.core.graphics.shape.RectangleShape
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.graphics.toColorStates
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.core.graphics.useOrNull
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.toIntPxOrNull
import com.meowbase.ui.theme.currentColors


/**
 * 将水波纹 [RippleDrawable] 设置为视图的前景
 *
 * @param color 涟漪颜色
 * @param shape 涟漪的遮罩形状，默认为 [RectangleShape]
 * @param radius 默认使用 [View] 的大小
 */
fun Modifier.rippleForeground(
  color: Color = Color.Unspecified,
  radius: SizeUnit = SizeUnit.Unspecified,
  shape: Shape? = null,
) = +RippleEffectModifier(color, shape, radius, RippleEffectModifier.Type.Foreground)

/**
 * 将水波纹 [RippleDrawable] 设置为视图的背景
 *
 * @param color 涟漪颜色
 * @param shape 涟漪的遮罩形状，默认为 [RectangleShape]
 * @param radius 默认使用 [View] 的大小
 */
fun Modifier.rippleBackground(
  color: Color = Color.Unspecified,
  radius: SizeUnit = SizeUnit.Unspecified,
  shape: Shape? = null,
) = +RippleEffectModifier(color, shape, radius, RippleEffectModifier.Type.Background)


private class RippleEffectModifier(
  val color: Color,
  val shape: Shape?,
  val radius: SizeUnit,
  val type: Type,
) : Modifier, UpdatableModifier {
  override fun View.realize(parent: ViewGroup?) {
    if (type == Type.Foreground && this !is ForegroundProvider) return
    val color = color.useOrElse {
      (this as? Ui)?.currentColors?.ripple?.useOrNull()
        ?: Color.White.copy(alpha = 0.2f)
    }

    when (type) {
      Type.Background -> background = background.wrapRipple(color)
      Type.Foreground -> (this as ForegroundProvider).setSupportForeground(
        foregroundSupport.wrapRipple(color)
      )
    }
  }

  override fun View.update(parent: ViewGroup?) {
    val ui = this as? Ui ?: return
    val bg = background as? RippleDrawable
    bg?.setColor(bg.colorStates.updateColors(ui).toColorStateList())

    val fg = (this as? ForegroundProvider)?.foregroundSupport as? RippleDrawable
    fg?.setColor(fg.colorStates.updateColors(ui).toColorStateList())
  }

  private fun NativeDrawable?.wrapRipple(color: Color) = RippleDrawable(
    content = this,
    mask = drawWith(
      color = Color.White,
      shape = shape ?: if (this is Drawable) attributes.shape else null ?: RectangleShape,
    ),
    colorStates = color.toColorStates(),
  ).also {
    radius.toIntPxOrNull()?.apply { setRadius(it, this) }
  }

  enum class Type { Background, Foreground }
}