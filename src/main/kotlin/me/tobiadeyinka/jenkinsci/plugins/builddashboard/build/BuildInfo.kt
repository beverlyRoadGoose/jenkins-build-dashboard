/*
 * MIT License
 *
 * Copyright (c) 2019 Oluwatobi Adeyinka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.tobiadeyinka.jenkinsci.plugins.builddashboard.build

import java.util.*
import java.io.InputStream

/**
 * Manages build information for installation instances
 *
 * @property[version] The installed version of the plugin
 */
class BuildInfo {

    private val pluginName: String
    private val version: String

    init {
        val properties = Properties()
        val propertiesInputStream: InputStream = BuildInfo::class.java.classLoader.getResourceAsStream("build.properties")

        properties.load(propertiesInputStream)

        pluginName = properties.getProperty("pluginName")
        version = properties.getProperty("version")
    }

    /**
     * @return The name of the plugin
     */
    fun pluginName(): String = pluginName

    /**
     * @return The currently installed version of the plugin
     */
    fun version(): String = version

}