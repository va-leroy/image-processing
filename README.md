<div align="center">

# Image Processing web application

![](https://img.shields.io/badge/TypeScript-5.2.2-blue??style=flat&logo=TypeScript&logoColor=white)
![](https://img.shields.io/badge/Java-11-orange.svg)
![](https://img.shields.io/badge/License-MIT-blue.svg)

A simple **online image-editing application** that lets you quickly view and process images through the implementation of **several image-processing algorithms**, all on a clean, single-page web interface.

Powered by the **Java Spring Boot** framework, with its embedded **Apache Tomcat** server, as well as the **Vue.js** framework for its UI. The processing algorithms are implemented in Java using the **BoofCV** library.

[Overview](#🌱-overview) •
[Running the project](#🏃🏽-running-the-project)

</div>

## 🌱 Overview

### About the project

This project is an extension from an assignment dealing about image processing algorithms as part of the BSc 3 Computer Science major at the University of Bordeaux.

<div align="center">
<img src="view/assets/example.gif" width="500" style="border-radius: 0.5em;"/>
</div>

### Implemented algorithms

The following processing algorithms are implemented in the project:

| Algorithm               | Description                        |
|-------------------------|------------------------------------|
| Brightness correction   | Adjusts the overall brightness of an image. |
| Contrast correction     | Adjusts the difference in brightness between the light and dark areas of an image. |
| Gamma correction        | Alters the luminance of an image using a gamma value. |
| Histogram equalization  | Improves the contrast of an image by spreading out the intensity values. |
| Mean convolution        | Applies a convolution operation using a mean kernel to smooth an image. |
| Gaussian convolution    | Applies a convolution operation using a Gaussian kernel for blurring or smoothing an image. |
| Noise and Grain filters | Adds or removes noise and graininess from an image. |
| Negative filter         | Inverts the colors of an image. |
| Vignette filter         | Darkens the corners or edges of an image to create a vignette effect. |
| Grayscale filter        | Converts an image to grayscale. |
| Horizontal flip         | Flips an image horizontally. |
| Sobel edge detection    | Detects edges in an image using the Sobel operator. |

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