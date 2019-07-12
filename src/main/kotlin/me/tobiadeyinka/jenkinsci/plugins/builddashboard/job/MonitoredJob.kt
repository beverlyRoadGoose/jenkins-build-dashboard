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

package me.tobiadeyinka.jenkinsci.plugins.builddashboard.job

import hudson.model.Job

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Defines a job monitored on the dashboard. JsonAutoDetect annotation is set to any to expose the
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
    private val latestRun: LatestRun = LatestRun(jenkinsJob.getLastBuild())

    /**
     * Last complete run of the job
     */
    private val lastCompleteRun: CompleteRun = CompleteRun(jenkinsJob.getLastCompletedBuild())

    /**
     * @return the number of runs in queue
     */
    @JsonProperty("numberOfQueuedRuns")
    fun numberOfQueuedRuns(): Int {
        return 0
    }

}