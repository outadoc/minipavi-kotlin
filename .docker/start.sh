#!/usr/bin/env bash

docker-compose up -d

open "http://localhost:8082/?gw=ws://localhost:8182"
