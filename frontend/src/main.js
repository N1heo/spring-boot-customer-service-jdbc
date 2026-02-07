import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import {FontAwesomeIcon} from "./plugins/font-awesome";

import axios from "axios";

axios.defaults.baseURL = "http://localhost:4445";

axios.defaults.withCredentials = true;

axios.interceptors.response.use(
    r => r,
    err => {
        if (err.response?.status === 401 || err.response?.status === 403) {
            localStorage.removeItem("user");
            window.location = "/login";
        }
        return Promise.reject(err);
    }
);

axios.interceptors.request.use(config => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (user?.accessToken) {
        config.headers.Authorization = "Bearer " + user.accessToken;
    }
    return config;
});

createApp(App)
    .use(router)
    .use(store)
    .component("font-awesome-icon", FontAwesomeIcon)
    .mount("#app");
