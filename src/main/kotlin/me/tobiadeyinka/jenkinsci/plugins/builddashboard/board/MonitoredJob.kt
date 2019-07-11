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
import hudson.model.Run

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

import hudson.model.Result

/**
 * Defines the attributes of a job monitored on the dashboard. JsonAutoDetect annotation is set to any to expose the
 * private fields in the responses to data requests from the vue webapp.
 *
 * The attributes with types from the jenkins lib are json ignored because they have lots of info that cause a stack
 * overflow during jackson serialization
 *
 * @property[jenkinsJob] The jenkins job represented on the monitor
 */
class MonitoredJob constructor(@JsonIgnore private val jenkinsJob: Job<*, *>) {

    /**
     * Display name of the job on the dashboard
     */
    private val displayName: String = jenkinsJob.getDisplayName()

    /**
     * Last/current run of the job
     */
    @JsonIgnore
    private val latestRun: Run<*, *>? = jenkinsJob.getLastBuild()

    /**
     * Number of the currently running job (if there is one). If there is no running job, the value will
     * be the number of the last complete run.
     */
    private val latestRunNumber: Int = latestRun?.getNumber() ?: 0

    /**
     * Name of the currently running job (if there is one). If there is no running job, the value will
     * be the name of the last complete run.
     */
    private val latestRunName: String? = latestRun?.getDisplayName()

    /**
     * Last complete run of the job
     */
    @JsonIgnore
    private val lastCompleteRun: Run<*, *>? = jenkinsJob.getLastCompletedBuild()

    /**
     * Number of the last complete run
     */
    private val lastCompleteRunNumber: Int = lastCompleteRun?.getNumber() ?: 0

    /**
     * Name of the last complete run
     */
    private val lastCompleteRunName: String = lastCompleteRun?.getDisplayName() ?: ""

    /**
     * Result of the last complete run. Defaults to [Result.NOT_BUILT] if there is no complete build
     */
    private val lastCompleteRunResult: Result? = lastCompleteRun?.getResult() ?: Result.NOT_BUILT

    /**
     * State of the latest build, running or not. defaults to false if there is no existing run
     */
    private val isRunning: Boolean = latestRun?.isBuilding() ?: false

    /**
     * Indicates if a job is currently runnable or not (disabled)
     */
    private val isRunnable: Boolean = jenkinsJob.isBuildable()

    /**
     * Description of the latest/current run
     */
    private val latestRunDescription: String? = latestRun?.getDescription()

    /**
     * Human readable format of the time a job has been running for.
     */
    private val currentRunDurationPretty: String? = latestRun?.getTimestampString()

    /**
     * Milliseconds representation of the time the current run started
     */
    private val startTime: Long? = latestRun?.getStartTimeInMillis()

    /**
     * Milliseconds representation of the time the last complete run started
     */
    private val lastCompleteRunStartTime: Long? = lastCompleteRun?.getStartTimeInMillis()

    /**
     * Milliseconds representation of the time it took for the last complete run to finish.
     */
    private val lastCompleteRunDuration: Long? = lastCompleteRun?.getDuration()

    /**
     * @return the number of runs in queue
     */
    @JsonProperty("numberOfQueuedRuns")
    fun numberOfQueuedRuns(): Int {
        return 0
    }

}