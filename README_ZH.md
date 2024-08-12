已过期，新项目：[Doimage](https://github.com/dingdangdog/Doimage)

# image-oss

[English](./README.md)  简体中文

## 关于

image-oss 的初衷是构建一个**个人图床服务**。它提供了一个简单的可视化操作页面，用于管理您的图片，包括上传、自动添加水印（支持使用[得意黑 Smiley Sans](https://github.com/atelier-anchor/smiley-sans)字体添加中文水印）、查看图片列表、删除图片和打包下载图片。

## 快速开始

1. Docker-Compose

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
         - ./config.json:/app/config/config.json
       ports:
         - 11080:80
   ```

2. 修改`config.json`配置文件：

   复制 [config.json](./docker/config.json) 到你的服务器上

   - **必须修改的配置项**：将`http://localhost:80`修改为您自己的图床服务地址。例如，如果您的图床站地址是`https://images.com`，则修改如下（未来将来会简化配置）：

   ```json
   "server_url": "https://images.com",
   "base_image_url": "https://images.com/images/",
   ```

   - **建议修改的配置**：根据以下注释自行修改：

   ```json
   // 个人认证码配置
   "user_map": {
     // "test" 将是用户名和文件夹名称，"testKey" 是用户 "test" 的认证码
     "testKey": "test",
     // "test2" 将是另一个用户名和文件夹名称，"testKey2" 是用户 "test2" 的认证码
     "testKey2": "test2"
   }
   ```

3. 在包含`docker-compose.yml`和`config.json`的文件夹中运行以下命令，以拉取最新的`image-oss`镜像并启动：

   ```sh
   docker-compose up -d
   ```

### 端口使用

1. 使用提供的`docker-compose.yml`部署，将占用主机（服务器）上的一个端口：`11080`。
2. `config.json`中的端口号`11033`一般不应修改，因为它与Nginx配置相对应。如果需要修改，请确保同时修改这两个配置。

### 注意事项

1. 个人认证码是必需的。快速开始部分的**建议修改的配置**用于维护这些认证码。
2. 如果用户名不变但认证码更换，仍然会被视为同一用户（图片保存到同一目录）。
3. **在前端页面中，必须正确输入个人认证码，否则几乎所有功能都无法使用**。
4. 在前端输入个人认证码并成功上传一次图片后，认证码会保存在浏览器缓存中。如果不清除缓存，下次打开页面时将自动填充认证码，水印信息同理。
5. 目前水印仅支持`jpg`、`jpeg`和`png`文件类型。对于其他类型，请在上传前清空“请输入水印内容”输入框，否则上传会失败。
6. 如果是互联网图床，建议服务器带宽在10M以上，否则上传大文件时容易引起超时异常。
7. 服务默认支持最大30M的文件上传。如果需要，可以按需修改，最大可改为50M。

### 常见错误

- `No Permission!`：无权限，请检查个人认证码是否正确。
- `Uploaded file type is not supported!`：不支持上传的文件类型。
- `The file type with watermark is not supported!`：不支持加水印的文件类型。
- `Unknown Exception!`：在文件处理时出现了预期外的异常，出现此错误时，建议提交`ISSUE`。
- `System Error!`：系统错误，常见原因可能是：服务端未知异常、网络连接异常。

**如果您有任何其他问题，可以提交ISSUE！**

## 技术要点

- Golang - Gin
- html + css + javascript
- nginx
- docker

## 实现原理

### 后端

后端使用Golang开发一个微服务，提供API以支持主要功能，如图片上传和导出。

### 前端

前端是一个非常简单的`html`文件。初期服务器只提供后端API支持，这样使用起来比较麻烦。因此，前端现在也被集成到了Docker镜像中。

现在，使用最新的Docker镜像部署，直接访问服务即可获得前端页面。

### Nginx

Nginx在项目中起到非常重要的作用，主要用于：

1. 访问前端页面；
2. 查看图片资源（图片、图库）；
3. 后端服务转发。

但请放心，即使您不懂Nginx也可以轻松使用本服务。Nginx相关配置已集成到Docker容器中，无需做任何修改即可直接使用。

## 未来计划

- [ ] 使用Vuetify重构Web UI
- [ ] 简化配置
- [ ] 谷歌浏览器插件
- [ ] 多语言支持
- [ ] 更多...

> 永远等待···

## 页面

- 主页面

  ![home](./images/home.jpg)

- 图片库

  ![store](./images/store.jpg)

### 注意

**我会尽力做好它。**
