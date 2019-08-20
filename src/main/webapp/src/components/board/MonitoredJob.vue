<!--
  - MIT License
  -
  - Copyright (c) 2019 Oluwatobi Adeyinka
  -
  - Permission is hereby granted, free of charge, to any person obtaining a copy
  - of this software and associated documentation files (the 'Software'), to deal
  - in the Software without restriction, including without limitation the rights
  - to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  - copies of the Software, and to permit persons to whom the Software is
  - furnished to do so, subject to the following conditions:
  - The above copyright notice and this permission notice shall be included in all
  - copies or substantial portions of the Software.
  -
  - THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  - IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  - FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  - AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  - LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  - OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  - SOFTWARE.
-->

<template>
  <div :class="'transitions monitored-job ' + jobStateClass" :style="styleObject">
    <div
      class="progress-bar transitions"
      v-if="this.jobIsRunning()"
      :class="this.getProgressBarBlinkClass()"
      :style="{width: this.getProgressPercentage() + '%', 'min-width': '5%', 'background-color': this.getProgressBarColor()}">
      <span class="progress-percentage">{{this.getProgressPercentage()}}%</span>
    </div>

    <div class="monitored-job-top-row">
      <div class="top-row-content-wrapper">
        <a :href="'job/' + jobData.displayName" :class="{'monitored-job-name': true, 'npbm': true, 'disabled-job-name': !this.isBuildable()}">
            <img :src="jobData.healthReport.iconUrl" :title="jobData.healthReport.description" class="health-icon" :style="healthIconStyleObject"/> {{jobData.displayName}}
        </a>
        <div v-if="this.jobHasBeenRun()" class="monitored-job-summary" :style="summaryStyleObject">{{this.jobData.latestRun.summary}}</div>
      </div>
    </div>

    <div class="monitored-job-bottom-row">
      <a
        v-if="this.jobHasBeenRun()"
        :href="'job/' + jobData.displayName + '/' + jobData.latestRun.buildNumber"
        class="monitored-job-last-build transitions">
          {{this.jobData.latestRun.displayName}}
      </a>
      <a v-else :href="'job/' + jobData.displayName" class="monitored-job-last-build">No Builds</a>

      <img
        v-if="this.shouldDisplayRebuildButton()"
        :src="resourcesUrl + '/src/assets/img/repeat.png'"
        class="repeat-button transitions"
        title="Rebuild"
        @click="rebuild(jobData.latestRun.buildNumber)"
      >

      <div class="start-time" v-if="this.jobHasBeenRun()">{{this.prettyStartTime}} ago</div>
    </div>
  </div>
</template>

<script>
  import {Events} from '../../Events';
  import {StatusColors} from '../../StatusColors';

  import PrettyMilliseconds from 'pretty-ms'
  import SettingsManager from '../../util/SettingsManager';

  export default {
    name: 'MonitoredJob',
    props: ['resourcesUrl', 'board', 'installation', 'jobData'],

    created() {
      this.$root.$on(Events.COLUMN_COUNT_RETRIEVED, (event, columnCount) => {
        this.columnCountRetrievedHandler(event, columnCount)
      });

      this.$root.$on(Events.TOGGLED_HEALTH_DISPLAY, (event, setting) => {
        this.toggleHealthDisplay(event, setting);
      });

      this.$root.$on(Events.TOGGLED_SUMMARY_DISPLAY, (event, setting) => {
        this.toggleSummaryDisplay(event, setting);
      });
    },

    data() {
      let columnCount = SettingsManager.getColumnCount(this.board);
      let width = (100 / ++columnCount) + '%';

      return {
        styleObject: {
          'flex-grow': 1,
          width: width,
          'min-width': width
        },

        healthIconStyleObject: {
          display: SettingsManager.shouldDisplayJobHealth(this.board) ? 'inline-block' : 'none'
        },

        summaryStyleObject: {
          display: SettingsManager.shouldDisplayRunSummary(this.board) ? 'block' : 'none'
        }
      };
    },

    computed: {
      /**
       * Determine the class to be assigned to this job depending on the
       * result of the last complete run
       *
       * @returns string name of the class to be assigned
       */
      jobStateClass: function () {
        if (this.jobData.lastCompleteRun === null || !this.jobData.isBuildable) {
          return 'monitored-job-aborted';
        }

        switch (this.jobData.lastCompleteRun.result.name) {
          case 'SUCCESS': return 'monitored-job-successful';
          case 'FAILURE': return 'monitored-job-failed';
          case 'NOT_BUILT':
          case 'ABORTED': return 'monitored-job-aborted';
        }
      },

      prettyStartTime: function () {
        return PrettyMilliseconds(Date.now() - this.jobData.latestRun.startTime, {compact: true, verbose: true}).replace('~','');
      }
    },

    methods: {
      columnCountRetrievedHandler: function (event, columnCount) {
        let width = (100 / ++columnCount) + '%';
        this.styleObject = {
          'flex-grow': 1,
          width: width,
          'min-width': width
        }
      },

      rebuild: function (buildNumber) {
        if (SettingsManager.allowsAnalyticsTracking(this.board)) {
          this.$ga.event({
            eventCategory: 'rebuild',
            eventAction: 'rebuildButtonClicked',
            eventLabel: 'rebuildButtonClicked',
            eventValue: true
          });
        }

        let request = new XMLHttpRequest();
        let rebuildUrl = 'job/' + this.jobData.displayName + '/' + buildNumber + '/rebuild';
        let triggerMessage = 'Triggering rebuild for ' + this.jobData.displayName + ' #' + buildNumber;
        let successMessage = 'Triggered rebuild for ' + this.jobData.displayName + ' #' + buildNumber;
        let failureMessage = 'Failed to trigger rebuild for ' + this.jobData.displayName + ' #' + buildNumber +
                '. Do you have the Rebuild plugin installed?';

        this.$root.$emit(Events.NEW_INFORMATION, this.$event, triggerMessage, StatusColors.NORMAL);

        request.open('GET', rebuildUrl, true);
        request.onload = () => {
          if (request.status >= 200 && request.status < 400) {
            this.$root.$emit(Events.NEW_INFORMATION, this.$event, successMessage, StatusColors.SUCCESS);
          } else {
            this.$root.$emit(Events.NEW_INFORMATION, this.$event, failureMessage, StatusColors.ERROR);
          }
        };

        request.send();
      },

      jobHasBeenRun: function () {
        return this.jobData.latestRun !== null;
      },

      jobHasACompleteRun: function () {
        return this.jobData.lastCompleteRun !== null;
      },

      jobIsRunning: function () {
        return this.jobHasBeenRun() && this.jobData.latestRun.isRunning;
      },

      isBuildable: function () {
        return this.jobData.isBuildable;
      },

      shouldDisplayRebuildButton: function () {
        return this.installation.pluginManager.rebuildPluginIsInstalled && this.isBuildable() && this.jobHasBeenRun()
      },

      getProgressPercentage: function () {
        if (this.jobIsRunning()) {
          let duration = Date.now() - this.jobData.latestRun.startTime;
          let estimatedDuration = this.jobData.latestRun.estimatedDuration;
          let percentage = Math.ceil((duration / estimatedDuration) * 100);

          if (percentage < 0 || percentage > 100) {
            percentage = 100;
          }

          return percentage;
        }
        else return 0;
      },

      getProgressBarColor: function () {
        if (!this.jobHasACompleteRun()) {
          return '#ababab';
        }

        switch (this.jobData.lastCompleteRun.result.name) {
          case 'SUCCESS': return '#068226';
          case 'FAILURE': return '#A40302';
          case 'NOT_BUILT':
          case 'ABORTED': return '#3B3B3B';
        }
      },

      getProgressBarBlinkClass: function () {
        if (!this.jobHasACompleteRun()) {
          return 'blink-grey';
        }

        switch (this.jobData.lastCompleteRun.result.name) {
          case 'SUCCESS': return 'blink-green';
          case 'FAILURE': return 'blink-red';
          case 'NOT_BUILT':
          case 'ABORTED': return 'blink-grey';
        }
      },

      toggleHealthDisplay: function (event, setting) {
        this.healthIconStyleObject.display = setting ? 'inline-block' : 'none';
      },

      toggleSummaryDisplay: function (event, setting) {
        this.summaryStyleObject.display = setting ? 'block' : 'none';
      }
    }
  }
</script>

<style scoped>
  @-moz-keyframes green-blink {
    0% {background-color: rgba(9, 186, 54, 1)}
    50% {background-color: rgba(9, 186, 54, 0.5)}
    100% {background-color: rgba(9, 186, 54, 1)}
  }

  @-webkit-keyframes green-blink {
    0% {background-color: rgba(9, 186, 54, 1)}
    50% {background-color: rgba(9, 186, 54, 0.5)}
    100% {background-color: rgba(9, 186, 54, 1)}
  }

  @-ms-keyframes green-blink {
    0% {background-color: rgba(9, 186, 54, 1)}
    50% {background-color: rgba(9, 186, 54, 0.5)}
    100% {background-color: rgba(9, 186, 54, 1)}
  }

  @keyframes green-blink {
    0% {background-color: rgba(9, 186, 54, 1)}
    50% {background-color: rgba(9, 186, 54, 0.5)}
    100% {background-color: rgba(9, 186, 54, 1)}
  }

  @-moz-keyframes red-blink {
    0% {background-color: rgba(240, 4, 3, 1)}
    50% {background-color: rgba(240, 4, 3, 0.5)}
    100% {background-color: rgba(240, 4, 3, 1)}
  }

  @-webkit-keyframes red-blink {
    0% {background-color: rgba(240, 4, 3, 1)}
    50% {background-color: rgba(240, 4, 3, 0.5)}
    100% {background-color: rgba(240, 4, 3, 1)}
  }

  @-ms-keyframes red-blink {
    0% {background-color: rgba(240, 4, 3, 1)}
    50% {background-color: rgba(240, 4, 3, 0.5)}
    100% {background-color: rgba(240, 4, 3, 1)}
  }

  @keyframes red-blink {
    0% {background-color: rgba(240, 4, 3, 1)}
    50% {background-color: rgba(240, 4, 3, 0.5)}
    100% {background-color: rgba(240, 4, 3, 1)}
  }

  @-moz-keyframes grey-blink {
    0% {background-color: rgba(88, 88, 88, 1)}
    50% {background-color: rgba(88, 88, 88, 0.5)}
    100% {background-color: rgba(88, 88, 88, 1)}
  }

  @-webkit-keyframes grey-blink {
    0% {background-color: rgba(88, 88, 88, 1)}
    50% {background-color: rgba(88, 88, 88, 0.5)}
    100% {background-color: rgba(88, 88, 88, 1)}
  }

  @-ms-keyframes grey-blink {
    0% {background-color: rgba(88, 88, 88, 1)}
    50% {background-color: rgba(88, 88, 88, 0.5)}
    100% {background-color: rgba(88, 88, 88, 1)}
  }

  @keyframes grey-blink {
    0% {background-color: rgba(88, 88, 88, 1)}
    50% {background-color: rgba(88, 88, 88, 0.5)}
    100% {background-color: rgba(88, 88, 88, 1)}
  }

  .monitored-job {
    color: #ffffff;
    padding: 0;
    margin: 3px;
    border-radius: 7px;
    box-sizing: border-box;
    position: relative;
    display: inline-block;
    overflow: hidden;
  }

  .progress-bar {
    position: absolute;
    height: 100%;
    border-top-right-radius: 7px;
    border-bottom-right-radius: 7px;
    -moz-box-shadow: 0 0 7px -3px #000;
    -webkit-box-shadow: 0 0 7px -3px #000;
    box-shadow: 0 0 7px -3px #000;
  }

  .blink-green {
    -moz-animation: green-blink normal 3s infinite ease-in-out;
    -webkit-animation: green-blink normal 3s infinite ease-in-out;
    animation: green-blink normal 3s infinite ease-in-out;
  }

  .blink-red {
    -moz-animation: red-blink normal 3s infinite ease-in-out;
    -webkit-animation: red-blink normal 3s infinite ease-in-out;
    animation: red-blink normal 3s infinite ease-in-out;
  }

  .blink-grey {
    -moz-animation: grey-blink normal 3s infinite ease-in-out;
    -webkit-animation: grey-blink normal 3s infinite ease-in-out;
    animation: grey-blink normal 3s infinite ease-in-out;
  }

  .progress-percentage {
    padding: 10px;
    float: right;
    color: #ffffff;
    font-size: .9em;
    font-weight: 900;
  }

  .monitored-job a {
    text-decoration: none;
  }

  .monitored-job a:hover {
    text-decoration: underline;
  }

  .monitored-job a:focus {
    outline: none;
  }

  .monitored-job a:visited {
    color: #ffffff;
  }

  .monitored-job-successful {
    background-color: #068226;
  }

  .monitored-job-failed {
    background-color: #A40302;
  }

  .monitored-job-aborted {
    background-color: #3b3b3b;
  }

  .monitored-job-name {
    width: fit-content;
    font-size: 2.5em;
    font-weight: 700;
    text-align: center;
    display: block;
    margin: auto auto 10px;
    color: #ffffff;
    text-shadow: .5px .5px .5px #222222;
    word-wrap: break-word;
  }

  .disabled-job-name {
    text-decoration: line-through !important;
  }

  .monitored-job-summary {
    width: fit-content;
    margin: auto;
    font-weight: 700;
    text-shadow: .5px .5px .5px #222222;
  }

  .monitored-job-top-row {
    height: 100%;
    position: relative;
    z-index: 9;
  }

  .top-row-content-wrapper {
    height: max-content;
    position: absolute;
    top:0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto;
  }

  .monitored-job-bottom-row {
    height: 15px;
    z-index: 10;
    position: absolute;
    bottom: 15px;
    left: 10px;
    right: 10px;
  }

  .monitored-job-last-build {
    color: #ffffff;
    font-size: 1em;
    display: inline-block;
    font-weight: 900;
    opacity: .5;
  }

  .monitored-job-last-build:hover {
    opacity: 1;
  }

  .start-time {
    display: inline-block;
    color: #ffffff;
    font-size: 1em;
    font-weight: 900;
    float: right;
    opacity: .5;
  }

  .repeat-button {
    display: inline-block;
    cursor: pointer;
    opacity: .6;
    position: relative;
    top: 2px;
    width: 15px;
    height: 15px;
  }

  .repeat-button:hover {
    opacity: 1;
  }

  .health-icon {
      vertical-align: middle;
  }
</style>