# 运行容器
FROM openjdk:8-alpine as RUNNER

LABEL author.name="DingDangDog"
LABEL author.email="dddogx@qq.com"
LABEL project.name="image-oss"
LABEL project.version="1.0"

# 前端
RUN mkdir /run/nginx
RUN apk --no-cache add nginx

COPY ./nginx/nginx.conf /var/lib/nginx/nginx.conf
COPY ./nginx/mime.types /var/lib/nginx/mime.types

# 后端
WORKDIR /usr/image-oss

COPY ./target/image-oss-1.0.jar ./image-oss-1.0.jar
COPY ./src/main/resources/application.yml ./application.yml
COPY ./src/main/resources/application-dev.yml ./application-dev.yml
COPY ./src/main/resources/application-pro.yml ./application-pro.yml

VOLUME /data/image-oss/images/

ENV TZ "Asia/Shanghai"

COPY ./entrypoint.sh ../

EXPOSE 80 11033
ENTRYPOINT [ "sh","../entrypoint.sh" ]
