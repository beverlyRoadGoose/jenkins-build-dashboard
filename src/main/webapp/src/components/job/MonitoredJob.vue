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
  <div :class="'monitored-job transitions ' + jobStateClass" :style="styleObject">
    <a
        v-if="jobData.latestRunNumber > 0"
        :href="'job/' + jobData.displayName + '/' + jobData.latestRunNumber"
        class="monitored-job-last-build">
      {{jobData.latestRunName}}
    </a>
    <a v-else :href="'job/' + jobData.displayName" class="monitored-job-last-build">No Builds</a>

    <img
        v-if="jobData.latestRunNumber > 0"
        :src="resourcesUrl + '/src/assets/img/repeat.png'"
        class="repeat-button transitions"
        title="Rebuild"
        @click="rebuild(jobData.latestRunNumber)"
    >

    <div class="monitored-job-mid-row">
      <a v-bind:href="'job/' + jobData.displayName" class="monitored-job-name npbm">{{jobData.displayName}}</a>
    </div>
  </div>
</template>

<script>
  import {Events} from '../../Events';
  import {StatusColors} from '../../StatusColors';
  import CookieManager from '../../util/CookieManager';

  export default {
    name: 'MonitoredJob',

    props: ['resourcesUrl', 'board', 'jobData'],

    created() {
      this.$root.$on(Events.COLUMN_COUNT_RETRIEVED, (event, columnCount) => {
        this.columnCountRetrievedHandler(event, columnCount)
      });
    },

    data() {
      let columnCount = CookieManager.cookieExists(this.board.split(' ').join('_') + '_columnCount') ? this.getColumnCountFromCookie() : 1;
      let width = (100 / ++columnCount) + '%';

      return {
        styleObject: {
          'flex-grow': 1,
          width: width,
          'min-width': width
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
        switch (this.jobData.lastCompleteRunResult.name) {
          case 'SUCCESS': return 'monitored-job-successful';
          case 'FAILURE': return 'monitored-job-failed';
          case 'NOT_BUILT': return 'monitored-job-aborted';
          case 'ABORTED': return 'monitored-job-aborted';
        }
      }
    },

    methods: {
      getColumnCountFromCookie: function () {
        return CookieManager.readCookie(this.board.split(' ').join('_') + '_columnCount');
      },

      columnCountRetrievedHandler: function (event, columnCount) {
        let width = (100 / ++columnCount) + '%';
        this.styleObject = {
          'flex-grow': 1,
          width: width,
          'min-width': width
        }
      },

      rebuild: function (buildNumber) {
        let request = new XMLHttpRequest();
        let rebuildUrl = 'job/' + this.jobData.displayName + '/' + buildNumber + '/rebuild';
        let triggerMessage = 'Triggering rebuild for ' + this.jobData.displayName + ' #' + buildNumber;
        let successMessage = 'Triggered rebuild for ' + this.jobData.displayName + ' #' + buildNumber;
        let failureMessage = 'Failed to trigger rebuild for ' + this.jobData.displayName + ' #' + buildNumber;

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
      }
    }
  }
</script>

<style scoped>
  .monitored-job {
    color: #3B3D3B;
    padding: 10px;
    margin: 3px;
    border-radius: 7px;
    box-sizing: border-box;
    box-shadow: 1px 1px 1px #cccccc;
    position: relative;
    display: inline-block;
    border: 1px solid #999999;
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
    color: #3B3D3B;
  }

  .monitored-job-successful {
    background-color: #E0F2E0;
  }

  .monitored-job-failed {
    background-color: #FFC6C3;
  }

  .monitored-job-aborted {
    background-color: #d2d2d2;
  }

  .monitored-job-name {
    width: max-content;
    font-size: 1.5em;
    text-align: center;
    color: #3B3D3B;
    display: inline-block;
    text-shadow: 1px 1px 1px #000000;
  }

  .monitored-job-mid-row {
    margin-bottom: 5px;
  }

  .monitored-job-last-build {
    color: #3B3D3B;
    font-size: 0.8em;
    display: inline-block;
    margin: 0;
  }

  .repeat-button {
    display: inline-block;
    cursor: pointer;
    opacity: .5;
    position: relative;
    top: 2px;
    width: 13px;
    height: 13px;
  }

  .repeat-button:hover {
    opacity: 1;
  }
</style>