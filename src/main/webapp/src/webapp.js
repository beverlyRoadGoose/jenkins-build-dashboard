import Vue from 'vue';
import VueHead from 'vue-head';
import VueResource from 'vue-resource';
import BuildDashboard from './components/BuildDashboard'

Vue.use(VueHead);
Vue.use(VueResource);

require('./styles/base.css');

window.onload = () => {
  new Vue({
    el: '#webapp',

    components: {
      BuildDashboard
    }
  });
};