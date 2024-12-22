#!/usr/bin/env bash

docker-compose up -d

open "http://localhost:8082/?gw=ws://localhost:8182%3Furl%3Dhttp%3A%2F%2Fhost.docker.internal%3A8080"
