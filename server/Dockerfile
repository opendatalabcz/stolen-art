FROM debian:latest

ENV PYTHONUNBUFFERED 1

COPY ./requirements.txt /requirements.txt
RUN apt-get update
RUN apt-get install -y postgresql-client postgresql
RUN apt-get install -y libjpeg-dev
RUN apt-get install -y libgl1-mesa-glx
RUN apt-get install -y libpq-dev
#RUN apt-get install -y gcc libc-dev linux-headers postgresql-dev musl-dev zlib zlib-dev
RUN apt-get install -y python3
RUN apt-get install -y python3-dev
RUN apt-get install -y python3-pip
RUN pip3 install scikit-build
RUN pip3 install --upgrade setuptools pip
RUN pip3 install -r /requirements.txt

RUN mkdir /app
WORKDIR /app
COPY ./app /app

RUN mkdir -p /vol/web/media
RUN mkdir -p /vol/web/static
RUN adduser user
RUN chown -R user:user /vol/
RUN chmod -R 755 /vol/web
USER user