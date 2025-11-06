#!/bin/bash

# Setup Google Cloud secrets for events-service
# Usage: ./setup-secrets.sh <path-to-firebase-key.json>

FIREBASE_KEY_PATH=$1
PROJECT_ID="phonic-altar-450817-q4"
SECRET_NAME="firebase-credentials"

if [ -z "$FIREBASE_KEY_PATH" ]; then
    echo "Usage: ./setup-secrets.sh <path-to-firebase-key.json>"
    exit 1
fi

if [ ! -f "$FIREBASE_KEY_PATH" ]; then
    echo "Error: Firebase key file not found at $FIREBASE_KEY_PATH"
    exit 1
fi

echo "Creating secret $SECRET_NAME in project $PROJECT_ID"
gcloud secrets create $SECRET_NAME \
    --data-file="$FIREBASE_KEY_PATH" \
    --project=$PROJECT_ID

echo "Secret created successfully. You can now deploy the service."