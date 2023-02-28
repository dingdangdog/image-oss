# 运行容器
FROM openjdk:8-alpine as RUNNER

LABEL author.name="DingDangDog"
LABEL author.email="dddogx@qq.com"
LABEL project.name="image-oss"
LABEL project.version="1.1"

# 安装 fontconfig 和 ttf-dejavu字体
RUN apk add fontconfig
RUN apk add --update ttf-dejavu
RUN fc-cache --force

# 前端
RUN mkdir /run/nginx
RUN apk --no-cache add nginx

COPY ./nginx/nginx.conf /var/lib/nginx/nginx.conf
COPY ./nginx/mime.types /var/lib/nginx/mime.types

#RUN mkdir /var/lib/nginx/html
COPY ./index.html /var/lib/nginx/html/index.html

# 后端
WORKDIR /usr/image-oss/jar

COPY ./target/image-oss-1.1.jar ./image-oss-1.1.jar
COPY ./src/main/resources/application.yml ./application.yml

# 容器数据卷
VOLUME /data/image-oss/images/
VOLUME /var/lib/nginx/nginx.conf
VOLUME /var/lib/nginx/html/index.html
VOLUME /usr/image-oss/jar/application.yml

ENV TZ "Asia/Shanghai"

COPY ./entrypoint.sh ../

EXPOSE 80
ENTRYPOINT [ "sh","../entrypoint.sh" ]
