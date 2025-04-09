# [Testing] Supporters service

## Prepare the database PostGIS

For local testing, use a docker image to get the **postgresql** database with the **gis** module included.

Here some reference about postGIS: [Official page](https://postgis.net/)

`docker run --name postgis -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=my-s3cr3t -e POSTGRES_DB=gisdb -p 5432:5432 -d postgis/postgis`

`docker exec -it postgis psql -U admin -d gisdb`

```sql
CREATE TABLE stadiums (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    document_id VARCHAR(128) NULL,
    location GEOMETRY(POLYGON, 4326)
);

INSERT INTO stadiums (name, document_id, location) VALUES
('Santiago Bernabéu', 'kIanogZmCsx6jcBKJlpO', ST_GeomFromText('POLYGON((-3.6904 40.4531, -3.6880 40.4531, -3.6880 40.4510, -3.6904 40.4510, -3.6904 40.4531))', 4326)),
('Spotify Camp Nou', 'Hf72v7HpzxFj2z0f4gCZ', ST_GeomFromText('POLYGON((2.1202 41.3811, 2.1234 41.3811, 2.1234 41.3789, 2.1202 41.3789, 2.1202 41.3811))', 4326)),
('Wanda Metropolitano', null, ST_GeomFromText('POLYGON((-3.5976 40.4372, -3.5948 40.4372, -3.5948 40.4349, -3.5976 40.4349, -3.5976 40.4372))', 4326)),
('Mestalla', null, ST_GeomFromText('POLYGON((-0.3584 39.4746, -0.3561 39.4746, -0.3561 39.4724, -0.3584 39.4724, -0.3584 39.4746))', 4326)),
('Ramón Sánchez-Pizjuán', null, ST_GeomFromText('POLYGON((-5.9854 37.3841, -5.9831 37.3841, -5.9831 37.3820, -5.9854 37.3820, -5.9854 37.3841))', 4326)),
('San Mamés', null, ST_GeomFromText('POLYGON((-2.9460 43.2646, -2.9438 43.2646, -2.9438 43.2624, -2.9460 43.2624, -2.9460 43.2646))', 4326)),
('Anoeta', null, ST_GeomFromText('POLYGON((-1.9736 43.3013, -1.9714 43.3013, -1.9714 43.2992, -1.9736 43.2992, -1.9736 43.3013))', 4326)),
('Benito Villamarín', null, ST_GeomFromText('POLYGON((-5.9913 37.3568, -5.9891 37.3568, -5.9891 37.3546, -5.9913 37.3546, -5.9913 37.3568))', 4326)),
('RCDE Stadium', null, ST_GeomFromText('POLYGON((2.0759 41.3470, 2.0781 41.3470, 2.0781 41.3448, 2.0759 41.3448, 2.0759 41.3470))', 4326)),
('Estadio de la Cerámica', null, ST_GeomFromText('POLYGON((-0.6415 39.9433, -0.6393 39.9433, -0.6393 39.9412, -0.6415 39.9412, -0.6415 39.9433))', 4326));
```

Link the location of the stadium in PostGIS database with the associated document in firebase.

Prepare the query to answer: Is a Device Inside Any Stadium?

```sql
SELECT name, document_id FROM stadiums
WHERE ST_Contains(
    location,
    ST_SetSRID(ST_Point(-3.6890, 40.4520), 4326)
);
```

## Prepare deployment and testing

> Build the image using a linux adm64 platform.

### Local

Recommended for local and testing porpouses:

`docker build --platform=linux/amd64 -t cafaray/champions-league:v1 .`

Push the image to the registry hub:

`docker push cafaray/champions-league:v1`

Run the container using the docker command: 

`docker run -p 8080:8080 --name champion-league \
  -e GOOGLE_APPLICATION_CREDENTIALS="/app/firebase-key.json" \
  -v /Users/wendigo/workspace/champion-league/src/main/resources/phonic-altar-450817-q4-firebase-adminsdk-fbsvc-749719c99c.json:/app/firebase-key.json \
  cafaray/champions-league:v1`

### Cloud Function

`docker build --platform=linux/amd64 -t gcr.io/lfs261-cicd-304112/champions-league:v1 .`

Use the next command, and be sure you are logged to GCP and to have the right service account and permissions:

`docker push gcr.io/lfs261-cicd-304112/champions-league:v1`

Before deploying, you must create a secret for managing the connection with Firebase

- Create the secret for the service and add the secret into the Secret Manager

```bash
gcloud secrets create firebase-testmobile-key \
  --data-file=/Users/wendigo/workspace/champion-league/src/main/resources/phonic-altar-450817-q4-firebase-adminsdk-fbsvc-749719c99c.json
```

- Bind the secret to be use it for the cloud run service instance

> considering you previously create a service account to manage the cloud function, in my case is: `cloud-run-functions@lfs261-cicd-304112.iam.gserviceaccount.com`

```bash
gcloud secrets add-iam-policy-binding firebase-testmobile-key \
  --member=serviceAccount:cloud-run-functions@lfs261-cicd-304112.iam.gserviceaccount.com \
  --role=roles/secretmanager.secretAccessor 
```

The output shoiuld look like this:

```bash
Updated IAM policy for secret [firebase-testmobile-key].
bindings:
- members:
  - serviceAccount:cloud-run-functions@lfs261-cicd-304112.iam.gserviceaccount.com
  role: roles/secretmanager.secretAccessor
etag: BwYwZnCsu1I=
version: 1
```

- Deploy the function to Cloud Run

```bash
gcloud run deploy champions-league \
  --image=gcr.io/lfs261-cicd-304112/champions-league:v2 \
  --region=europe-southwest1 \
  --allow-unauthenticated \
  --timeout=60s \
  --service-account=cloud-run-functions@lfs261-cicd-304112.iam.gserviceaccount.com \
  --set-secrets=/app/firebase-testmobile-key.json=firebase-testmobile-key:latest
```

If any error, be sure that it runs locally:

> be sure you are using the same image in the gcr.io

```bash
docker run -p 8080:8080 \
  -e GOOGLE_APPLICATION_CREDENTIALS="/app/firebase-key.json" \
  -v /Users/wendigo/workspace/champion-league/src/main/resources/phonic-altar-450817-q4-firebase-adminsdk-fbsvc-749719c99c.json:/app/firebase-key.json \
  gcr.io/lfs261-cicd-304112/champions-league:v1
```

#### For private gcr.io

In case you are using Kubernetes to deploy the service, and the gcr.io registry is private:

- Create a new service account (or use an existing one).

- Grant it the "Storage Object Viewer" role (for access to GCR images).

- Create and download a JSON key for the service account

- Create a secret to connect to gcr.io

```bash
cat keyfile.json | docker login -u _json_key --password-stdin https://gcr.io

kubectl create secret -n ${namespace} docker-registry gcr-json-key \
  --docker-server=gcr.io \
  --docker-username=_json_key \
  --docker-password="$(cat ~/localdir/lfs261-cicd--gcrio-304112-54b378730613.json)" \
  --docker-email=gcrio-sa-261@lfs261-cicd-304112.iam.gserviceaccount.com
```

finally, patch the service account:

```bash
kubectl patch serviceaccount default \
  -p '{"imagePullSecrets":[{"name":"gcr-json-key"}]}' \
  -n ${namespace}
```

#### Connect to Firebase from external

Create a secret to connect to firebase

```bash
kubectl create secret generic firebase-config \
  --from-file=~/localdir/firebase-config.json \
  --namespace ${namespace}
```

## Test the service

Use a curl image to test any endpoint of the service for firebase database:

```bash
kubectl run curl --rm -i --tty --image=curlimages/curl -- \
    curl -v http://${uri-service}:8080/v1/leagues
```

Expected output:

```bash
If you don't see a command prompt, try pressing enter.
< HTTP/1.1 200 OK
< Content-Type: application/json
< content-length: 640
< 
* Connection #0 to host champion-league left intact
[{"teams":["Athletic Club","Atlético Madrid","Barcelona","CA Osasuna","CD Leganés","Celta Vigo","Deportivo Alavés","Getafe CF","Girona FC","Rayo Vallecano\tRayo Vallecano","RCD Espanyol","RCD Mallorca","Real Betis","Real Madrid\tReal Madrid","Real Sociedad","Real Valladolid","Sevilla","UD Las Palmas","Valencia CF","Villarreal CF"],"origin":"1928","name":"La Liga","short_name":"La Liga","region":"Spain"},{"teams":["Blackburn Rovers","Bristol City","Burnley","Cardiff City","Coventry City","Derby County","Hull City"],"origin":"Gales, England","name":"English Football League Championship","short_name":"Championship","region":"UEFA"}]Session ended, resume using 'kubectl attach curl -c curl -i -t' command when the pod is running
pod "curl" deleted
```

Let's try to test any endpoint of the service for postgres database:

```bash
kubectl run curl --rm -i --tty --image=curlimages/curl -- \
    curl -v http://${uri-service}:8080/v1/venues/positions?lat=-3.6890&long=40.4520
```

Expected output:

```bash
If you don't see a command prompt, try pressing enter.
< HTTP/1.1 200 OK
< Content-Type: application/json
< content-length: 74
< 
* Connection #0 to host champion-league left intact
[{"id":1,"name":"Santiago Bernabéu","documentId":"kIanogZmCsx6jcBKJlpO"}]Session ended, resume using 'kubectl attach curl -c curl -i -t' command when the pod is running
pod "curl" deleted
```
Finally, let's expose the service:

kubectl expose deployment champion-league-deployment --type=LoadBalancer --port=80 --target-port=8080

curl -i -v ${url-service}/v1/venues/positions?lat=-3.6890&long=40.4520

In case you need to expose the service to public Internet, use an Ingress resource in your Kubernetes installation, here an example:

```
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: champion-league
  annotations:
    traefik.ingress.kubernetes.io/router.entrypoints: web
spec:
  rules:
    - host: champion-league.${kubernets_dns}.com
      http:
        paths:
          - path: '/apis'
            pathType: Prefix
            backend:
              service:
                name: champion-league
                port:
                  number: 8080
```

# Reference

Install kubens: https://webinstall.dev/kubens/