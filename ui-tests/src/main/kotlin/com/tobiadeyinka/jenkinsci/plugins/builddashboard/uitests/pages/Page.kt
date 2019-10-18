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

package com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages

import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.config.TestConfig
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.config.TestConfigLoader

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions

abstract class Page(val webDriver: WebDriver, explicitLoad: Boolean = true) {

  protected val testConfig: TestConfig = TestConfigLoader().testConfig
  protected val serverUrl: String = testConfig.config!!.server!!.address

  init {
    if (explicitLoad) {
      load()
    }
  }

  fun waitForElementToBeDisplayed(locator: By): MutableList<WebElement> = WebDriverWait(webDriver, 10).until(
    ExpectedConditions.presenceOfAllElementsLocatedBy(locator)
  )

  private fun load() = webDriver.get(url())

  abstract fun url(): String

}