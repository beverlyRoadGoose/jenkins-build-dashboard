<!--
  - MIT License
  -
  - Copyright (c) 2019 Oluwatobi Adeyinka
  -
  - Permission is hereby granted, free of charge, to any person obtaining a copy
  - of this software and associated documentation files (the "Software"), to deal
  - in the Software without restriction, including without limitation the rights
  - to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  - copies of the Software, and to permit persons to whom the Software is
  - furnished to do so, subject to the following conditions:
  - The above copyright notice and this permission notice shall be included in all
  - copies or substantial portions of the Software.
  -
  - THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  - IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  - FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  - AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  - LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  - OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  - SOFTWARE.
-->

<template>
  <div id="dashboard">
    <dashboard-header :resources-url="resourcesUrl" :board="board" :board-title="boardTitle"></dashboard-header>
    <settings-widget :resources-url="resourcesUrl" :board="board"></settings-widget>

    <div id="monitor-wrapper">
      <div v-if="thereAreNoJobs" id="empty-board-notice">
        <span>There's nothing here.</span>
        <br>
        <span>
          <a :href="'/view/' + board + '/configure'" class="transitions">Add jobs to the board</a> to be able to monitor them.
        </span>
      </div>

      <div v-else id="monitored-jobs-wrapper">
        <monitored-job
            v-for="(job, index) in JSON.parse(jobs)"
            :key="index"
            :resources-url="resourcesUrl"
            :board="board"
            :job-data="job"
        ></monitored-job>
      </div>
    </div>

    <dashboard-footer :build="build"></dashboard-footer>
  </div>
</template>

<script>
  import MonitoredJob from '../job/MonitoredJob';
  import DashboardHeader from './DashboardHeader';
  import DashboardFooter from './DashboardFooter';
  import SettingsWidget from "./SettingsWidget";

  export default {
    name: 'BuildDashboard',

    props: ['resourcesUrl', 'board', 'boardTitle', 'jobs', 'build'],

    components: {
      SettingsWidget,
      MonitoredJob,
      DashboardHeader,
      DashboardFooter
    },

    mounted() {
      this.startTimelyRequestsForLatestData();
    },

    computed: {
      thereAreNoJobs: function () {
        return JSON.parse(this.jobs).length === 0;
      }
    },

    methods: {
      startTimelyRequestsForLatestData: function () {
        window.pluginInstance.getMonitoredJobsAsJSON(jobsData => {
          console.log(jobsData.responseJSON);
        });
      }
    }
  }
</script>

<style scoped>
  #dashboard {
    height: 100%;
  }

  #monitor-wrapper {
    padding: 3px;
    width: 100%;
    height: calc(100% - 75px);
    height: -moz-calc(100% - 75px);
    height: -webkit-calc(100% - 75px);
    min-height: calc(100% - 75px);
    min-height: -moz-calc(100% - 75px);
    min-height: -webkit-calc(100% - 75px);
  }

  #monitored-jobs-wrapper {
    padding: 3px;
    width: 100%;
    min-width: 100%;
    height: 100%;
    min-height: 100%;
    display: flex;
    flex-wrap: wrap;
  }

  #empty-board-notice {
    position: fixed;
    top: 50%;
    left: 50%;
    -webkit-transform: translate(-50%, -50%);
    -moz-transform: translate(-50%, -50%);
    -o-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    color: #3B3D3B;
    text-align: center;
  }

  #empty-board-notice span:first-of-type {
    font-size: 2em;
  }

  #empty-board-notice a {
    color: #38B0DE;
    text-decoration: none;
  }

  #empty-board-notice a:visited {
    color: #38B0DE;
  }

  #empty-board-notice a:hover {
    color: #38B0DE;
    text-decoration: underline;
  }
</style>