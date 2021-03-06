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

package com.meowbase.preference.flupref

import com.meowbase.preference.kotpref.KotprefModel

/** 返回一个 [KotprefModel] */
typealias KotprefProvider = (
  arguments: List<*>,
  name: String,
  mode: Int,
  commitAllProperties: Boolean
) -> KotprefModel

/** 记录所有注册了的 [KotprefProvider] */
internal val kotprefProviders = mutableMapOf<String, KotprefProvider>()

/** 供其他 [KotprefModel] 扩展库中提供 */
interface FluprefModelProvider {
  /** Flutter 端发起请求时的需要响应的名称 */
  fun requestName(): String

  /** 提供一个请求响应后返回到 Flutter 端以供使用的实例 */
  fun kotprefProvider(
    arguments: List<*>,
    name: String,
    mode: Int,
    commitAllProperties: Boolean
  ): KotprefModel
}


/**
 * 提供 Flutter 端的 [KotprefModel] 以供 [FluprefPlugin] 解析
 * ```
 * provideFluprefModel("provideMmkv") { arguments, name, mode, commitAllProperties ->
 *   MmkvModel(
 *     name = name,
 *     processMode = mode,
 *     cryptKey = arguments[3] as String?,
 *     savePath = arguments[4] as String?,
 *     commitAllProperties = commitAllProperties
 *   )
 * }
 * == 当 Flutter 端需要使用此模型时将会发起 "provideMmkv" 请求 ==
 * ```
 *
 * @author 凛
 * @date 2020/9/18 上午11:58
 * @github https://github.com/RinOrz
 * @param requestName Flutter 发起调用请求时的名称
 * @param kotprefProvider 返回一个 [KotprefModel] 实例到 Flutter 端
 * @see FluprefPlugin.newInstance
 */
fun provideFluprefModel(requestName: String, kotprefProvider: KotprefProvider) {
  kotprefProviders[requestName] = kotprefProvider
}


/**
 * 提供 Flutter 端的 [KotprefModel] 以供 [FluprefPlugin] 解析
 * ```
 * class Store: FluprefModelProvider {
 *  fun requestName() = "provideStore"
 *
 *  fun kotprefProvider(
 *    arguments: List<*>,
 *    name: String,
 *    mode: Int,
 *    commitAllProperties: Boolean
 *   ): KotprefModel = StoreModel(
 *     name = name,
 *     mode = mode,
 *     commitAllProperties = commitAllProperties
 *   )
 * }
 *
 * provideFluprefModel(Store())
 *
 * == 当 Flutter 端需要使用此模型时将会发起 "provideStore" 请求 ==
 * ```
 *
 * @param fluprefProviders 使用其他 Preference 扩展库中的内置提供器
 * @see FluprefPlugin.newInstance
 */
fun provideFluprefModels(vararg fluprefProviders: FluprefModelProvider) {
  fluprefProviders.forEach { fluprefProvider ->
    kotprefProviders[fluprefProvider.requestName()] = fluprefProvider::kotprefProvider
  }
}