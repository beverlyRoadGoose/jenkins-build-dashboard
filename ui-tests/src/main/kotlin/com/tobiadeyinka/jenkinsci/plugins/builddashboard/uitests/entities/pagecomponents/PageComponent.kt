package com.tobiadeyinka.jenkinsci.plugins.builddashboard.uitests.entities.pagecomponents

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

open class PageComponent(webDriver: WebDriver, locator: By) {

  protected val webElement: WebElement = webDriver.findElement(locator)

}