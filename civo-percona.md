
## Prepare the cluster.

Create a Kubernetes K3s cluster in civo, recommended number of nodes is 3, of a medium size.

```bash 
civo kubernetes create pggis --cluster-type k3s -p cilium -n 2 -s g4s.kube.medium --save --switch -w
```

## Using Kubernetes

Create the namespace for the operator:

`kubectl create namespace pggis-operator`

Deploy the CRDs and resources needed by the Operator using 

`kubectl apply --server-side -f https://raw.githubusercontent.com/percona/percona-postgresql-operator/v2.6.0/deploy/bundle.yaml -n pggis-operator`

It should install the required kubernetes resources needed for the operator, verify if the Operator pod is up and running:

```
$ kubectl get pods -n pggis-operator
NAME                                           READY   STATUS    RESTARTS   AGE
percona-postgresql-operator-57b5597c68-6s8nz   1/1     Running   0          98s
```

Now, let's deploy the Percona Distribution for PostgreSQL cluster, exchanging the pgp image for a `gis` specific. [Here](https://docs.percona.com/percona-operator-for-postgresql/2.0/images.html) you can find the list of available & certified images by the operator. I'm going to use the `percona/percona-postgresql-operator:2.6.0-ppg16.8-postgres-gis3.3.8`

Configure a user for connect to the database instance using `pgbouncer` in the `percona-operator-cr.yaml` file

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

```

Give the chance to the user for connecting as `superuser` using the `exposeSuperusers: true`

```yaml
  proxy:
    pgBouncer:
      replicas: 3
      image: percona/percona-postgresql-operator:2.6.0-ppg16.8-pgbouncer1.24.0
      exposeSuperusers: true
```

Create the cluster:

`kubectl apply -f ~/workspace/percona-operator-cr.yaml -n pggis-operator`

> If you don't need to install the gis extension, you can go for it:
> `kubectl apply -f https://raw.githubusercontent.com/percona/percona-postgresql-operator/v2.6.0/deploy/cr.yaml -n postgres-operator`

Verify the cluster creation:

```bash
k get pods -n pggis-operator
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

```bash
kubectl get pg -n pggis-operator
NAME       ENDPOINT                                STATUS   POSTGRES   PGBOUNCER   AGE
cluster1   cluster1-pgbouncer.pggis-operator.svc   ready    3          3           6m38s
```

You have successfully installed and deployed the Operator with default parameters.

## Test the database cluster.

Get the connection string:

```bash
PGBOUNCER_URI=$(kubectl -n pggis-operator get secrets cluster1-pguser-cluster1 -o jsonpath="{.data.pgbouncer-uri}" | base64 --decode)

PGBOUNCER_SUPPORTER_URI=$(kubectl -n pggis-operator get secrets supporter-credentials -o jsonpath="{.data.pgbouncer-uri}" | base64 --decode)

echo $PGBOUNCER_URI
postgresql://cluster1:d,Nb_1b%3CQ_0nR0VGb%3F%3F7RMR7@cluster1-pgbouncer.pggis-operator.svc:5432/cluster1%
```

Use a temporary pod to access the database:

`kubectl run -i --rm --tty percona-client --image=perconalab/percona-distribution-postgresql:16 --restart=Never -- psql $PGBOUNCER_URI`

kubectl run -i --rm --tty percona-client --image=perconalab/percona-distribution-postgresql:16 --restart=Never -- psql -U supporter -h venues-pgbouncer.pggis-operator.svc -d venues -W "u%40+hOjxrL;ym-b5XV_Yh_y%3C0"

# SELECT * FROM pg_available_extensions WHERE name = 'postgis';
  name   | default_version | installed_version |                          comment
---------+-----------------+-------------------+------------------------------------------------------------
 postgis | 3.3.8           |                   | PostGIS geometry and geography spatial types and functions

CREATE EXTENSION IF NOT EXISTS postgis;

CREATE EXTENSION

# SELECT postgis_full_version();
                                                                     postgis_full_version
--------------------------------------------------------------------------------------------------------------------------------------------------------------
 POSTGIS="3.3.8 0" [EXTENSION] PGSQL="160" GEOS="3.11.2-CAPI-1.17.2" PROJ="9.0.1" LIBXML="2.9.13" LIBJSON="0.14" LIBPROTOBUF="1.3.3" WAGYU="0.5.0 (Internal)"
(1 row)



## Using Helm
helm repo add percona https://percona.github.io/percona-helm-charts/
helm repo update


kubectl create namespace test-percona

namespace/test-percona created

The `my-operator` parameter is the name of a new release object  which is created for the Operator when you install its Helm chart (use any name you like).

helm install my-operator percona/pg-operator --namespace test-percona

NAME: my-operator
LAST DEPLOYED: Wed Mar 26 18:05:56 2025
NAMESPACE: test-percona
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
1. Percona Operator for PostgreSQL is deployed.
  See if the operator Pod is running:

    kubectl get pods -l app.kubernetes.io/name=pg-operator --namespace test-percona

  Check the operator logs if the Pod is not starting:

    export POD=$(kubectl get pods -l app.kubernetes.io/name=pg-operator --namespace test-percona --output name)
    kubectl logs $POD --namespace=test-percona

2. Deploy the database cluster from pg-db chart:

    helm install my-db percona/pg-db --namespace=test-percona

Read more in our documentation: https://docs.percona.com/percona-operator-for-postgresql/2.0/

kubectl get pods -l app.kubernetes.io/name=pg-operator --namespace test-percona

NAME                                       READY   STATUS    RESTARTS   AGE
my-operator-pg-operator-57f7bd5cc6-mx2fl   1/1     Running   0          105s

```bash
helm install percona-cluster percona/pg-db -n test-percona

NAME: percona-cluster
LAST DEPLOYED: Wed Mar 26 19:05:04 2025
NAMESPACE: test-percona
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
#

                    %                        _____
                   %%%                      |  __ \
                 ###%%%%%%%%%%%%*           | |__) |__ _ __ ___ ___  _ __   __ _
                ###  ##%%      %%%%         |  ___/ _ \ '__/ __/ _ \| '_ \ / _` |
              ####     ##%       %%%%       | |  |  __/ | | (_| (_) | | | | (_| |
             ###        ####      %%%       |_|   \___|_|  \___\___/|_| |_|\__,_|
           ,((###         ###     %%%        _      _          _____                       _
          (((( (###        ####  %%%%       | |   / _ \       / ____|                     | |
         (((     ((#         ######         | | _| (_) |___  | (___   __ _ _   _  __ _  __| |
       ((((       (((#        ####          | |/ /> _ </ __|  \___ \ / _` | | | |/ _` |/ _` |
      /((          ,(((        *###         |   <| (_) \__ \  ____) | (_| | |_| | (_| | (_| |
    ////             (((         ####       |_|\_\\___/|___/ |_____/ \__, |\__,_|\__,_|\__,_|
   ///                ((((        ####                                  | |
 /////////////(((((((((((((((((########                                 |_|   Join @ percona.com/k8s


Join Percona Squad! Get early access to new product features, invite-only ”ask me anything” sessions with Percona Kubernetes experts, and monthly swag raffles.

>>> https://percona.com/k8s <<<

To get a PostgreSQL prompt inside your new cluster you can run:

  PGBOUNCER_URI=$(kubectl -n test-percona get secrets percona-cluster-pg-db-pguser-percona-cluster-pg-db -o jsonpath="{.data.pgbouncer-uri}" | base64 --decode)

And then connect to a cluster with a temporary Pod:

  $ kubectl run -i --rm --tty percona-client --image=perconalab/percona-distribution-postgresql:16 --restart=Never \
  -- psql $PGBOUNCER_URI
```

The cluster1 parameter is the name of a new release object  which is created for the Percona Distribution for PostgreSQL when you install its Helm chart (use any name you like).  

The creation process is over when both the Operator and replica set Pods report the ready status:

```bash
kubectl get pg -n test-percona
NAME                    ENDPOINT                                           STATUS   POSTGRES   PGBOUNCER   AGE
percona-cluster-pg-db   percona-cluster-pg-db-pgbouncer.test-percona.svc   ready    3          3           5m27s
```
```bash
PGBOUNCER_URI=$(kubectl -n test-percona get secrets percona-cluster-pg-db-pguser-percona-cluster-pg-db -o jsonpath="{.data.pgbouncer-uri}" | base64 --decode)
```

```bash
kubectl run -i --rm --tty percona-client --image=perconalab/percona-distribution-postgresql:16 --restart=Never \
  -- psql $PGBOUNCER_URI
If you don't see a command prompt, try pressing enter.
percona-cluster-pg-db=> \db
       List of tablespaces
    Name    |  Owner   | Location
------------+----------+----------
 pg_default | postgres |
 pg_global  | postgres |
(2 rows)

percona-cluster-pg-db=> \dt
Did not find any relations.
percona-cluster-pg-db=>
```
Check if btree_gist is Available
Once inside the PostgreSQL shell, run:

percona-cluster-pg-db=> SELECT * FROM pg_available_extensions WHERE name = 'btree_gist';
    name    | default_version | installed_version |                    comment
------------+-----------------+-------------------+-----------------------------------------------
 btree_gist | 1.7             |                   | support for indexing common datatypes in GiST

 Install and Enable btree_gist
If the extension is available but not installed, enable it in your database:

```
CREATE EXTENSION IF NOT EXISTS btree_gist;
CREATE EXTENSION
```

Verify the installation:

SELECT * FROM pg_extension WHERE extname = 'btree_gist';
  oid  |  extname   | extowner | extnamespace | extrelocatable | extversion | extconfig | extcondition
-------+------------+----------+--------------+----------------+------------+-----------+--------------
 16485 | btree_gist |    16479 |        16480 | t              | 1.7        |           |
(1 row)

Use btree_gist in Indexing
Once btree_gist is enabled, you can create indexes using it.

Example: Composite GiST Index



# References.

[Percona installation using Helm](https://docs.percona.com/percona-operator-for-postgresql/2.0/helm.html)
