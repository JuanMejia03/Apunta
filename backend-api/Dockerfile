FROM gradle:jdk21 AS dev

WORKDIR /app

COPY build.gradle settings.gradle ./

RUN gradle dependencies --no-daemon

COPY src ./src

EXPOSE 8080

CMD ["sh", "-c", "gradle bootRun --daemon & LAST=$(find /app/src -type f -exec stat -c %Y {} + 2>/dev/null | sort -n | tail -1); while sleep 2; do CURR=$(find /app/src -type f -exec stat -c %Y {} + 2>/dev/null | sort -n | tail -1); if [ \"$CURR\" != \"$LAST\" ]; then LAST=$CURR; echo '--> Cambio detectado, recompilando...'; gradle classes --daemon; fi; done"]