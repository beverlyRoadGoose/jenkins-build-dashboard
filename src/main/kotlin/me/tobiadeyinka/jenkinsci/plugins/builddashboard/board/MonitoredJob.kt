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

package me.tobiadeyinka.jenkinsci.plugins.builddashboard.board

import hudson.model.Job

import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonIgnore

import org.jenkinsci.plugins.workflow.job.WorkflowJob

import me.tobiadeyinka.jenkinsci.plugins.builddashboard.plugins.PluginManager
import me.tobiadeyinka.jenkinsci.plugins.builddashboard.plugins.pipelines.PipelineStage
import me.tobiadeyinka.jenkinsci.plugins.builddashboard.plugins.pipelines.PipelineUtils
import me.tobiadeyinka.jenkinsci.plugins.builddashboard.serialization.MonitoredJobPropertyFilter

/**
 * Defines a job monitored on the dashboard. JsonAutoDetect annotation is set to any to expose the
 * private fields in the responses to data requests from the vue webapp.
 *
 * The attributes with types from the jenkins lib are json ignored because they have lots of info that causes a stack
 * overflow during jackson serialization. This is also why the class doesn't just extend the Job class.
 *
 * @property[job] The jenkins job represented on the monitor
 */
@JsonFilter(MonitoredJobPropertyFilter.NAME)
class MonitoredJob constructor(@JsonIgnore private val job: Job<*, *>) {

    /**
     * Display name of the job on the dashboard
     */
    val displayName: String = job.getDisplayName()

    /**
     * Last/current run of the job
     */
    val latestRun: LatestRun? = job.getLastBuild()?.let {
        LatestRun(it)
    }

    /**
     * Last complete run of the job
     */
    val lastCompleteRun: CompleteRun? = job.getLastCompletedBuild()?.let {
        CompleteRun(it)
    }

    /**
     * Indicates if the job is in a runnable state. (e.g not disabled/archived)
     */
    val isBuildable: Boolean = job.isBuildable()

    /**
     * Health status of the job based on the results of the last 5 runs
     */
    val healthReport: HealthReport = HealthReport(
        job.getBuildHealth().getIconUrl("32x32"),
        job.getBuildHealth().getDescription()
    )

    /**
     * Indicates if the job is a pipeline. Requires the pipeline plugin to be installed on the jenkins server.
     */
    val isPipeline: Boolean = PluginManager().pipelinePluginIsInstalled() && job is WorkflowJob

    /**
     * List of stages in the job if it is a pipeline, returns an empty list if it isn't
     */
    val pipelineStages: List<PipelineStage> = PipelineUtils().getPipelineStages(this)

}