/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa

import com.futureworkshops.mobileworkflow.domain.PluginFactory
import com.futureworkshops.mobileworkflow.model.plugin.PluginInformation

class AAPluginFactory : PluginFactory(
    listOf(AAPlugin())
) {
    override fun getInformation(): PluginInformation = PluginInformation(
        name = BuildConfig.LIBRARY_PACKAGE_NAME,
        version = BuildConfig.PLUGIN_VERSION
    )
}