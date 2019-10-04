package com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.jenkins

import org.openqa.selenium.WebDriver
import com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.pages.Page

class IndexPage(webDriver: WebDriver, explicitLoad: Boolean = true) : Page(webDriver, explicitLoad) {

    override fun url() = serverUrl

}