FROM maven:3.6.2-jdk-11
ADD src /srv/src
ADD pom.xml /srv
ADD image-collector.iml /srv
WORKDIR /srv
VOLUME /var/data
RUN mvn clean package
CMD java -jar target/image-collector.jar --pro
