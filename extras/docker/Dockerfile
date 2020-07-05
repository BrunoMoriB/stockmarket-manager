FROM debian:stretch

RUN apt update
RUN apt install wget -y

RUN apt install -y openjdk-8-jdk
 
RUN apt install telnet -y
RUN apt install procps -y

RUN wget https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
RUN mkdir -p /usr/share/maven
RUN tar -xzf apache-maven-3.6.3-bin.tar.gz -C /usr/share/maven --strip-components=1
RUN ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
RUN rm apache-maven-3.6.3-bin.tar.gz

ENV M2_HOME=/usr/share/maven
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

EXPOSE 8080
EXPOSE 5005

WORKDIR /stockmarket-manager
ENTRYPOINT /bin/bash -c "chmod +x /stockmarket-manager/extras/docker/entrypoint.sh && /stockmarket-manager/extras/docker/entrypoint.sh"
