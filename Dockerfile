FROM maven:3-eclipse-temurin-17-alpine
WORKDIR /modulartelebot
COPY . .

# clean up
RUN mvn clean compile
RUN rm -rf temp

# add python :[
ENV PYTHONUNBUFFERED=1
RUN apk add --no-cache python3 py3-pip

CMD ["mvn", "exec:java"]
