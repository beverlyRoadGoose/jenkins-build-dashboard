
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

package me.tobiadeyinka.jenkinsci.plugins.builddashboard

import io.mockk.*
import org.junit.*

import hudson.model.*
import jenkins.model.Jenkins

import org.assertj.core.api.Assertions.assertThat

private const val TEST_BOARD_NAME = "Dashboard"
private const val TEST_BOARD_TITLE = "Dashboard title"

class BuildDashboardTest {

    private val jenkins: Jenkins = mockk(relaxed = true)
    private val owner: ViewGroup = mockk(relaxed = true)
    private val ownerItemGroup: ItemGroup<TopLevelItem> = mockk(relaxed = true)

    @Before
    fun setup() {
        mockkStatic(Jenkins::class).run {
            every { Jenkins.get() } returns jenkins
        }
    }

    @Test
    fun createDashboardWithoutTitle() {
        val dashboard = BuildDashboard(TEST_BOARD_NAME, null)
        assertThat(dashboard.getTitle()).isEqualTo(TEST_BOARD_NAME)
    }

    @Test
    fun createDashboardWithTitle() {
        val dashboard = BuildDashboard(TEST_BOARD_NAME, TEST_BOARD_TITLE)
        assertThat(dashboard.getTitle()).isEqualTo(TEST_BOARD_TITLE)
    }

    @Test
    fun jobsListShouldBeEmptyOnNewBoards() {
        val dashboard = BuildDashboard(TEST_BOARD_NAME, null)

        mockkObject(dashboard).run {
            every { dashboard.owner } returns owner
            every { dashboard.ownerItemGroup } returns ownerItemGroup
        }

        assertThat(dashboard.isEmpty())
    }

    @Test
    fun addJobToBoard() {
        val dashboard = BuildDashboard(TEST_BOARD_NAME, null)
        val job: TopLevelItem = mockk(relaxed = true)

        mockkObject(dashboard).run {
            every { dashboard.owner } returns owner
            every { dashboard.ownerItemGroup } returns ownerItemGroup
        }

        dashboard.add(job)
        assertThat(dashboard.jobNamesContains(job))
    }

    @Test
    fun removeJobFromBoard() {
        val dashboard = BuildDashboard(TEST_BOARD_NAME, null)
        val job: TopLevelItem = mockk(relaxed = true)

        mockkObject(dashboard).run {
            every { dashboard.owner } returns owner
            every { dashboard.ownerItemGroup } returns ownerItemGroup
        }

        dashboard.add(job)
        assertThat(dashboard.jobNamesContains(job))

        dashboard.remove(job)
        assertThat(!dashboard.jobNamesContains(job))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

}