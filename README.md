# image-oss
image-oss

## 简介

image-oss初衷是搭建一个 个人图床服务。

> 互联网各种图床看的眼花缭乱，用起来还需要配置各种东西，所以我想搭建一个自己的图床服务，这样用起来更加顺手。

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

- 上传图片：上传图片会将图片保存至一个指定的服务器文件夹下，查看[image.path](./src/main/resources/application-dev.yml)配置项。保存后会自动返回该图片的互联网URL（访问图片的url），生成URL时用到了一个配置项[image.url](./src/main/resources/application-dev.yml)。

- 导出图片：导出图片是将当前用户的个人文件夹压缩后，生成一个zip文件，然后导出。

- 用户概念：该系统没有复杂的用户限制，但为了私人服务被他人滥用，提供了一个密钥配置项：[keyList](./src/main/resources/application-dev.yml)。

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

## 使用方法

> 如果你想使用本项目自己搭建图床服务，你需要修改一些配置项，这样才能使这个项目为你所用，具体配置项作用可阅读[实现原理](#实现原理)了解。

> 前排提示：**请提前准备Java开发环境（jdk8）和docker环境。若无个人dockerhub帐号，有私人docker镜像仓库也可，若都没有，请自行部署nginx和springboot服务。**

### 修改后端配置并打包

前情提要：本项目使用jdk8，因为jdk11构建docker镜像太慢了，jdk17等高级版本还没了解过，有能力可自行升级。

1. 复制[resources](./src/main/resources)文件夹下的`application-dev.yml`文件，重命名为`application-pro.yml`。（不要删除`application-dev.yml`）
2. 修改`application-pro.yml`文件内的配置项为你的服务器配置
3. 使用java开发工具打开项目（如[IDEA](https://www.jetbrains.com/idea/)），更新一下maven依赖，然后打包你的源码，生成jar包，正常会打包会在你项目根目录生成`target`文件夹，其中会生成一个`image-oss-1.0.jar`。


### nginx配置修改

前请提要：因为本项目最终部署是使用docker，所以nginx会在构建docker镜像时自动安装，无需手动安装，所以只需修改好nginx的配置即可。

如果你修改了后端保存图片的文件夹，下方的`root`配置项请修改为对应的文件夹，若您不了解nginx如何配置，建议不要修改图片存储的文件夹，保持默认即可。

```
		location /images {
            root   /data/image-oss;
            autoindex on;
            autoindex_exact_size off;
            autoindex_localtime on;
        }
```

### 前端配置

请阅读[前端](#前端)

### docker

详见[Dockerfile](./Dockerfile)。

如果你不懂Dockerfile，建议不要修改。

如果你懂Dockerfile，可以随意修改😁。

修改后项目根目录运行命令：`docker build -t xxx/image-oss:1.0 .`接口构建镜像。其中`xxx`应该是你的dockerhub用户名，`image-oss`应该是你的docker镜像仓库名，也是你的镜像名，`1.0`是镜像的版本号。

当前有我已经构建好镜像，使用[docker-compose.yml](./docker-compose.yml)直接运行即可，但其中配置是我的服务配置，可以启动后再到docker容器中进行相关配置修改，然后重启docker容器，springboot服务配置在容器内`/usr/image-oss`文件夹中，nginx配置在`/var/lib/nginx`文件夹中。



### 部署方式

- 首先，部署方式推荐docker，可使用本项目提供的[docker-compose.yml](./docker-compose.yml)自动化部署。
- 其次，若不想使用docker，自行部署nginx、springboot微服务即可。

**记得修改相关配置哦**



## 使用效果

![images-oss](./images-oss.gif)

