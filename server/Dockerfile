# latest Debian version available
FROM debian:latest

ENV PYTHONUNBUFFERED 1

# copy the requirements file from the local build context into the container
COPY ./requirements.txt /requirements.txt

# running update and installing some dependencies, pip3 install
RUN apt-get update
RUN apt-get install -y postgresql-client postgresql
RUN apt-get install -y libjpeg-dev
RUN apt-get install -y libgl1-mesa-glx
RUN apt-get install -y libpq-dev

# installing python3 + pip
RUN apt-get install -y python3
RUN apt-get install -y python3-dev
RUN apt-get install -y python3-pip
RUN pip3 install scikit-build
RUN pip3 install requests
RUN pip3 install --upgrade setuptools pip

# installing requirements
RUN pip3 install -r /requirements.txt

# creating the server directory and copying it from the local build context
RUN mkdir /app
WORKDIR /app
COPY ./app /app


RUN mkdir -p /vol/web/media
RUN mkdir -p /vol/web/static
RUN adduser user
RUN chown -R user:user /vol/
RUN chmod -R 755 /vol/web
USER user
