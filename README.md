# image-oss
image-oss

## 简介

image-oss初衷是搭建一个 个人图床服务。

> 互联网各种图床看的眼花缭乱，用起来还需要配置各种东西，所以我想搭建一个自己的图床服务，这样用起来更加顺手。


## 快速开始

> 如果你想使用本项目自己搭建图床服务，你需要修改一些配置项，这样才能使这个项目为你所用，具体配置项作用可阅读[实现原理](#实现原理)了解。

1. 准备配置文件：在你的服务器创建一个本项目的专属文件夹，然后将以下文件拷贝到该文件夹：
   - `docker-compose.yml`：在项目根目录中
   - `nginx.conf`：在[nginx](./nginx)文件夹下
   - `application.yml`：在[src/main/resources](./src/main/resources)文件夹下

2. 修改配置文件：
   - `application.yml`文件中的`image.url`是必须修改配置项，其他均可以不修改。
 
3. 在配置文件的所在文件夹运行指令：

```
docker-compose up -d
```

4. 服务正常启动后，客户端文件为`uploadImages.html`，需要修改其源码中的`key`、`baseUrl`。修改后使用任意浏览器打开即可。（如果不会修改`html`文件源码。。。建议百度一下）

### 端口占用

通过本项目提供的`docker-compose.yml`部署，若不做任何修改，将占用宿主机（服务器）两个端口：11080-前端访问端口；11033-后端服务端口。

## 基本功能

1. 上传图片，获取图片的url地址
2. 导出全部图片
3. 查看图片文件清单

## 技术点

- springboot
- nginx
- docker

## 实现原理


### 后端

后端使用springboot开发一个微服务，该微服务只有两个接口：上传图片、导出图片。

- 上传图片：上传图片会将图片保存至一个指定的服务器文件夹下，查看[image.path](./src/main/resources/application.yml)配置项。保存后会自动返回该图片的互联网URL（访问图片的url），生成URL时用到了一个配置项[image.url](./src/main/resources/application.yml)。

- 导出图片：导出图片是将当前用户的个人文件夹压缩后，生成一个zip文件，然后导出。

- 用户概念：该系统没有复杂的用户限制，但为了私人服务被他人滥用，提供了一个密钥配置项：[keyList](./src/main/resources/application.yml)。

### 前端

因为该项目的初衷是个人使用，所以前端追求简单易用。前端只有一个文件：[uploadImages.html](./uploadImages.html)。

使用该文件的方法是：**下载到本地，使用任意浏览器打开即可。**

若你个人部署使用该服务，需要编辑该文件，将其中两个配置项修改为你个人服务的配置：

```javascript
  // 个人密钥
  const key = "dingdangdog";
  // 服务地址
  const baseUrl = "http://localhost:11033/";
```

- 个人密钥`key`：该配置应该与后端配置对应

- 服务地址`baseUrl`：该配置应该是你后端服务的访问地址

## 使用效果

![images-oss](./images-oss.gif)

