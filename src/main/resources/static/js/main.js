Vue.use(VueRouter);
Vue.use(VeeValidate, {
  locale: 'ko'
});

Vue.component('login', httpVueLoader('./template/user/login.vue'));
Vue.component('signin', httpVueLoader('./template/user/signin.vue'));
Vue.component('search', httpVueLoader('./template/main/main.vue'));

const routes = [
  {
    path: '/',
    name: 'login',
    component: httpVueLoader('./template/user/login.vue'),
  },
  {
    path: '/signin',
    name: 'signin',
    component: httpVueLoader('./template/user/signin.vue'),
  },
  {
    path: '/search',
    name: 'search',
    component: httpVueLoader('./template/search/search.vue'),
  }
]

const router = new VueRouter({
  routes
})

var $app = new Vue({
  el: '#app',
  watch: {},
  data: {
    user: {
      savedId: "",
      exp: "",
      id: "",
      iss: ""
    }
  },

  created() {
    this.setLoginUserId();
  },

  methods: {
    mvLogin: function () {
      router.push("/");
    },

    setLoginUserId: function () {
      try {
        let token = this.getCookie('apiKey');
        if (token) {
          let base64Url = token.split('.')[1];
          let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
          let jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
          }).join(''));

          this.user = JSON.parse(jsonPayload);
        }
      } finally {
      }
    },

    getCookie: function (name) {
      let value = "; " + document.cookie;
      let parts = value.split("; " + name + "=");
      if (parts.length == 2) return parts.pop().split(";").shift();
    }
  },
  router
});