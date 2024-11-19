from consol/rocky-xfce-vnc
MAINTAINER domotic70@gmail.com

USER root
WORKDIR .

RUN  /bin/sh -c set -eux; dnf install -y java-1.8.0-openjdk gedit webkit2gtk3

RUN  echo "set mouse-=a" >> ~/.vimrc

# CREATING PIMP DIR
RUN mkdir /opt/jd2

# Copy all
COPY jd2 /opt/jd2
# Rm logs
RUN rm -rf /opt/jd2/logs/*

COPY pimpMyJDownloader.json /opt/jd2
COPY launch.sh  /opt/jd2
COPY vnc_startup.sh /dockerstartup

expose 8080
expose 6901

