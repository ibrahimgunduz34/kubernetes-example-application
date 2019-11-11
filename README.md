# Image Collector

It allows to collect and download the images from multiple image gallery. Right now, it supports only Flickr.
The application runs in different modes as `collector` and `downloader

## How To Build The Project

```
$ docker build -t image-collector:latest -f infra/docker/Dockerfile .
```

## How To Run The Project as Collector
```
docker run -it \
 -e SPRING_PROFILES_ACTIVE="collector" \
 -e SPRING_RABBITMQ_HOST=<rabbitmq host> \
 image-collector:latest
```

## How To Run The Project as Downloader
```
docker run -it \
 -e SPRING_PROFILES_ACTIVE="downloader" \
 -e SPRING_RABBITMQ_HOST=<rabbitmq host> \
 -e DOWNLOAD_PATH=/path/to/download \
 image-collector:latest
```

## Environment Variables:
**SPRING_PROFILES_ACTIVE**: (required)  It can take one of`collector` or `downloader` values. <br />
**SPRING_RABBITMQ_HOST** (default: localhost) Rabbitmq host machine <br />
**SPRING_RABBITMQ_USER** (default: guest) Rabbitmq user  <br />
**SPRING_RABBITMQ_PASSWORD** (default: guest) Rabbitmq password  <br />
**DOWNLOAD_PATH** (default: application root)  path to download the images <br />

## How To Create A Rabbitmq Instance for Test Purpose

```$xslt
docker run -d \
--name=rabbitmq \
-p5672:5672 \
-p15672:15672 \
--hostname=rabbitmq \
-e RABBITMQ_ERLANG_COOKIE="rabbitmq" \
rabbitmq:3.8.0-management
```

## How To Run The Application By Using The Test RabbitMQ Instance
```
docker run \
-it \
--network=host \
-e SPRING_RABBITMQ_HOST=localhost \
 ... (Other environment variables that depend on the profile.) \
image-collector:latest
```