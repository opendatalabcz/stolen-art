FROM python:3.7-alpine

# set an evironment variable PYTHONUNBUFFERED to 1
ENV PYTHONUNBUFFERED=1 

# copy our file on the docker image
COPY ./requirements.txt /requirements.txt

RUN apk add --update --no-cache postgresql-client jpeg-dev
RUN apk add --update --no-cache --virtual .tmp-build-deps \
        gcc libc-dev linux-headers postgresql-dev musl-dev zlib zlib-dev

# after moving it to the image, install it
RUN pip install -r /requirements.txt 

# delete temp req. used just for installing other things
RUN apk del .tmp-build-deps

RUN mkdir /app
WORKDIR /app
COPY ./app /app

RUN mkdir -p /vol/web/media
RUN mkdir -p /vol/web/static

# create user with not root access
RUN adduser -D user 

RUN chown -R user:user /vol/
RUN chmod -R 775 /vol/web

# activate the user
USER user 

