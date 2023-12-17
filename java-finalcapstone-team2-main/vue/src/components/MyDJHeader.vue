<template>
  <header>
    <div class="logo">
      <router-link to="/landing"
        ><img src="..\assets\auxbox-logo-3.png" id="logo" />
      </router-link>
    </div>

    <div class="nav">
      <router-link to="/landing">Home</router-link>
      <router-link to="/about">About Us</router-link>
      <router-link
        :to="{ name: 'DJHomePage', params: { userName: user.username } }"
        >DJ Homepage</router-link
      >
      <router-link to="/logout">Logout</router-link>
      <i class="fa-regular fa-circle-user fa-2xl"></i>
      <div class="username">DJ {{ user.username }}</div>
    </div>
  </header>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "my-dj-header",

  computed: mapState({ user: "user" }),
};
</script>

<style scoped>
.fa-circle-user {
  color: #46277a;
  padding-left: 15px;
  padding-right: 10px;
}

.username {
  font-size: 16px;
  color: #46277a;
  font-family: "Pontano Sans", sans-serif;
  font-weight: bold;
  text-align: center;
}

header {
  height: 150px;
}

.slogan {
  display: flex;
  align-items: center;
  font-size: 22px;
}
</style>