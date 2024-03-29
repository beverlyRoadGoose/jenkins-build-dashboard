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

package com.tobiadeyinka.jenkinsci.plugins.builddashboard.plugins.pipelines

import hudson.model.Job

import org.jenkinsci.plugins.workflow.graph.FlowNode
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import org.jenkinsci.plugins.workflow.cps.nodes.StepStartNode
import org.jenkinsci.plugins.workflow.support.steps.StageStep

import com.tobiadeyinka.jenkinsci.plugins.builddashboard.plugins.PluginManager

import java.util.*

class PipelineUtils {

  private val pluginManager: PluginManager = PluginManager()

  fun getRunningStages(job: Job<*, *>): List<PipelineStage> {
    val pipelineStages: MutableList<PipelineStage> = mutableListOf()

    if (pluginManager.pipelinePluginIsInstalled() && job is WorkflowJob) {
      job.lastBuild?.let { build ->
        val workflowRun: WorkflowRun = build
        val nodes: Queue<FlowNode> = LinkedList<FlowNode>()

        workflowRun.execution?.let { run ->
          nodes.addAll(run.currentHeads)

          while (!nodes.isEmpty()) {
            nodes.remove().let {
              if (it is StepStartNode && it.descriptor!!.isSubTypeOf(StageStep::class.java)) {
                pipelineStages.add(
                  PipelineStage(it.displayName)
                )
              } else {
                nodes.addAll(it.parents)
              }
            }
          }
        }
      }
    }

    return pipelineStages
  }

}