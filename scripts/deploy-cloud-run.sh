#!/bin/bash

# Cloud Run deployment script for events-service
# Usage: ./deploy-cloud-run.sh <version> [compile] [deploy]

version=$1
compile=$2
deploy=$3

# Configuration
PROJECT_ID="phonic-altar-450817-q4"
SERVICE_NAME="events-service"
REGION="europe-southwest1" # Madrid
IMAGE_NAME="gcr.io/${PROJECT_ID}/${SERVICE_NAME}"
SECRET_NAME="firebase-credentials"
SERVICE_ACCOUNT="343004725643-compute@developer.gserviceaccount.com"

gcloud config set project ${PROJECT_ID}

STARTTIME=`date +%s`
echo "***   `date`: Starting Cloud Run deployment script."

if [ "$compile" = "compile" ]; then
    echo "***   Compiling and packaging version $version"
    mvn clean package
    if [[ "$?" -ne 0 ]] ; then
        echo 'Could not perform packaging'; exit 1
    fi
fi

echo "***   Building Docker image version $version"
docker build \
    -f src/main/docker/Dockerfile.jvm \
    --platform=linux/amd64 \
    -t ${IMAGE_NAME}:$version .

echo "***   Pushing image to Google Container Registry"
docker push ${IMAGE_NAME}:$version

if [ "$deploy" = "deploy" ]; then
    echo "***   Deploying to Cloud Run using image ${IMAGE_NAME}:$version"
    
    gcloud run deploy ${SERVICE_NAME} \
        --image=${IMAGE_NAME}:$version \
        --platform=managed \
        --region=${REGION} \
        --allow-unauthenticated \
        --port=8080 \
        --memory=1Gi \
        --cpu=1 \
        --set-env-vars="GOOGLE_APPLICATION_CREDENTIALS=/app/firebase-key.json" \
        --update-secrets="/app/firebase-key.json=${SECRET_NAME}:latest" \
        --project=${PROJECT_ID}
    
    # Check if IAM policy binding is needed
    # CURRENT_POLICY=$(gcloud run services get-iam-policy ${SERVICE_NAME} --region=${REGION} --format="value(bindings.members)" 2>/dev/null)
    # if [[ ! "$CURRENT_POLICY" =~ "serviceAccount:${SERVICE_ACCOUNT}" ]]; then
    #     echo "***   Setting IAM policy for service account access"
    #     gcloud run services add-iam-policy-binding ${SERVICE_NAME} \
    #         --region=${REGION} \
    #         --member=serviceAccount:${SERVICE_ACCOUNT} \
    #         --role=roles/run.invoker \
    #         --project=${PROJECT_ID}
    # else
    #     echo "***   IAM policy already configured for service account"
    # fi
    
    echo "***   Deployment completed. Service URL:"
    gcloud run services describe ${SERVICE_NAME} --region=${REGION} --format="value(status.url)"
fi

ENDTIME=`date +%s`
let DURATION=${ENDTIME}-${STARTTIME}
echo "***   `date`: Deployment finished in ${DURATION} seconds"