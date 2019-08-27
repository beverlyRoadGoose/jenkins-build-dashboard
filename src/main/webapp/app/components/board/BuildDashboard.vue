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
    <div id="info-box" class="transitions" :style="infoBoxStyleObject">{{this.informationMessage}}</div>
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
            v-for="(job, index) in this.jobsData"
            :key="index"
            :resources-url="resourcesUrl"
            :board="board"
            :installation="JSON.parse(installation)"
            :job-data="job">
        </monitored-job>
      </div>
    </div>

    <dashboard-footer :build="JSON.parse(installation).build"></dashboard-footer>
  </div>
</template>

<script>
  import {page} from 'vue-analytics'
  import {Events} from '../../Events';

  import MonitoredJob from './MonitoredJob';
  import DashboardHeader from './DashboardHeader';
  import DashboardFooter from './DashboardFooter';
  import SettingsWidget from "./SettingsWidget";
  import SettingsManager from '../../util/SettingsManager';

  export default {
    name: 'BuildDashboard',

    props: ['resourcesUrl', 'board', 'boardTitle', 'jobs', 'installation'],

    data() {
      return {
        jobsData: JSON.parse(this.jobs),

        infoBoxStyleObject: {
          opacity: 0,
        },

        informationMessage: ''
      }
    },

    components: {
      SettingsWidget,
      MonitoredJob,
      DashboardHeader,
      DashboardFooter
    },

    created: function () {
      this.$root.$on(Events.NEW_INFORMATION, (event, message, statusColor) => this.displayInformation(
        event,
        message,
        statusColor
      ));
    },

    mounted() {
      if (SettingsManager.allowsAnalyticsTracking(this.board)) {
        this.track();
      }

      if (this.jobsData.length > 0) {
        this.startTimelyRequestsForLatestData();
      }
    },

    computed: {
      thereAreNoJobs: function () {
        return this.jobsData.length === 0;
      }
    },

    methods: {
      track: function () {
        page('/')
      },

      startTimelyRequestsForLatestData: function () {
        let self = this;
        setInterval(() => {
          window.pluginInstance.getMonitoredJobsAsJSON(updatedData => {
              self.jobsData = updatedData.responseJSON;
          });
        }, 3000);
      },

      displayInformation: function (event, message, statusColor) {
        this.informationMessage = message;
        this.infoBoxStyleObject = {
          opacity: 1,
        };

        setTimeout(() => {
          this.infoBoxStyleObject.opacity = 0;
          this.informationMessage = '';
        }, 10000);
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
    color: #ffffff;
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

  #info-box {
    position: absolute;
    top: 20px;
    left: 50%;
    -moz-transform: translateX(-50%);
    -webkit-transform: translateX(-50%);
    -ms-transform: translateX(-50%);
    -o-transform: translateX(-50%);
    transform: translateX(-50%);
    width: max-content;
    color: #ffffff;
    font-size: 1em;
    padding: 5px;
    border-radius: 5px;
    font-weight: 700;
  }
</style>