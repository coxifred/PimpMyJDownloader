# Welcome to the PimpMyJDownloader!


![PimpMyJDownloader](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/fond.png?raw=true&s=100)

## **Quick description**
![Overview](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/monkey.png?raw=true)

Stuck of copy/paste through network into JDownloader ? start download ? PimpMyJDownloader is here to simply send a link (from chrome) to a JDownloader instance running outside your pc. In only one click your download will start automatically and warns you when the download is completed. You can watch progression from chrome directly.

Pros:

  No MyJdownloader account.

  Can turnoff immediately my computer (Download running on remote server)

_All starts with an overview:_

![Overview](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/arch.png?raw=true)

## **Features**


### 1. Jetty instance embedded in PimpMyJDownloader Extension. Tunable port and ip for binding.

![feature1](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/extension.jpg?raw=true)

### 2. Chrome Extension for following multiple downloads progressions.

![feature2](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/extension2.jpg?raw=true)

![feature3](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/extension3.jpg?raw=true)

### 3. Mqtt publishing of download files with progression, speed, fileName, url.

![feature5](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/extension5.jpg?raw=true)

### 4. Context action included in Chrome Extension.

![feature4](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/extension4.jpg?raw=true)

### 5. Webserver (if you don't want use ChromeExtension)

![feature6](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/extension6.jpg?raw=true)

### 6. Simple Api, to push links, retrieve states/logs.

http://your_ip:your_port/admin?link=your_url  For pushing a new link to download

http://your_ip:your_port/admin?state=json  For retrieving Json status of all downloads.

http://your_ip:your_port/admin?logs=json  For retrieving Json logs.

## **Running with docker**

Simpliest way to launch this plugin is with docker.

> Your JDownloader will be launched inside a docker rocky linux container.
> 
> Your desktop will be reachable through port 6901 (NoVnc) with HTML5 browser.
> 
> Your PimpMyJDownloader server will be reachable through port 8080 with HTML5 browser.
> 

### 0. Use compose:

```yaml
version: "2.4"
services:
  "pimpmyjdownloader":
    container_name: "pimpmyjdownloader"
    privileged: true
    image: "docker.io/coxifred/pimpmyjdownloader:1.0"
    #volumes:
      # Override storage directory
      #- /mediatheque/JDownloader:/mediatheque/JDownloader
      # Override PimpMyJDownloader settings
      #- /root/pimpmyjdownloader.json:/opt/jd2/pimpMyJDownloader.json
     
      
    restart: always
    ports:
      # NoVnc Html5
      - "6901:6901"
      # PimpMyJDownloader Jetty webserver
      - "8080:8080"
```
### 1. docker-compose up -d (JDownloader should start within 1mn)

## **Installation in JDownloader** for real men :)

### 0. Requisites, considering JDownloader2 is installed (I use build June 2019).This extension has been built with java8.
And sorry, headless is not supported, need a MyJDownloader account :(.

### 1. Copy jar ([PimpMyJDownloader.jar](https://github.com/coxifred/PimpMyJDownloader/raw/master/release/PimpMyJDownloader-1.0.jar)) or compile project yourself and put it into JDownloader extension directory.

### 2. In JDownloader/tmp/extensioncache directory, open extensionInfos.json and add this at the end (be careful Json array)

Just change the **jarPath** to yours.
 
```json
{
  "settings" : true,
  "configInterface" : "org.jdownloader.extensions.pimpmyjdownloader.PimpMyJDownloaderConfig",
  "quickToggle" : true,
  "headlessRunnable" : true,
  "description" : "Simply open a webserver to receive paste link from ChromeExtension",
  "lng" : "fr_FR",
  "iconPath" : "monkey",
  "linuxRunnable" : true,
  "macRunnable" : true,
  "name" : "PimpMyJDownloader",
  "version" : -1,
  "windowsRunnable" : true,
  "classname" : "org.jdownloader.extensions.pimpmyjdownloader.PimpMyJDownloaderExtension",
  "jarPath" : "<path_to_JDownloader_Extensions>/PimpMyJDownloader-1.0.jar"
}
```
### 3. In JDownloader/update/versioninfo/JD directory, create/open extensions.installed.json and add/complete this

```json
["pimpmyjdownloader"]
```

### 4. Copy (from this repository) [monkey.png](https://github.com/coxifred/PimpMyJDownloader/blob/master/images_resources4wiki/monkey.png?raw=true) into JDownloader/themes/standard/org/jdownloader/images

### 5. Restart JDownloader and go to parameters, you should see the monkey.

## **Installation in Chrome**

### 1. Authorized developer mode in extensions tabs (chrome://extensions) upper-right corner.

### 2. Click on Load extension not packaged (provide in this repo). You just have to point on directory PimpMyJDownloaderChrome.

### 3. You should see the monkey, click on it and configure ip/port and save.

### 4. Try it by selecting a download link, right click and Pimp It !

