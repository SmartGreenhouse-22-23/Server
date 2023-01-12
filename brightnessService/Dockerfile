FROM gradle:7-jdk11-alpine AS brightness_service
COPY --chown=gradle:gradle .. /home/gradle/src
WORKDIR /home/gradle/src
CMD gradle brightnessService:Start
EXPOSE 8893