<!-- [![version](https://badge.fury.io/gh/beverlyRoadGoose%2jenkins-build-dashboard.svg)](https://github.com/beverlyRoadGoose/jenkins-build-dashboard/releases) -->
[![CircleCI](https://circleci.com/gh/beverlyRoadGoose/jenkins-build-dashboard.svg?style=svg&circle-token=e5e2acc9dc19e91cee13b063f4937fbbcc474df0)](https://circleci.com/gh/beverlyRoadGoose/jenkins-build-dashboard)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Jenkins Build Dashboard
![Demo](https://i.imgur.com/5gjPlvT.gif)

This is a plugin for [Jenkins](https://jenkins.io/) that makes it possible to create customisable dashboards for monitoring the status of your jobs or builds.

## Features
- Customisable layout for each board
- Displays an automatically updated status of the jobs added to a board
- Runs can be re-triggered from the board (requires the jenkins [rebuild plugin](https://github.com/jenkinsci/rebuild-plugin))

## Running the plugin
You need to have [Gradle](https://gradle.org/) installed.

### Running locally
The quickest way to get the plugin running is by running this command from the root directory of the repo to spin 
up a local instance of jenkins with the plugin installed which you can access on `localhost:8080`
```console
foo@bar:~$ gradle server
```

### Running on Docker
A Dockerfile is available in the root of the project if you'd prefer to run Jenkins & the plugin in a [Docker](https://www.docker.com/) container.

### Installing on an existing jenkins instance
If you want to install the plugin on an existing instance of jenkins, you need to generate the jpi file and then install
it or your server. Generate the file by running:
```console
foo@bar:~$ gradle dist
```
You can find the generated jpi file in the root directory of the repo.

The plugin is developed against Jenkins version `2.164.3` and isn't tested on earlier versions, it would probably work fine,
but there are no guarantees.

## Usage
To create a board, go to the `/newView` page on jenkins, enter a name for the dashboard and select `Build Dashboard` as 
the type.

On the view configuration page you can select the jobs you want to monitor on the dashboard and set a title for the board if
you want something different from the name. After configuring the board, you can access it on `/view/{board name}`

## License

```
MIT License

Copyright (c) 2019 Oluwatobi Adeyinka

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
