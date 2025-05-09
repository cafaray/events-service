version=$1
compile=$2
STARTTIME=`date +%s`
echo "***   `date`: Starting the script."
if [ "$compile" = "compile" ]; then
    echo "***   compiling and packaging version $version";
    mvn clean package
    if [[ "$?" -ne 0 ]] ; then
        echo 'could not perform packaging'; exit $rc
    fi
fi

echo "***   Creating docker image version $version"
docker build \
    -f /Users/wendigo/workspace/champion-league/src/main/docker/Dockerfile.jvm \
    --platform=linux/amd64 \
    -t cafaray/champions-league:$version .
echo "***   new image is ready to push: cafaray/champions-league:$version";
docker push cafaray/champions-league:$version
echo "***   `date`: The script has finished."
ENDTIME=`date +%s`
let DURATION=${ENDTIME}-${STARTTIME}
echo "***   That took ${DURATION} seconds"

echo "***   deploying to kubernetes using image cafaray/champions-league:$version"

sed -i '' "s|image:.*|image: cafaray/champions-league:$version|" manifests/champion-league-deploy.yaml
echo "***   Ready to run $(cat manifests/champion-league-deploy.yaml)"
kubectl delete deploy champion-league -n pggis-operator
kubectl apply -f manifests/champion-league-deploy.yaml -n pggis-operator
kubectl get pods -n pggis-operator champion-league -w