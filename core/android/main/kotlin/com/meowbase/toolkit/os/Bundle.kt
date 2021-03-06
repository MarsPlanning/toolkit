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

package com.meowbase.toolkit.os

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.meowbase.toolkit.className
import java.io.Serializable

/**
 * 创建带有参数的 Bundle
 * @param params 参数 -> [Bundle.putAll] [fillBundleArguments]
 */
fun createBundle(vararg params: Pair<String, Any?>): Bundle {
  val bundle = Bundle()
  fillBundleArguments(bundle, params)
  return bundle
}

/**
 * 转换实际参数
 * [Pair.first] -> 扩展数据名称
 * [Pair.second] -> 扩展数据值
 * @see [Intent.putExtra]
 */
fun fillBundleArguments(bundle: Bundle, params: Array<out Pair<String, Any?>>) {
  if (params.isEmpty()) return
  params.forEach {
    when (val value = it.second) {
      is Array<*> -> when {
        value.isArrayOf<CharSequence>() -> bundle.putSerializable(it.first, value)
        value.isArrayOf<String>() -> bundle.putSerializable(it.first, value)
        value.isArrayOf<Parcelable>() -> bundle.putParcelable(it.first, value as Parcelable?)
        else -> error("Bundle extra ${it.first} has wrong type ${value.className}")
      }
      is Int -> bundle.putInt(it.first, value)
      is Long -> bundle.putLong(it.first, value)
      is String -> bundle.putString(it.first, value)
      is Float -> bundle.putFloat(it.first, value)
      is Double -> bundle.putDouble(it.first, value)
      is Char -> bundle.putChar(it.first, value)
      is Short -> bundle.putShort(it.first, value)
      is Boolean -> bundle.putBoolean(it.first, value)
      is Bundle -> bundle.putBundle(it.first, value)
      is IntArray -> bundle.putIntArray(it.first, value)
      is LongArray -> bundle.putLongArray(it.first, value)
      is FloatArray -> bundle.putFloatArray(it.first, value)
      is DoubleArray -> bundle.putDoubleArray(it.first, value)
      is CharArray -> bundle.putCharArray(it.first, value)
      is ShortArray -> bundle.putShortArray(it.first, value)
      is BooleanArray -> bundle.putBooleanArray(it.first, value)
      is CharSequence -> bundle.putCharSequence(it.first, value)
      is Serializable -> bundle.putSerializable(it.first, value)
      is Parcelable -> bundle.putParcelable(it.first, value)
      null -> bundle.putBundle(it.first, value)
      else -> error("Bundle extra ${it.first} has wrong type ${value.className}")
    }
    return@forEach
  }
}