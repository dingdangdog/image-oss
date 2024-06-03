# image-oss

## About

The original intention of image-oss is to build a **Personal image storage service**.

And now, it provides a simple visual operation page.

> The various image beds on the Internet are dazzling to see, and you need to configure various things to use them, so I want to build my own image bed service to make it easier to use.

## Necessary

If want to deploy `dingdangdog/image-oss`, You need An `config.json` config file. You can find it content from [config.json](./server/config/config.json).

You need create `config.json` config file, the storage path must correspond to the configuration in `docker-compose.yml`.

If you use this example `docker-compose.yml`, you should should make sure `docker-compose.yml` and `config.json` stay together in a folder.

## docker-compose.yml Demo

```yaml
services:
  image-oss:
    container_name: image-oss
    image: dingdangdog/image-oss:latest
    restart: always
    environment:
      TZ: "Asia/Shanghai"
    volumes:
      - ./images:/app/images
      - ./config.json:/app/config.json
    ports:
      - 11080:80
```

## Plan

- [ ] Use Vuetify refactor webui
- [ ] Simplified configuration
- [ ] Google Chrome plug-in
- [ ] Multi-language support
- [ ] More...

> waiting forever···

## Get Started

[More Information (Chinese)](./MoreInfo.md)

### Gallery function

- 主界面
![home](./images/home.jpg)
- 图库
![store](./images/store.jpg)

## Notice

**I will try my best to do it well.**
