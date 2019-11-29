module.exports = {
  pluginOptions: {
    quasar: {
      rtlSupport: true,
      treeShake: true
    }
  },
  transpileDependencies: [
    /[\\/]node_modules[\\/]quasar[\\/]/,
  ],
  outputDir: 'target/dist',
  assetsDir: 'static'
};
