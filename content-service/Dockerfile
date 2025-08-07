# 1. Collecting Hugo
FROM golang:1.23-alpine AS builder

ENV HUGO_VERSION=0.148.2

RUN apk add --no-cache git curl gcc g++ musl-dev libwebp-dev libsass-dev tree \
  && cd /tmp \
  && curl -L https://github.com/gohugoio/hugo/archive/refs/tags/v${HUGO_VERSION}.tar.gz | tar -xz \
  && tree /tmp/hugo-${HUGO_VERSION} \
  && cd hugo-${HUGO_VERSION} \
  && CGO_ENABLED=1 go install --tags extended

FROM alpine:3.18

RUN apk add --no-cache libstdc++ libgcc git tree

COPY --from=builder /go/bin/hugo /usr/local/bin/hugo

RUN tree /usr/local/bin/

WORKDIR /src

ENTRYPOINT ["hugo"]
