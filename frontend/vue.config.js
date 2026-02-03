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
