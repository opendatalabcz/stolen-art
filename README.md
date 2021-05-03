# Stolen Art Finder

This GitHub repository contains all source codes for my bachelor thesis. The software includes a mobile client, server, and database, which can be used for stolen painting recognition.

The repo has three main parts:
1. **Mobile application for Android OS**
   - written in Java
   - developed in Android Studio
   - full project in */client/Stolen-Art-Finder/* directory
2. **Dockerized Django server**
   - server side of the project
   - easily installed using Docker
   - two separate containers for django and database
   - implements RESTful API
   - the painting recognition occurs on the server
   - full source code in *server* directory
3. **Jupyter notebooks**
   - notebooks were used for downloading/augmenting paintings and for demonstration purposes
   - data augmentation notebook, data downloader notebook, orb demonstration notebook
   - all notebooks in *jupyter_notebooks* directory 





## Installation



### Server