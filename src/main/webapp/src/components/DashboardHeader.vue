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
  <div id="header">
    <a href="/" title="Jenkins Home" id="home-link">
      <img :src="resourcesUrl + '/src/assets/img/jenkins.png'" id="jenkins-logo"/>
    </a>

    <a :href="'/view/' + board + '/configure'" :title="'Configure ' + board" id="configure-link">
      <h1 id="board-title">{{boardTitle}}</h1>
    </a>
  </div>
</template>

<script>
  export default {
    name: "DashboardHeader",

    props: ['resourcesUrl', 'board', 'boardTitle']
  }
</script>

<style scoped>
  #header {
    padding: 3px;
    overflow: auto;
  }

  #home-link {
    display: inline-block;
    width: 40px;
    height: 40px;
    outline: none;
    float: left;
  }

  #jenkins-logo {
    width: 40px;
    height: 40px;
    outline: none;
  }

  #configure-link {
    float: left;
  }

  #board-title {
    color: #3B3D3B;
    display: inline-block;
    text-shadow: 1px 1px 1px #000000;
    margin: 2px 0 0 5px;
  }

  #board-title:hover {
    text-decoration: underline;
  }
</style>