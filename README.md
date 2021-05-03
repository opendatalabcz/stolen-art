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


**Each directory contains separate README.md** with details of usage.



## Installation



### Server

1. Install [Docker](https://docs.docker.com/get-docker/) on the host machine.
2. Install [Docker Compose](https://docs.docker.com/compose/install/)
3. Open terminal and navigate to the *server* directory.
4. Run this command to start the server:
```console
admin@admin:~$ docker-compose up
``` 
5. Wait for the server to start, [check if it is running](http://127.0.0.1:8000/api/painting/).


### Mobile app

You can download the apk file from this repository and simply install it on your Android device.
However, if you want to use the application with your server, you have to **modify the BASE_URL** variable to contain the URL of your server:
1. Open the *APIClient.java* file located in the networking package.
2. Change the URL
```java
private static final String BASE_URL = "http://111.111.111.111:88/";
``` 
3. Build and install the app.


### Jupyter notebooks

To run or view the notebooks [install Jupyter Software](https://jupyter.org/install).
Then run the jupyter notebook command from the projects directory.
```console
admin@admin:~$ jupyter notebook
```  

Because the notebooks depend on a lot of packages, it is **highly recommended** to use the project's conda environment.
Please [install Conda](https://conda.io/projects/conda/en/latest/user-guide/install/index.html) and then [see the CONDA.md file](https://github.com/opendatalabcz/stolen-art/blob/master/CONDA.md) for instructions on how to setup jupyter notebook to work with the environment.