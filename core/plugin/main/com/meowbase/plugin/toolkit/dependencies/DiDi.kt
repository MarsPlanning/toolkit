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

@file:Suppress("PackageDirectoryMismatch", "SpellCheckingInspection", "unused")

/*
 * author: 凛
 * date: 2020/8/12 6:49 PM
 * github: https://github.com/RinOrz
 * description: 滴滴团队公开的依赖管理
 */
object DiDi {
  /* https://github.com/didi/booster */
  val booster = Booster

  object Booster {
    private const val artifactPrefix = "com.didiglobal.booster:booster"
    const val aapt2 = "$artifactPrefix-aapt2:_"
    const val api = "$artifactPrefix-api:_"
    const val cha = "$artifactPrefix-cha:_"
    const val command = "$artifactPrefix-command:_"
    const val taskAnalyser = "$artifactPrefix-task-analyser:_"
    const val transformThread = "$artifactPrefix-transform-thread:_"
    const val transformWebview = "$artifactPrefix-transform-webview:_"
    const val transformSharedPreferences = "$artifactPrefix-transform-shared-preferences:_"
    const val transformRInline = "$artifactPrefix-transform-r-inline:_"
    const val transformBrInline = "$artifactPrefix-transform-br-inline:_"
    const val transformFinalizerWatchdogDaemon = "$artifactPrefix-transform-finalizer-watchdog-daemon:_"
    const val transformMediaPlayer = "$artifactPrefix-transform-media-player:_"
    const val transformResCheck = "$artifactPrefix-transform-res-check:_"
    const val transformToast = "$artifactPrefix-transform-toast:_"
    const val transformActivityThread = "$artifactPrefix-transform-activity-thread:_"

    // https://github.com/didi/booster#package-size
    const val taskCompressionCwebp = "$artifactPrefix-task-compression-cwebp:_"
    const val taskCompressionPngquant = "$artifactPrefix-task-compression-pngquant:_"
    const val taskCompressionProcessedRes = "$artifactPrefix-task-compression-processed-res:_"
    const val taskResourceDeredundancy = "$artifactPrefix-task-resource-deredundancy:_"
    const val taskCheckSnapshot = "$artifactPrefix-task-check-snapshot:_"
    const val taskListPermission = "$artifactPrefix-task-list-permissiont:_"
    const val taskListSharedLibrary = "$artifactPrefix-task-list-shared-library:_"
  }
}
