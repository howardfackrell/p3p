#!/bin/bash

docker run \
  -it \
  --rm \
  -p 9000:9000 \
  -e PORT=9000 \
  -e P3P="WTF OMG" \
  p3p