FROM maven:3-eclipse-temurin-21-alpine
WORKDIR /modulartelebot
COPY . .
RUN mvn clean compile

# add python :[
ENV PYTHONUNBUFFERED=1
RUN apk add --no-cache python3 py3-pip

CMD ["mvn", "exec:java"]
