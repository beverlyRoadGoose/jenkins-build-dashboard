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
  <div id="settings-widget">
    <img
        :src="resourcesUrl + '/src/assets/img/settings-dark.png'"
        id="settings-icon"
        class="transitions"
        title="Settings"
        :style="settingsIconStyleObject"
        @click="toggleSettingsBoxVisibility"
    />

    <div id="settings-box" :style="settingsBoxStyleObject">
      <div>
        Columns: {{columnCount}}
        <input
            type="range"
            min="1"
            max="6"
            v-model="columnCount"
            v-bind:title="columnCount"
            v-on:change="updateBoardLayout(columnCount)"
            id="column-slider"
        >
      </div>

      <div class="option-wrapper">
        <input
          type="checkbox"
          name="health"
          :value="displayJobHealth"
          v-model="displayJobHealth"
          @click="toggleJobHealthDisplay($event)">
            Show build stability icons
        <br>
      </div>

      <div class="option-wrapper">
       <input
          type="checkbox"
          name="summary"
          :value="displayRunSummary"
          v-model="displayRunSummary"
          @click="toggleRunSummaryDisplay($event)">
            Show run description
        <br>
      </div>

      <div>
        <input
          type="checkbox"
          name="analytics"
          :value="allowsAnalyticsTracking"
          v-model="allowsAnalyticsTracking"
          @click="toggleAnalyticsTracking($event)">
            Allow analytics tracking
        <br>
      </div>

      <a href="configure" id="settings-configure-link" :title="'Configure ' + board">Board configuration</a>

      <button id="settings-close-button" v-on:click="toggleSettingsBoxVisibility">Close</button>
    </div>
  </div>
</template>

<script>
  import {Events} from "../../Events";
  import SettingsManager from '../../util/SettingsManager';

  export default {
    name: 'SettingsWidget',

    props: ['resourcesUrl', 'board'],

    data() {
      return {
        settingsBoxStyleObject: {
          display: 'none'
        },

        settingsIconStyleObject: {
          opacity: .5
        },

        columnCount: SettingsManager.getColumnCount(this.board),
        displayJobHealth: SettingsManager.shouldDisplayJobHealth(this.board),
        displayRunSummary: SettingsManager.shouldDisplayRunSummary(this.board),
        allowsAnalyticsTracking: SettingsManager.allowsAnalyticsTracking(this.board)
      };
    },

    methods: {
      toggleSettingsBoxVisibility: function () {
        switch (this.settingsBoxStyleObject.display) {
          case 'none': {
            this.settingsIconStyleObject.opacity = 1;
            this.settingsBoxStyleObject.display = 'block';
          }break;

          case 'block': {
            this.settingsIconStyleObject.opacity = .5;
            this.settingsBoxStyleObject.display = 'none';
          } break;

          default: {
            this.settingsIconStyleObject.opacity = .5;
            this.settingsBoxStyleObject.display = 'none';
          }
        }
      },

      toggleJobHealthDisplay: function(event) {
        this.$root.$emit(Events.TOGGLED_HEALTH_DISPLAY, this.$event, event.target.checked);
        this.displayJobHealth = event.target.checked;
        SettingsManager.setJobHealthDisplaySetting(this.board, event.target.checked);
      },

      toggleRunSummaryDisplay: function(event) {
        this.$root.$emit(Events.TOGGLED_SUMMARY_DISPLAY, this.$event, event.target.checked);
        this.displayRunSummary = event.target.checked;
        SettingsManager.setRunSummaryDisplaySetting(this.board, event.target.checked);
      },

      toggleAnalyticsTracking: function(event) {
        this.allowsAnalyticsTracking = event.target.checked;
        SettingsManager.setAnalyticsTracking(this.board, event.target.checked);
      },

      updateBoardLayout: function (value) {
        this.$root.$emit(Events.COLUMN_COUNT_RETRIEVED, this.$event, value);
        SettingsManager.saveColumnCount(this.board, value);
      }
    }
  }
</script>

<style scoped>
  #settings-widget {
    position: absolute;
    right: 0;
    top: 0;
    z-index: 20;
  }

  #settings-icon {
    width: 30px;
    height: 30px;
    margin: 20px 10px 10px;
  }

  #settings-icon:hover {
    cursor: pointer;
  }

  #settings-icon:focus {
    outline: none;
  }

  #settings-box {
    position: absolute;
    top: 57px;
    right: 10px;
    padding: 10px 10px;
    border-radius: 5px;
    background-color: rgba(50, 50, 50, .7);
    border: 1px solid rgba(50, 50, 50, 1);
    color: #ffffff;
    width: 200px;
    font-size: .8em;
    z-index: 10;
  }

  .option-wrapper {
    margin-bottom: 10px;
  }

  #column-slider {
    width: 100%;
    margin-bottom: 15px;
  }

  #column-slider:hover {
    cursor: pointer;
  }

  #settings-configure-link {
    text-decoration: none;
    text-align: center;
    display: block;
    color: #3B3D3B;
    margin-top: 20px;
    padding: 5px;
    border-radius: 3px;
    border: 1px solid #cacaca;
    background-color: #E6E6E6;
    background-image: -o-linear-gradient(top, #E6E6E6, #CCCCCC);
    background-image: -ms-linear-gradient(top, #E6E6E6, #CCCCCC);
    background-image: -moz-linear-gradient(top, #E6E6E6, #CCCCCC);
    background-image: -webkit-linear-gradient(top, #E6E6E6, #CCCCCC);
    background-image: -webkit-gradient(linear, left top, left bottom, from(#E6E6E6), to(#CCCCCC));
    filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#E6E6E6, endColorstr=#CCCCCC);
    background-image: linear-gradient(to bottom, #E6E6E6, #CCCCCC);
  }

  #settings-configure-link:hover {
    border: 1px solid #b3b3b3;
    background-color: #cdcdcd;
    background-image: -o-linear-gradient(top, #cdcdcd, #b3b3b3);
    background-image: -ms-linear-gradient(top, #cdcdcd, #b3b3b3);
    background-image: -moz-linear-gradient(top, #cdcdcd, #b3b3b3);
    background-image: -webkit-linear-gradient(top, #cdcdcd, #b3b3b3);
    background-image: -webkit-gradient(linear, left top, left bottom, from(#cdcdcd), to(#b3b3b3));
    filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#cdcdcd, endColorstr=#b3b3b3);
    background-image: linear-gradient(to bottom, #cdcdcd, #b3b3b3);
  }

  #settings-close-button {
    text-decoration: none;
    text-align: center;
    display: block;
    color: #ffffff;
    margin-top: 10px;
    width: 100%;
    padding: 7px;
    border-radius: 3px;
    border:1px solid #616261;
    background-color: #7d7e7d;
    font-size: .9em;
    background-image: -o-linear-gradient(top, #7d7e7d, #0e0e0e);
    background-image: -ms-linear-gradient(top, #7d7e7d, #0e0e0e);
    background-image: -moz-linear-gradient(top, #7d7e7d, #0e0e0e);
    background-image: -webkit-linear-gradient(top, #7d7e7d, #0e0e0e);
    background-image: -webkit-gradient(linear, left top, left bottom, from(#7d7e7d), to(#0e0e0e));
    filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#7d7e7d, endColorstr=#0e0e0e);
    background-image: linear-gradient(to bottom, #7d7e7d, #0e0e0e);
  }

  #settings-close-button:hover {
    border:1px solid #4a4b4a;
    background-color: #646464;
    background-image: -o-linear-gradient(top, #646464, #282828);
    background-image: -ms-linear-gradient(top, #646464, #282828);
    background-image: -moz-linear-gradient(top, #646464, #282828);
    background-image: -webkit-linear-gradient(top, #646464, #282828);
    background-image: -webkit-gradient(linear, left top, left bottom, from(#646464), to(#282828));
    filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr=#646464, endColorstr=#282828);
    background-image: linear-gradient(to bottom, #646464, #282828);
    cursor: pointer;
  }

  #settings-close-button:focus {
    outline: none;
  }
</style>