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

package com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins

import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.Page
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.entities.pagecomponents.Button
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.entities.pagecomponents.TextField

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class LoginPage(webDriver: WebDriver, explicitLoad: Boolean = true) : Page(webDriver, explicitLoad) {

  private val userNameFieldLocator: By = By.id("j_username")
  private val passwordFieldLocator: By = By.name("j_password")
  private val signInButtonLocator: By = By.name("Submit")

  val userNameField: TextField
  val passwordField: TextField
  val signInButton: Button

  init {
    waitForElementToBeDisplayed(signInButtonLocator)
    userNameField = TextField(webDriver, userNameFieldLocator)
    passwordField = TextField(webDriver, passwordFieldLocator)
    signInButton = Button(webDriver, signInButtonLocator)
  }

  override fun url(): String = "$serverUrl/login"

}