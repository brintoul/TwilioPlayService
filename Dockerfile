FROM java:8
ADD target/playing-1.0-SNAPSHOT.jar /data/playing-1.0-SNAPSHOT.jar
ADD twilio-numbers.yml /data/twilio-numbers.yml
CMD java -jar /data/playing-1.0-SNAPSHOT.jar server /data/twilio-numbers.yml
EXPOSE 5656
