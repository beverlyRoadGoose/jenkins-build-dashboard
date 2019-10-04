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
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins.IndexPage
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins.LoginPage
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.entities.jenkins.JobType

abstract class Process {

    companion object {

        fun login(loginPage: LoginPage, user: User): IndexPage {
            loginPage.userNameField.typeIn(user.username)
            loginPage.passwordField.typeIn(user.password)
            loginPage.signInButton.click()

            return IndexPage(loginPage.webDriver, false)
        }

        fun createFreestyleJob(jobName: String) = createNewJob(jobName, JobType.FREESTYLE)

        fun createPipelineJob(jobName: String) = createNewJob(jobName, JobType.PIPELINE)

        private fun createNewJob(jobName: String, type: JobType) {

        }
    }

}