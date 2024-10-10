#!/bin/bash

kubectl apply -f api.yaml
kubectl apply -f frontend.yaml
kubectl apply -f ingress.yaml
