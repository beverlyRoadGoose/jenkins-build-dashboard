const path = require('path');
const webpack = require('webpack');
const { VueLoaderPlugin } = require('vue-loader');
const resolve = relativePath => path.resolve(__dirname, '..', relativePath);

module.exports = {

    mode: 'development',

    entry: {
        vue: 'vue',
        index: resolve('src/webapp.js'),
    },

    output: {
        filename: 'webapp.js',
        path: resolve('webapp'),
        publicPath: '../webapp/',
    },

    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader',
                options: {
                    loaders: {
                        css: ['vue-style-loader', {
                            loader: 'css-loader',
                        }],
                        js: [
                            'babel-loader',
                        ],
                    },
                    cacheBusting: true,
                },
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                include: [
                    resolve('app'),
                    resolve('node_modules/webpack-dev-server/client'),
                ],
            },
            {
                test: /\.css$/,
                use: [
                    'vue-style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.(gif|png|jpe?g|svg)$/i,
                use: [
                    'file-loader',
                    {
                        loader: 'image-webpack-loader',
                        options: {
                        }
                    },
                ]
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/,
                use: [
                    'file-loader'
                ]
            }
        ]
    },

    plugins: [
        new VueLoaderPlugin(),
        new webpack.NamedModulesPlugin(),
        new webpack.HotModuleReplacementPlugin()
    ],

    resolve: {
        alias: {
            'vue$': 'vue/dist/vue.esm.js',
        },
        extensions: ['*', '.vue', '.js', '.json', '.css'],
    },

    performance: {
        hints: false,
    }

};