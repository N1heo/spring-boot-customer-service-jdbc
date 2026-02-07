module.exports = {
  devServer: {
    port: 4446,
    proxy: {
      "/api": {
        target: "http://localhost:4445",
        changeOrigin: true,
      },
      "/register": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/login": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
      "/customers": {
        target: "http://localhost:8080",
        changeOrigin: true
      }
    }
  },

  transpileDependencies: [
    "vue-router",
    "birpc",
    "@vue/devtools-kit",
    "@vue/devtools-api",
    "vee-validate"
  ]
};
