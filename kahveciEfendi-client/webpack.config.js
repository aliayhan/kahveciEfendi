var webpack = require('webpack');
var path    = require('path');

module.exports = {

  entry: {
    'vendor': './app/vendor.ts',
    'app':    './app/main.ts',
  },

  output: {
    path: __dirname,
    filename: 'build/[name].js',
  },

  devtool: 'source-map',

  devServer: {
    inline:             true,
    hot:               false,
    historyApiFallback: true,
    stats:         'minimal',
  },

  watchOptions: {
    aggregateTimeout: 1500
  },

  resolve: { extensions: ['.js', '.ts'] },

  module: {
    loaders: [
      {
        test: /\.ts$/,
        loaders: ['awesome-typescript-loader', 'angular2-template-loader'],
      },
      {
        test: /\.html$/,
        loader: "html-loader?minimize=false",
      },
      {
        test: /\.css$/,
        loader: ['exports-loader?module.exports.toString()', 'css-loader'],
        include: path.resolve(__dirname, "app"),
      },
      {
        test: /\.css$/,
        loader: 'style-loader!css-loader',
        exclude: path.resolve(__dirname, 'app'),
      },
      {
        test: /\.(png|jpg|gif|svg|woff|woff2|ttf|eot)$/,
        loader: 'url-loader?limit=25000',
      },
    ]
  },


  plugins: [
    new webpack.ProvidePlugin({ 'THREE': 'three' }),
    new webpack.optimize.CommonsChunkPlugin({ name: ['app', 'vendor'] }),
    new webpack.LoaderOptionsPlugin({ debug: true }),
    new webpack.ContextReplacementPlugin(
        /angular(\\|\/)core(\\|\/)(esm(\\|\/)src|src)(\\|\/)linker/, __dirname),
  ]
};
