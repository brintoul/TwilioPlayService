FROM java:8
ADD target/playing-1.0-SNAPSHOT.jar /data/playing-1.0-SNAPSHOT.jar
#ADD example.keystore /data/example.keystore
ADD twilio-numbers.yml /data/twilio-numbers.yml
#RUN java -jar playing-1.0-SNAPSHOT.jar db migrate /data/example.yml
CMD java -jar playing-1.0-SNAPSHOT.jar server /data/twilio-numbers.yml
EXPOSE 8080
