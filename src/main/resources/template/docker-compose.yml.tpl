version: '3'
services:
  [=artifactId]-service:
    build: .
    ports:
    - "8888:8888"
    environment:
    - GLUE_CONFIG=./glue-test.config
    - LOG4J_DEFAULT_LEVEL=DEBUG
