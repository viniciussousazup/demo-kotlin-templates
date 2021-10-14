#!/bin/bash

gradle build -x test 
k3d cluster delete || echo "ok"
k3d cluster create
docker build -t {{project_name|lower}}-application:k3d .
k3d image import "{{project_name|lower}}-application:k3d"
kubectl apply -f infra/local/application.yaml