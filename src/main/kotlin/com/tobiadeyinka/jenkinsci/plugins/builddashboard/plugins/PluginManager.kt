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

package com.tobiadeyinka.jenkinsci.plugins.builddashboard.plugins

import java.util.*
import jenkins.model.Jenkins
import com.fasterxml.jackson.annotation.JsonProperty

class PluginManager {

  @JsonProperty("rebuildPluginIsInstalled")
  fun rebuildPluginIsInstalled(): Boolean = pluginIsInstalled(SupportedPlugins.REBUILD.artifactId)

  @JsonProperty("pipelinePluginIsInstalled")
  fun pipelinePluginIsInstalled(): Boolean = pluginIsInstalled(SupportedPlugins.PIPELINE.artifactId)

  /**
   * Checks if a plugin is installed on the jenkins server
   *
   * @param[artifactId] Jenkins artifactid/shortname for the plugin
   *
   * @return true if the plugin is installed, false otherwise
   */
  private fun pluginIsInstalled(artifactId: String): Boolean = !Objects.isNull(
    Jenkins.getInstance()?.getPlugin(artifactId)
  )

}