FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY backend/src/ backend/src/
COPY backend/data/hindi_10k.txt backend/data/hindi_10k.txt
COPY frontend/ frontend/

RUN javac --add-modules jdk.httpserver -encoding UTF-8 backend/src/*.java

EXPOSE 10000

CMD ["java", "--add-modules", "jdk.httpserver", "-cp", "backend/src", "Main"]
