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
        &nbsp;Columns:
        <input type="range" min="1" max="6" id="column-slider">
      </div>
      <a href="configure" id="settings-configure-link" :title="'Configure ' + board">Board configuration</a>
      <button id="settings-close-button" v-on:click="toggleSettingsBoxVisibility">Close</button>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'SettingsWidget',

    props: ['resourcesUrl', 'board'],

    data: function () {
      return {
        settingsBoxStyleObject: {
          display: 'none'
        },

        settingsIconStyleObject: {
          opacity: .5
        }
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
      }
    }
  }
</script>

<style scoped>
  #settings-widget {
    position: absolute;
    right: 0;
    top: 0;
  }

  #settings-icon {
    width: 30px;
    height: 30px;
    margin: 17px 13px 13px;
  }

  #settings-icon:hover {
    cursor: pointer;
  }

  #settings-icon:focus {
    outline: none;
  }

  #settings-box {
    position: absolute;
    top: 50px;
    right: 13px;
    padding: 10px 10px;
    border-radius: 5px;
    background-color: rgba(50, 50, 50, .7);
    border: 1px solid rgba(50, 50, 50, 1);
    color: #ffffff;
    width: 200px;
    font-size: .8em;
    text-shadow: 1px 1px 1px #000000;
  }

  #column-slider {
    width: 100%;
  }

  #column-slider:hover {
    cursor: pointer;
  }

  #settings-configure-link {
    text-decoration: none;
    text-align: center;
    display: block;
    color: #ffffff;
    margin-top: 10px;
    padding: 5px;
    border-radius: 3px;
    border:1px solid #cacaca;
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
    border:1px solid #b3b3b3;
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
    text-shadow: 1px 1px 1px #000000;
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