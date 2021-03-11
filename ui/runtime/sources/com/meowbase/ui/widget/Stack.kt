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

@file:Suppress("FunctionName", "OverridingDeprecatedMember")

package com.meowbase.ui.widget

import android.widget.FrameLayout
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.widget.implement.*

/**
 * 一个盒子布局 [Ui.Row]
 * 可让子视图在其中堆叠、平放
 *
 * @see FrameLayout
 */
inline fun Ui.Box(
  modifier: Modifier = Modifier,
  children: Box.() -> Unit
): Box = With(::Box) {
  it.modifier = modifier
  it.children()
}