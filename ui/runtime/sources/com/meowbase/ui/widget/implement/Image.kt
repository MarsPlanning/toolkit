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

@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate", "RestrictedApi")

package com.meowbase.ui.widget.implement

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatImageView
import com.meowbase.ui.Theme
import com.meowbase.ui.Ui
import com.meowbase.ui.UiKitMarker
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.ModifierManager
import com.meowbase.ui.core.UpdatableModifier
import com.meowbase.ui.core.graphics.*
import com.meowbase.ui.core.decoupling.*
import com.meowbase.ui.uiParent
import com.meowbase.ui.theme.Colors
import com.meowbase.ui.theme.Colors.Companion.resolveColor
import kotlin.math.roundToInt

/*
 * author: 凛
 * date: 2020/8/8 6:24 PM
 * github: https://github.com/RinOrz
 * description: 文本控件的扩展
 */
@UiKitMarker open class Image @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr),
  Theme.User,
  ViewCanvasProvider,
  ForegroundProvider,
  ModifierProvider,
  Ui {
  /**
   * 备份一份填充颜色，这是因为这个颜色可能是使用了颜色库 [Colors.Companion] 中占坑的颜色
   * 主题更新时这些坑位颜色也会动态更新，所以需要一份没有被解析过的颜色值进行判断是否使用了坑位色
   * 对于坑位色需要一些额外处理 [Colors.resolveColor]
   */
  private var backupTintColor: Color? = null

  /** 填充颜色，如果使用的颜色是 [Colors] 中的主题颜色则会跟随后续主题变化而更新 */
  fun setImageTintList(tint: Color?) {
    if (backupTintColor == tint) return
    backupTintColor = tint?.useOrNull()
    if (tint == null) supportImageTintList = null else backupTintColor?.argb
      ?.let(ColorStateList::valueOf)
      ?.apply(::setSupportImageTintList)
  }

  /**
   * 创建一个 Image 视图
   * @param imageResource 用 resId [Resources.getDrawable] 设置图片
   * @param imageBitmap 设置 [Bitmap] 类型的图片
   * @param imageDrawable 设置 [Drawable] 类型的图片
   * @param modifier 对于 View 的其他额外调整
   * @param scaleType 将原图显示在视图上时的缩放类型
   * @param tint 对图片进行着色，如果为 null 则取消之前的所有填色
   * @param tintMode 着色模式
   * @param colorFilter 滤色镜, [EraseColorFilter] 会取消所有滤色镜的设置
   * @param alpha 图片显示的透明度（不是控件的 [View.getAlpha] 透明度）
   */
  fun update(
    imageResource: Int? = null,
    imageBitmap: Bitmap? = null,
    imageDrawable: Drawable? = null,
    modifier: Modifier = this.modifier,
    scaleType: com.meowbase.ui.core.graphics.ScaleType? = null,
    tint: Color? = this.backupTintColor,
    tintList: ColorStateList? = null,
    tintMode: BlendMode? = null,
    colorFilter: ColorFilter = this.colorFilter ?: EraseColorFilter,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float? = null,
  ) = also {
    imageResource?.apply(it::setImageResource)
    imageBitmap?.apply(it::setImageBitmap)
    imageDrawable?.apply(it::setImageDrawable)
    setImageTintList(tint)
    tintList?.apply(it::setSupportImageTintList)
    tintMode?.toPorterDuffMode().apply(it::setSupportImageTintMode)
    when (colorFilter) {
      EraseColorFilter -> if (it.colorFilter != null) it.colorFilter = null
      else -> it.colorFilter = colorFilter
    }
    if (alpha != null) (255 * alpha).roundToInt().apply {
      if (it.imageAlpha != this) it.imageAlpha = this
    }
    if (scaleType != null && it.scaleType != scaleType.real) setScaleType(scaleType.real)
    it.modifier = modifier
  }


  /**
   * 更新主题
   *
   * NOTE: 如果之前使用了主题，主题更新时我们也要更新对应的值
   * 为了避免逻辑错误，后续手动修改了某个样式时将不会被主题覆盖
   * 除非使用的是主题中的值，如 [Colors.onPrimary] 等
   */
  override fun updateUiKitTheme() {
    backupTintColor?.resolveColor(this)?.apply(::setImageTintList)

    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.apply { update(uiParent) }
    }
  }


  // ---------------------------------------------------------------
  // -                  Decoupling Implementation                  -
  // ---------------------------------------------------------------

  override var foregroundSupport: Drawable? = null
  override var beforeDispatchDrawCallbacks: ArrayDeque<DrawEventWithValue>? = null
  override var afterDispatchDrawCallbacks: ArrayDeque<DrawEvent>? = null
  override var beforeDrawCallbacks: ArrayDeque<DrawEventWithValue>? = null
  override var afterDrawCallbacks: ArrayDeque<DrawEvent>? = null
  override var modifier: Modifier = Modifier
    set(value) {
      if (field == value || value == Modifier) return
      field = value
      modifier.apply { realize(uiParent) }
    }

  override fun dispatchDraw(canvas: Canvas) {
    var intercepted = false

    // 执行调用前的回调
    beforeDispatchDrawCallbacks?.forEach {
      if (it.invoke(canvas)) intercepted = true
    }

    // 如果上一个回调返回的值为 false 则代表不拦截 super()
    if (!intercepted) super.dispatchDraw(canvas)

    // 执行调用后的回调
    afterDispatchDrawCallbacks?.forEach {
      it.invoke(canvas)
    }
  }

  override fun draw(canvas: Canvas) {
    var intercepted = false

    // 执行调用前的回调
    beforeDrawCallbacks?.forEach {
      if (it.invoke(canvas)) intercepted = true
    }

    // 如果上一个回调返回的值为 false 则代表不拦截 super()
    if (!intercepted) super.draw(canvas)

    // 执行调用后的回调
    afterDrawCallbacks?.forEach {
      it.invoke(canvas)
    }
  }
}