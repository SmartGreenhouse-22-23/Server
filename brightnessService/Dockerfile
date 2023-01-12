FROM gradle:7-gdk11-alpine AS brightness_service
COPY --chown=gradle:gradle ../../ /home/gradle/src
CMD gradle brightnessService:Start
EXPOSE 8893
EXPOSE 27017