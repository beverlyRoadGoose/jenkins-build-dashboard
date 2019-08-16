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

package me.tobiadeyinka.jenkinsci.plugins.builddashboard

import java.io.IOException
import javax.servlet.ServletException

import hudson.Extension
import hudson.Functions
import hudson.Util.filter

import hudson.model.Job
import hudson.model.ListView
import hudson.model.Descriptor.FormException

import org.kohsuke.stapler.StaplerRequest
import org.kohsuke.stapler.DataBoundConstructor
import org.kohsuke.stapler.bind.JavaScriptMethod

import me.tobiadeyinka.jenkinsci.plugins.builddashboard.build.Build
import me.tobiadeyinka.jenkinsci.plugins.builddashboard.build.Installation
import me.tobiadeyinka.jenkinsci.plugins.builddashboard.build.BuildInfoLoader

import me.tobiadeyinka.jenkinsci.plugins.builddashboard.job.MonitoredJob

import net.sf.json.JSONArray
import net.sf.json.JSONObject
import net.sf.json.JSONSerializer.toJSON

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import com.fasterxml.jackson.module.kotlin.KotlinModule

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor.*


/**
 * Main entity definition of the plugin.
 *
 * @property[name] The name of the dashboard
 * @property[title] An optional title for the dashboard. Defaults to the name when not set.
 */
class BuildDashboard
@DataBoundConstructor constructor(private val name: String?, private var title: String?) : ListView(name) {

    private val installation: Installation = Installation()

    private val build: Build = installation.build

    /**
     * Handles the dashboard settings form data on submit
     */
    @Throws(ServletException::class, IOException::class, FormException::class)
    override fun submit(request: StaplerRequest) {
        super.submit(request)
        title = request.getParameter("title")
    }

    /**
     * @return The title of the dashboard if it exists, if not, return the name.
     */
    fun getTitle(): String = title ?: displayName

    /**
     * @return true when then there are no jobs added to the dashboard
     */
    fun isEmpty(): Boolean = allItems.isEmpty()

    /**
     * @return true when there are no jobs configured on the jenkins instance
     */
    fun jenkinsInstanceHasNoJobsConfigured(): Boolean = Functions.getAllTopLevelItems(owner.itemGroup).isEmpty()

    /**
     * @return The installed version of the plugin
     */
    fun getBuildVersion(): String = build.version

    /**
     * @return The number of jobs on the board
     */
    fun getNumberOfJobs(): Int = getMonitoredJobs().size

    /**
     * Used by the vue webapp to retrieve JSON data of the jobs on the board. The javascript method annotation exposes
     * the method to javascript calls.
     *
     * @return A JSON object representation of all the jobs on the board along with their status data
     */
    @JavaScriptMethod
    fun getMonitoredJobsAsJSON(): JSONArray = toJSON(getObjectMapper().writeValueAsString(getMonitoredJobs())) as JSONArray

    @JavaScriptMethod
    fun getInstallationAsJSON(): JSONObject = toJSON(getObjectMapper().writeValueAsString(installation)) as JSONObject

    /**
     * Creates [MonitoredJob] instances of all the jobs on the
     * board and returns a list of these instances
     */
    private fun getMonitoredJobs(): List<MonitoredJob> {
        val monitoredJobs: MutableList<MonitoredJob> = mutableListOf()
        filter(allItems, Job::class.java).forEach {
            monitoredJobs.add(MonitoredJob(it))
        }
        return monitoredJobs
    }

    private fun getObjectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(KotlinModule())
            .setVisibility(FIELD, JsonAutoDetect.Visibility.ANY) // enable jackson to see private fields as there are no public getters
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    }

    /**
     * Integration point with jenkins
     */
    @Extension
    class BuildDashboardDescriptor : hudson.model.ListView.DescriptorImpl() {

        /**
         *  Sets the display name that jenkins identifies the plugin with
         */
        override fun getDisplayName(): String = BuildInfoLoader()
            .getBuildInfo()
            .build!!
            .pluginName

    }

}