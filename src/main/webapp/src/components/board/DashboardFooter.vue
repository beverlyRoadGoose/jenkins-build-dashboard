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
  <div id="footer">
    <a href="https://github.com/beverlyRoadGoose/jenkins-build-dashboard" class="footer-link" target="_blank">Build Dashboard Plugin</a>
    by <a href="https://github.com/beverlyRoadGoose" class="footer-link" target="_blank">Oluwatobi Adeyinka</a>
    <a href="https://github.com/beverlyRoadGoose/jenkins-build-dashboard/releases" id="build-version" target="_blank">v{{build.version}}</a>
  </div>
</template>

<script>
  export default {
    name: 'DashboardFooter',

    props: ['build']
  }
</script>

<style scoped>
  #footer {
    padding: 0 10px 5px;
    color: #3B3D3B;
    font-size: .8em;
  }

  #footer a {
    color: #38B0DE;
    text-decoration: none;
  }

  #footer a:hover {
    color: #38B0DE;
    text-decoration: underline;
  }

  #build-version {
    float: right;
  }
</style>