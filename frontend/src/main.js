import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import {FontAwesomeIcon} from "./plugins/font-awesome";

import axios from "axios";


axios.defaults.withCredentials = true;


let csrfInitPromise = null;


function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) {
    return parts.pop().split(";").shift();
  }
  return null;
}


async function waitForCookie(name, maxAttempts = 10, delay = 50) {
  for (let i = 0; i < maxAttempts; i++) {
    const cookie = getCookie(name);
    if (cookie) {
      return cookie;
    }
    await new Promise(resolve => setTimeout(resolve, delay));
  }
  return null;
}


function resetCsrf() {
  csrfInitPromise = null;
}

async function initCsrf() {
  if (!csrfInitPromise) {
    csrfInitPromise = axios
        .get("/api/csrf")
        .then(async () => {
          await waitForCookie("XSRF-TOKEN");
        })
        .catch(() => {
          csrfInitPromise = null;
        });
  }
  return csrfInitPromise;
}


initCsrf();


axios.interceptors.request.use(async (config) => {
  const method = (config.method || "get").toLowerCase();

  if (["post", "put", "patch", "delete"].includes(method)) {
    let csrf = getCookie("XSRF-TOKEN");

    if (!csrf) {
      resetCsrf();
      await initCsrf();
      csrf = await waitForCookie("XSRF-TOKEN");
    }

    if (csrf) {
      config.headers = config.headers || {};
      config.headers["X-XSRF-TOKEN"] = csrf;
    }
  }

  return config;
});


createApp(App)
    .use(router)
    .use(store)
    .component("font-awesome-icon", FontAwesomeIcon)
    .mount("#app");
