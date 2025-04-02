# Setup a Spatial Database on Civo (Percona Cluster for PostGIS)

> **_NOTE:_** The setup is using a [Civo Kubernetes cluster](https://civo.com/docs/kubernetes/).

**Content:**

- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Verify the cluster creation](#verify-the-cluster-creation)
- [Add PostGIS extension](#add-postgis-extension)   
- [Testing the database](#testing-the-database)

## Prerequisites

- A Civo account
- A Civo cluster with at least 3 Medium nodes - Standard (2 CPU, 4 GB, 50 GB)
- A Percona Cluster PostgreSQL database (using Kubernetes or terraform installer)
- Add PostGIS extension

## Setup

The approach that we are using is to use the Kubernetes installer. Next lines explain the process.

### Create Kubernetes cluster

Create a Kubernetes K3s cluster in civo, recommended number of nodes is 3, of a medium size.

```bash
civo kubernetes create pggis --cluster-type k3s -p cilium -n 2 -s g4s.kube.medium --save --switch -w
```

Create the namespace for the operator:

```bash
kubectl create namespace pggis-operator
```

Deploy the CRDs and resources needed by the Operator:

```bash
kubectl apply --server-side \
  -f https://raw.githubusercontent.com/percona/percona-postgresql-operator/v2.6.0/deploy/bundle.yaml \
  -n pggis-operator
```

It should install the required kubernetes resources needed for the operator, verify if the Operator pod is up and running:

```bash
kubectl get pods -n pggis-operator
```

expected outout:

```bash
NAME                                           READY   STATUS    RESTARTS   AGE
percona-postgresql-operator-57b5597c68-6s8nz   1/1     Running   0          98s
```

Now, let's deploy the Percona Distribution for PostgreSQL cluster, exchanging the pgp image for a `gis` specific. [Here](https://docs.percona.com/percona-operator-for-postgresql/2.0/images.html) you can find the list of available & certified images by the operator. We are going to use the `percona/percona-postgresql-operator:2.6.0-ppg16.8-postgres-gis3.3.8`

> Download the `percona-operator-cr.yaml` file from [here](https://raw.githubusercontent.com/percona/percona-postgresql-operator/v2.6.0/deploy/cr.yaml) and save it in the current directory.

First, let's configure a user for connect to the database instance using `pgbouncer` in the `percona-operator-cr.yaml` file

```yaml
...

  users:
   - name: supporter
     databases:
       - venues
     options: "SUPERUSER"
     password:
       type: ASCII
     secretName: "supporter-credentials"
...
```

As it is required to install the `postgis` extension, give the chance to the user for connecting as `superuser` using the parameter:  

`exposeSuperusers: true`

```yaml
  proxy:
    pgBouncer:
      replicas: 3
      image: percona/percona-postgresql-operator:2.6.0-ppg16.8-pgbouncer1.24.0
      exposeSuperusers: true    # <-- allow to act as a database superuser
```

Create the Percona Cluster:

```bash
kubectl apply -f percona-operator-cr.yaml -n pggis-operator
```

> **_Note_**: If you don't need to install the gis extension, you can go for the default installation:
> `kubectl apply -f https://raw.githubusercontent.com/percona/percona-postgresql-operator/v2.6.0/deploy/cr.yaml -n postgres-operator`

### Verify the cluster creation

To verify the percona cluster creation, run the following command:

```bash
kubectl get pods -n pggis-operator
```

The expected output is:

```bash
NAME                                           READY   STATUS    RESTARTS   AGE
percona-postgresql-operator-57b5597c68-6s8nz   1/1     Running   0          17m
cluster1-pgbouncer-59d46d5fc8-72lww            2/2     Running   0          3m46s
cluster1-pgbouncer-59d46d5fc8-j4bvf            2/2     Running   0          3m46s
cluster1-pgbouncer-59d46d5fc8-mfdvd            2/2     Running   0          3m46s
cluster1-repo-host-0                           2/2     Running   0          3m46s
cluster1-backup-k9x4-z9xzw                     1/1     Running   0          2m23s
cluster1-instance1-vv4r-0                      4/4     Running   0          3m46s
cluster1-instance1-mqmj-0                      4/4     Running   0          3m46s
cluster1-instance1-nt9w-0                      4/4     Running   0          3m46s
```

Check the Operator and replica set Pods status.

`kubectl get pg -n pggis-operator`

The expected output is:

```bash
NAME       ENDPOINT                                STATUS   POSTGRES   PGBOUNCER   AGE
cluster1   cluster1-pgbouncer.pggis-operator.svc   ready    3          3           6m38s
```

You have successfully installed and deployed the Operator with default parameters.

### Test the database cluster

Get the connection string to the database:

```bash
PGBOUNCER_URI=$(kubectl -n pggis-operator get secrets cluster1-pguser-cluster1 -o jsonpath="{.data.pgbouncer-uri}" | base64 --decode)

PGBOUNCER_SUPPORTER_URI=$(kubectl -n pggis-operator get secrets supporter-credentials -o jsonpath="{.data.pgbouncer-uri}" | base64 --decode)

echo $PGBOUNCER_URI
postgresql://cluster1:d,Nb_1b%3CQ_0nR0VGb%3F%3F7RMR7@cluster1-pgbouncer.pggis-operator.svc:5432/cluster1%
```

Use a temporary pod to access the database:

`kubectl run -i --rm --tty percona-client --image=perconalab/percona-distribution-postgresql:16 --restart=Never -- psql $PGBOUNCER_URI`

Once inside the container, you can run the following commands to check if PostGIS is installed:

```sql
SELECT * FROM pg_available_extensions WHERE name = 'postgis';
```

The expected output is:

```sql
  name   | default_version | installed_version |                          comment
---------+-----------------+-------------------+------------------------------------------------------------
 postgis | 3.3.8           |                   | PostGIS geometry and geography spatial types and functions
```

If PostGIS is not installed, you can install it by running:

```sql
CREATE EXTENSION IF NOT EXISTS postgis;
```

You can verify that PostGIS is installed by running:

```sql
SELECT postgis_full_version();
```

The expected output is:

```sql
 POSTGIS="3.3.8 0" [EXTENSION] PGSQL="160" GEOS="3.11.2-CAPI-1.17.2" PROJ="9.0.1" LIBXML="2.9.13" LIBJSON="0.14" LIBPROTOBUF="1.3.3" WAGYU="0.5.0 (Internal)"
(1 row)
```

## Testing the database

Superb, we are in position now to use our percona cluster for our application. Let's create a simple table to validate that it is working as expected:

> **_Note_**: Here some reference about postGIS: [Official page](https://postgis.net/)

```sql
CREATE TABLE stadiums (
    id SERIAL PRIMARY KEY,             -- Auto-incrementing ID
    name TEXT NOT NULL,                -- Name of the stadium
    document_id VARCHAR(128) NULL,    -- Firebase document ID of the stadium to link the location
    location GEOMETRY(POLYGON, 4326)  -- Location of the stadium
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

Once linked the location of the stadium in PostGIS database with the  associated document in firebase.

Prepare the query to answer a simple question: _Is a Device Inside Any Stadium?_

```sql
SELECT name, document_id FROM stadiums
WHERE ST_Contains(
    location,
    ST_SetSRID(ST_Point(-3.6890, 40.4520), 4326)
);
```

The query will return the name and document ID of the stadium where the point is inside the polygon.

> **_Note_**: Here some reference about postGIS: [Official page](https://postgis.net/)
