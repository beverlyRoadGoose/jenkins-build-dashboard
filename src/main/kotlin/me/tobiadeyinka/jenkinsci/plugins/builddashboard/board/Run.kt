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

import com.fasterxml.jackson.annotation.JsonIgnore

import hudson.model.Run

/**
 * Defines general attributes of runs of a job.
 */
open class Run constructor(@JsonIgnore private val run: Run<*, *>) {

    /**
     * Build number of this run.
     */
    protected val buildNumber: Int = run.number

    /**
     * Display name of this run
     */
    protected val displayName: String = run.getDisplayName()

    /**
     * Description of the run
     */
    protected val description: String? = run.getDescription()

    /**
     * Milliseconds representation of the time the run started
     */
    protected val startTime: Long? = run.getStartTimeInMillis()

    /**
     * Milliseconds representation of the time it took for the last complete run to finish
     */
    protected val duration: Long? = run.getDuration()

}