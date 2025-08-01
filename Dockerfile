FROM ubuntu:22.04 as builder

ENV DEBIAN_FRONTEND=noninteractive

RUN apt update && apt install -y \
    git cmake g++ make unzip wget zip \
    default-jdk curl pkg-config libssl-dev \
    zlib1g-dev openjdk-17-jdk ca-certificates maven

# Clone the TDLib
WORKDIR /opt
RUN git clone https://github.com/tdlib/td.git

# Clone the vcpkg
RUN git clone https://github.com/Microsoft/vcpkg.git
WORKDIR /opt/vcpkg
RUN ./bootstrap-vcpkg.sh
RUN ./vcpkg install gperf openssl zlib

# Collect TDLib with JNI
WORKDIR /opt/td
RUN mkdir build
WORKDIR /opt/td/build
RUN cmake .. \
  -DCMAKE_BUILD_TYPE=Release \
  -DCMAKE_INSTALL_PREFIX=/opt/tdlib \
  -DTD_ENABLE_JNI=ON \
  -DCMAKE_TOOLCHAIN_FILE=/opt/vcpkg/scripts/buildsystems/vcpkg.cmake
RUN cmake --build . --target install --config Release -- -j$(nproc)

# add search files for check the way
RUN find /opt/td/build -name "libtdjni.so"
RUN find /opt/td/build -name "tdlib.jar"

FROM eclipse-temurin:17-jdk as runner

COPY --from=builder /opt/tdlib /opt/tdlib

RUN echo "=== tdlib.jar ===" && find /opt/tdlib -name "tdlib.jar"
RUN echo "=== libtdjni.so ===" && find /opt/tdlib -name "libtdjni.so"


# copy all app
WORKDIR /app
COPY . /app
RUN mvn clean install


# Add tdlib.jar to local Maven-repo
RUN apt update && apt install -y maven
RUN mvn install:install-file \
    -Dfile=/opt/tdlib/tdlib.jar \
    -DgroupId=com.td \
    -DartifactId=tdlib \
    -Dversion=1.0 \
    -Dpackaging=jar



# Збираємо застосунок
RUN mvn clean install

# Запускаємо
CMD ["java", "-Djava.library.path=.", "-jar", "target/*.jar"]
