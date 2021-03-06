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

@file:Suppress(
  "PropertyName", "MemberVisibilityCanBePrivate",
  "UnstableApiUsage", "PackageDirectoryMismatch"
)

import com.android.build.api.dsl.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.meowbase.toolkit.isNotNull
import com.meowbase.toolkit.safeCast
import org.gradle.api.Project
import java.io.File


/*
 * author: 凛
 * date: 2020/9/6 下午12:56
 * github: https://github.com/RinOrz
 * description: 用于简单快速的配置通用的 APP / AAR 项目的 build.gradle.kts
 */
class AndroidSharedConfig internal constructor(
  /** 每次使用共享 android 配置时的项目实例，可在 shareAndroidConfig {} 中调用 */
  val project: Project
) {

  /*
   * Some basic options for android
   */
  var compileSdkVersion: Int = 29
  var targetSdkVersion: Int = compileSdkVersion
  var minSdkVersion: Int = 21


  /*
   * Some options for jetpack compose
   * https://developer.android.com/jetpack/androidx/releases/compose#declaring_dependencies
   */
  internal var composeOptions: (ComposeOptions.() -> Unit)? = null
  var enableCompose: Boolean = false

  /*
   * Jetpack compose 的版本号
   * 默认会使用 versions.properties 文件中的 compose 相关依赖的版本（如果存在）
   * https://github.com/jmfayard/refreshVersions
   * https://developer.android.com/jetpack/androidx/releases/compose#declaring_dependencies
   */
  var composeExtensionVersion: String? = null
    get() = field ?: resolveComposeVersion()


  /** [BaseFlavor.testInstrumentationRunner] */
  var testInstrumentationRunner: String = "androidx.test.runner.AndroidJUnitRunner"


  internal var appProguardFiles: Array<out Any> = arrayOf(File("proguard-rules.pro"))
  internal var libraryProguardFiles: Array<out Any> = arrayOf(File("consumer-rules.pro"))

  internal var debugSigningConfig: (SigningConfig.() -> Unit)? = null
  internal var releaseSigningConfig: (SigningConfig.() -> Unit)? = debugSigningConfig
  internal var debugBuildType: (BuildType.() -> Unit)? = null
  internal var releaseBuildType: (BuildType.() -> Unit)? = null

  internal var defaultConfig: (DefaultConfig.() -> Unit)? = null
  internal var libraryFullOptions: (LibraryExtension.() -> Unit)? = null
  internal var appFullOptions: (AppExtension.() -> Unit)? = null
  internal var mixinFullOptions: (MixinExtension.() -> Unit)? = null
  internal var baseExtensionOptions: (BaseExtension.() -> Unit)? = null
  internal var compileOptions: (CompileOptions.() -> Unit)? = null

  /**
   * 开启 Jetpack compose 并对其设置
   * 也可以看看:
   * @see enableCompose
   * @see composeExtensionVersion
   */
  fun enableCompose(options: ComposeOptions.() -> Unit) {
    enableCompose = true
    composeOptions = options
  }

  /**
   * AAR 库项目需要混淆时的混淆文件
   * @see BaseFlavor.proguardFiles
   */
  fun libraryProguard(vararg proguardFiles: Any) {
    libraryProguardFiles = proguardFiles
  }

  fun libraryProguard(proguardFiles: Iterable<Any>) {
    libraryProguardFiles = proguardFiles.toList().toTypedArray()
  }

  /**
   * App 项目需要混淆时的混淆文件
   * @see BaseFlavor.proguardFiles
   */
  fun appProguard(vararg proguardFiles: Any) {
    appProguardFiles = proguardFiles
  }

  fun appProguard(proguardFiles: Iterable<Any>) {
    appProguardFiles = proguardFiles.toList().toTypedArray()
  }

  /**
   * Debug 模式下的完整配置
   * NOTE: 不设置则不会进行签名
   * @see CommonExtension.buildTypes
   */
  fun debugBuildType(config: BuildType.() -> Unit) {
    debugBuildType = config
  }

  /**
   * Release 模式下的完整配置
   * @see CommonExtension.buildTypes
   */
  fun releaseBuildType(config: BuildType.() -> Unit) {
    releaseBuildType = config
  }

  /**
   * Debug 模式下的签名配置
   * NOTE: 不设置则不会进行签名
   * @see CommonExtension.signingConfigs
   */
  fun debugSignature(config: SigningConfig.() -> Unit) {
    debugSigningConfig = config
  }

  /**
   * Release 模式下的签名配置
   * NOTE: 当没设置 Release 签名配置时，如果 [debugSigningConfig] 设置了则将会使用 Debug 模式的签名
   * 如果都没有设置则不会进行签名
   * @see CommonExtension.signingConfigs
   */
  fun releaseSignature(config: SigningConfig.() -> Unit) {
    releaseSigningConfig = config
  }

  /**
   * 适用于 app / aar 项目 android 块内的 default {} 的完整配置
   * 如果其他的所有内置通用选项都不满足则可以创建此原始块来自定义
   * ```
   * android {
   *   与 default { ... } 相同
   * }
   * ```
   */
  fun defaultConfig(block: DefaultConfig.() -> Unit) {
    defaultConfig = block
  }

  /**
   * 适用于 aar-library 项目的完整配置
   * 如果其他的所有内置通用选项都不满足则可以创建此原始块来自定义
   * ```
   * 与 android { ... } 相同
   * ```
   */
  fun libraryFullOptions(block: LibraryExtension.() -> Unit) {
    libraryFullOptions = block
  }

  /**
   * 适用于 application 项目的完整配置
   * 如果其他的所有内置通用选项都不满足则可以创建此原始块来自定义
   * ```
   * 与 android { ... } 相同
   * ```
   */
  fun appFullOptions(block: AppExtension.() -> Unit) {
    appFullOptions = block
  }

  /**
   * 适用于 application 或 aar-library 项目的完整混合配置
   * 如果其他的所有内置通用选项都不满足则可以创建此原始块来自定义
   * ```
   * 与 android { ... } 相同
   * ```
   * @see appFullOptions
   * @see libraryFullOptions
   * @see baseExtensionOptions
   */
  fun mixinFullOptions(block: MixinExtension.() -> Unit) {
    mixinFullOptions = block
  }

  /**
   * 适用于 application 或 aar-library 项目的完整混合配置
   * 如果其他的所有内置通用选项都不满足则可以创建此原始块来自定义
   * ```
   * 与 android { ... } 相同
   * ```
   * @see mixinFullOptions
   * @see BaseExtension
   */
  fun baseExtensionOptions(block: BaseExtension.() -> Unit) {
    baseExtensionOptions = block
  }

  /** [BaseExtension.compileOptions] */
  fun compileOptions(block: CompileOptions.() -> Unit) {
    compileOptions = block
  }

  private fun resolveComposeVersion(): String? = VersionsProperties.properties
    .filterKeys { it.safeCast<String>()?.startsWith("version.androidx.compose") == true }
    .values.firstOrNull() as? String
}