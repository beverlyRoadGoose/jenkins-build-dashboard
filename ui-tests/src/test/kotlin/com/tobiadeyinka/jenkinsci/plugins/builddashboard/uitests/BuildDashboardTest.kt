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

package com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests

import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.config.TestConfig
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.config.entities.User
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.config.TestConfigLoader

import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.processes.Process
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.util.WebDriverManager
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins.IndexPage
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins.LoginPage

import java.util.*

import org.junit.Test
import org.junit.AfterClass
import org.junit.BeforeClass

class BuildDashboardTest {
  companion object {
    private val webDriver = WebDriverManager().getWebDriver()
    private val testConfig: TestConfig = TestConfigLoader().testConfig
    private val testUser: User = User(testConfig.config!!.user!!.username, testConfig.config!!.user!!.password)

    @BeforeClass @JvmStatic fun createJobsForTests() {
      Process.login(LoginPage(webDriver), testUser)
      Process.createFreestyleJob(webDriver, "Freestyle ${UUID.randomUUID()}")
      Process.createPipelineJob(webDriver, "Pipeline ${UUID.randomUUID()}")
    }

    @AfterClass @JvmStatic fun tearDown() = webDriver.quit()
  }

  @Test fun shouldBeAbleToCreateDashboard() {
    Process.createDashboardView(webDriver, "Dashboard ${UUID.randomUUID()}")
    //TODO verify view url returns 200
  }

  fun goToIndexPage(): IndexPage = IndexPage(webDriver)
}