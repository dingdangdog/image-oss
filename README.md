# image-oss

English  [简体中文](./README_ZH.md)

## About

The original intention of image-oss is to build a **Personal image storage service**. It provides a simple visual operation page for managing your images, including uploading, automatic watermarking (with support for Chinese watermarks using the [Smiley Sans](https://github.com/atelier-anchor/smiley-sans) font), viewing image lists, deleting images, and downloading images in a package.

## Quick Start

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

2. Modify the `config.json` configuration file:  
    copy the [config.json](./docker/config.json) to you server floder

     - **Mandatory Changes**: Change `http://localhost:80` to your own image hosting service address. For example, if your image hosting site address is `https://images.com`, modify as follows(Will be Simplify configuration in the future):

     ```json
     "server_url": "https://images.com",
     "base_image_url": "https://images.com/images/",
     ```

    - **Recommended Changes**: Refer to the comments below and modify accordingly:

     ```json
     // Personal authentication code configuration
     "user_map": {
       // "test" will be a user and folder name, "testKey" is the authentication code for user "test"
       "testKey": "test",
       // "test2" will be an anthor user and folder name, "testKey2" is the authentication code for user "test2"
       "testKey2": "test2"
     }
     ```

3. In the folder containing `docker-compose.yml` and `config.json`, run the following command to pull the latest `image-oss` image and start it:

   ```sh
   docker-compose up -d
   ```

### Port Usage

1. Deploying with the provided `docker-compose.yml` will occupy one port on the host (server): `11080`.
2. The port number `11033` in `config.json` generally should not be modified as it corresponds to the Nginx configuration. If you need to modify it, ensure you modify both configurations together.

### Notes

1. Personal authentication codes are required. The **recommended changes** in the quick start section are for maintaining these codes.
2. If the username remains unchanged but the authentication code changes, it will still be considered the same user (images are saved in the same directory).
3. **In the frontend page, the personal authentication code must be entered correctly, otherwise almost all functions will be unavailable**.
4. After entering the personal authentication code in the frontend and successfully uploading an image once, the code will be saved in the browser cache. If the cache is not cleared, the code will be auto-filled the next time the page is opened, same for the watermark information.
5. Currently, watermarks are only supported for `jpg`, `jpeg`, and `png` file types. For other types, clear the "Enter watermark content" input box before uploading, otherwise, the upload will fail.
6. For an internet image bed, it is recommended to have a server bandwidth of 10M or above to avoid timeout errors when uploading large files.
7. The service supports uploading files up to 30M by default. If needed, this can be modified to a maximum of 50M.

### Common Errors

- `No Permission!`: No permission, please check if the personal authentication code is correct.
- `Uploaded file type is not supported!`: Unsupported file type.
- `The file type with watermark is not supported!`: File type not supported for watermarking.
- `Unknown Exception!`: An unexpected exception occurred during file processing. If this error occurs, it is recommended to submit an `ISSUE`.
- `System Error!`: System error, common causes may include unknown server exceptions or network connection issues.

**If you have any other questions, feel free to submit an ISSUE!**

## Technical Points

- Golang - Gin
- html + css + javascript
- nginx
- docker

## Implementation Principles

### Backend

The backend uses Golang to develop a microservice that provides APIs to support main functions such as image uploading and exporting.

### Frontend

The frontend is a very simple `html` file. Initially, the server only provided backend API support, which was cumbersome for multi-terminal use. Therefore, the frontend has now been integrated into the Docker image.

Now, using the latest Docker image deployment, you can directly access the service to get the frontend page.

### Nginx

Nginx plays an important role in the project, mainly for:

1. Accessing the frontend page;
2. Viewing image resources (images, galleries);
3. Backend service forwarding.

However, rest assured, even if you do not understand Nginx, you can easily use this service. Nginx-related configurations have been integrated into the Docker container and can be used directly without any modifications.

## Future Plans

- [ ] Refactor web UI using Vuetify
- [ ] Simplify configuration
- [ ] Google Chrome plug-in
- [ ] Multi-language support
- [ ] More...

> waiting forever···

## Pages

- Home Page

![home](./images/home.jpg)

- Image Gallery

![store](./images/store.jpg)

### Notice

**I will try my best to do it well.**
