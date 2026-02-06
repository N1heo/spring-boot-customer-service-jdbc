module.exports = {
  devServer: {
    port: 4446,
    proxy: {
      "/api": {
        target: "http://localhost:4445",
        changeOrigin: true,
      },
    },
  },
  transpileDependencies: [
    "vue-router",
    "birpc",
    "@vue/devtools-kit",
    "@vue/devtools-api",
    "vee-validate"
  ]
};
export default {
  server: {
    proxy: {
      "/register": "http://localhost:8080",
      "/login": "http://localhost:8080",
      "/customers": "http://localhost:8080"
    }
  }
}
