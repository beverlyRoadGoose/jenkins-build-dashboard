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

package com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.processes

import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.config.entities.User
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.entities.jenkins.JobType

import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins.IndexPage
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins.LoginPage
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins.NewViewPage
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins.NewJobConfigurationPage

import org.openqa.selenium.WebDriver

abstract class Process {
  companion object {
    fun goToIndexPage(webDriver: WebDriver): IndexPage = IndexPage(webDriver)

    fun login(loginPage: LoginPage, user: User): IndexPage {
      loginPage.userNameField.typeIn(user.username)
      loginPage.passwordField.typeIn(user.password)
      loginPage.signInButton.click()

      return IndexPage(loginPage.webDriver, false)
    }

    fun createFreestyleJob(webDriver: WebDriver, jobName: String) = createNewJob(webDriver, jobName, JobType.FREESTYLE)

    fun createPipelineJob(webDriver: WebDriver, jobName: String) = createNewJob(webDriver, jobName, JobType.PIPELINE)

    fun createDashboardView(webDriver: WebDriver, dashboardName: String) {
      NewViewPage(webDriver)
        .setViewName(dashboardName)
        .selectBuildDashboard()
        .save()
    }

    private fun createNewJob(webDriver: WebDriver, jobName: String, jobType: JobType) {
      NewJobConfigurationPage(webDriver)
        .setJobName(jobName)
        .selectJobType(jobType)
        .save()
    }
  }
}