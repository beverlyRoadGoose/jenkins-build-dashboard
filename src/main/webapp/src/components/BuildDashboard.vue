<template>
  <div id="dashboard">
    {{this.data}}
  </div>
</template>

<script>
  import {Constants} from "../constants";

  export default {
    name: "BuildDashboard",

    props: ['data'],

    mounted() {
      console.log(Constants.pluginInstance.getMonitoredJobsAsJSON());
    }
  }
</script>

<style scoped>

</style>