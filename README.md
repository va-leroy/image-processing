<div align="center">

# Image Processing web application

![](https://img.shields.io/badge/Made_with-TypeScript_v5.2.2-blue??style=flat&logo=TypeScript&logoColor=white)
![](https://img.shields.io/badge/Java-11-orange.svg)
![](https://img.shields.io/badge/License-MIT-blue.svg)
![](https://img.shields.io/badge/build-passing-limegreen.svg)

A simple **online image-editing application** that lets you quickly view and process images through the implementation of **several image-processing algorithms**, all on a clean, single-page web interface.

Powered by the **Java Spring Boot** framework, with its embedded **Apache Tomcat** server, as well as the **Vue.js** framework for its UI. The processing algorithms are implemented in Java using the **BoofCV** library.

[Overview](#🌱-overview) •
[Running the project](#🏃🏽-running-the-project)

</div>

## 🌱 Overview

This project is an extension from an assignment dealing about image processing algorithms as part of the BSc 3 Computer Science major at the University of Bordeaux.

Some of the algorithms featured in this project include: Brightness correction, Contrast correction, Gamma correction, Histogram equalization, Mean convolution, Gaussian convolution, Noise and Grain filters, Negative filter, Vignette filter, Grayscale filter, Horizontal flip, and Sobel edge detection.

<div align="center">
<img src="view/assets/example.gif" width="500" style="border-radius: 0.5em;"/>
</div>

## 🏃🏽 Running the project

### Before anything else

Before running the project, make sure you have the necessary dependencies installed on your machine:

```sh
cd view
npm i # or npm install
```

That's it, no need for an atrocious `npm audit fix` or anything like that.

### Running the client only

To run the client only, simply run the following command:

```sh
cd view
npm run dev
```

### Compiling the project

To compile the project, simply run the following command:

```sh
cd image-processing
mvn clean install
```

### Running the project

To run the project, simply run the following command:

```sh
cd image-processing
mvn --projects model spring-boot:run
```