FROM openjdk:11-jdk-slim
WORKDIR /traffic-cop
RUN mkdir -p /tmp/traffic-cop
COPY . .
RUN ./gradlew build -x test
RUN mv application/build/libs/traffic-cop.jar /tmp/traffic-cop
WORKDIR /tmp/traffic-cop
RUN jar xf traffic-cop.jar
RUN rm traffic-cop.jar

FROM openjdk:11-jre-slim
RUN mkdir -p /traffic-cop/reports
WORKDIR /traffic-cop
COPY --from=0 /tmp/traffic-cop /traffic-cop
CMD java -Dreports.dir=/traffic-cop/reports -cp . org.springframework.boot.loader.JarLauncher
