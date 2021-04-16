/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa

import com.futureworkshops.mobileworkflow.domain.PluginFactory

class AAPluginFactory : PluginFactory(
    listOf(AAPlugin())
)