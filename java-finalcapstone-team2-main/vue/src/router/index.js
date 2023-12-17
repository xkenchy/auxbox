import Vue from 'vue'
import Router from 'vue-router'
// import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Logout from '../views/Logout.vue'
import Register from '../views/Register.vue'
import store from '../store/index'
import Landing from '../views/Landing.vue'
import GuestPartyPage from '../views/GuestPartyPage.vue'
import About from '../views/About.vue'
import DJCreateParty from '../views/DJCreateParty.vue'
import DJHomePage from '../views/DJHomePage.vue'


import Gif from '../views/Gif.vue'
import Splash from '../views/Splash.vue'
import DJPartyPage from '../views/DJPartyPage.vue'

Vue.use(Router)

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,

  routes: [
    
    {
      path: '/gif',
      name: 'gif',
      component: Gif
    },


    {
      path: '/landing',
      name: 'landing',
      component: Landing,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: '/',
      name: 'splash',
      component: Splash,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/logout",
      name: "logout",
      component: Logout,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/party/:partyId",
      name: "partyPage",
      component: GuestPartyPage,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/about",
      name: "about",
      component: About,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/createparty",
      name: "DJCreateParty",
      component: DJCreateParty,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/DJhomepage/:userName",
      name: "DJHomePage",
      component: DJHomePage,
      meta: {
        requiresAuth: false
      }
    },

    {
      path: "/DJparty/:partyId",
      name: "DJPartyPage",
      component: DJPartyPage,
      meta: {
        requiresAuth: false
      }

    }

  ]
})

router.beforeEach((to, from, next) => {
  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    next("/login");
  } else {
    // Else let them go to their next destination
    next();
  }
});

export default router;
