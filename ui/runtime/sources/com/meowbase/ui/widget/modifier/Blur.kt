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
import com.meowbase.toolkit.float
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.UpdatableModifier
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.material.BlurMaterial
import com.meowbase.ui.core.graphics.material.Material
import com.meowbase.ui.core.graphics.useOrNull
import com.meowbase.ui.theme.Colors.Companion.resolveColor
import com.meowbase.ui.theme.Materials.Companion.resolveMaterial
import com.meowbase.ui.theme.currentMaterials
import com.meowbase.ui.util.BlurHelper

/*
 * author: 凛
 * date: 2020/8/14 3:40 PM
 * github: https://github.com/RinOrz
 * description: 类似于 iOS 的 UIBlurEffect 或 Flutter 的 ImageFilter.blur
 * reference: https://developer.apple.com/documentation/uikit/uiblureffect
 */
interface BlurEffect : Ui {
  /** 模糊效果的具体实现 [BlurHelper] */
  var blurHelper: BlurHelper?
}

/**
 * 将模糊效果应用于视图背后的内容
 * @param radius [BlurMaterial.radius]
 * @param overlayColor [BlurMaterial.overlayColor]
 * @see material
 */
fun Modifier.blurEffect(
  radius: Number? = null,
  overlayColor: Color = Color.Unspecified,
) = +BlurMaterialModifier(radius = radius, overlayColor = overlayColor)

/**
 * 将模糊材质应用于视图背后的内容
 * @param blurMaterial 模糊材质
 * @see blurEffect
 */
fun Modifier.material(blurMaterial: BlurMaterial) = +BlurMaterialModifier(blurMaterial)


/** 为视图提供模糊材质的调整器 */
private data class BlurMaterialModifier(
  val material: Material? = null,
  val radius: Number? = null,
  val overlayColor: Color = Color.Unspecified,
) : Modifier, UpdatableModifier {
  val Ui.resolvedMaterial
    get() = (material ?: currentMaterials.regular.resolveMaterial(this))
      .resolveMaterial(this)
      .merge(BlurMaterial(radius, overlayColor)) as? BlurMaterial
      ?: error("fun BlurEffect(material..) 参数必须是一个 BlurMaterial")

  override fun View.realize(parent: ViewGroup?) {
    if (this !is BlurEffect) return
    val material = resolvedMaterial
    blurHelper = BlurHelper(this).also { helper ->
      material.radius?.float?.apply { helper.radius = this }
      material.overlayColor.useOrNull()?.apply { helper.overlayColor = argb }
      helper.attach()
    }
  }

  override fun View.update(parent: ViewGroup?) {
    if (this !is BlurEffect) return
    blurHelper?.overlayColor = resolvedMaterial.overlayColor.useOrNull()
      ?.resolveColor(this)
      ?.argb
  }
}