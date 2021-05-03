# Stolen Art Finder

This GitHub repository contains all source codes for my bachelor thesis. The software includes a mobile client, server, and database, which can be used for stolen painting recognition.

The repo has three main parts:
1. Mobile application for Android OS
   - written in Java
   - developed in Android Studio
   - full project in */client/Stolen-Art-Finder/* directory
2. Dockerized Django server
   - server side of the project
   - easily installed using Docker
   - two separate containers for django and database
   - implements RESTful API
   - the painting recognition occurs on the server
   - full source code in *server* directory
3. Jupyter notebooks
   - notebooks were used for downloading/augmenting paintings and for demonstration purposes
   - data augmentation notebook, data downloader notebook, orb demonstration notebook
   - all notebooks in *jupyter_notebooks* directory 

# Datasets

## Imgaug
- imgaug library
- defining a imgaug.Sequential object with augmetation "layers"
- loop over images and apply the sequence on each of them
- the result (augmented image) is shown next to the original
=======

## The Tate Collection
- thousands rows of data
- various forms of art, but you can easily filter by medium
- https://github.com/tategallery/collection
