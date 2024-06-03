FROM golang:alpine AS binarybuilder

LABEL author.name="DingDangDog"
LABEL author.email="dingdangdogx@outlook.com"
LABEL project.name="image-oss"
LABEL project.version="2.0.0"

WORKDIR /app

# 后端
COPY ./server ./
# 构建适用于linux的可执行程序
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o images-server .

# 构建最终镜像
FROM alpine:latest

RUN apk add --no-cache nginx

WORKDIR /app

COPY --from=binarybuilder /app/images-server .
COPY --from=binarybuilder /app/config/ ./config/
COPY --from=binarybuilder /app/font/ ./font/
COPY ./web/ ./web/
COPY ./docker/nginx/nginx.conf /etc/nginx/nginx.conf
COPY ./docker/nginx/mime.types /etc/nginx/mime.types

# 前端
RUN nginx -t

ENV TZ "Asia/Shanghai"

# 容器数据卷
VOLUME /app/images/
VOLUME /app/config/config.json

# 运行应用
EXPOSE 80
CMD  ["sh", "-c", "nginx && ./images-server"]