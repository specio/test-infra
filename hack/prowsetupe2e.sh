az group delete --name ${RESOURCE_GROUP} --yes
az group delete --name MC_${RESOURCE_GROUP}_oe-${AKS_CLUSTER}_${LOCATION} --yes

az group create --location ${LOCATION} --name ${RESOURCE_GROUP}
az aks create --resource-group ${RESOURCE_GROUP} \
    --name ${AKS_CLUSTER_NAME} \
    --node-vm-size ${NODE_SIZE} \
    --max-count ${MAX_NODE_COUNT} \
    --vm-set-type VirtualMachineScaleSets \
    --load-balancer-sku standard \
    --location ${LOCATION} \
    --enable-cluster-autoscaler \
    --service-principal ${SERVICE_PRINCIPAL} \
    --client-secret ${CLIENT_SECRET} \
    --min-count ${MIN_NODE_COUNT} \
    --max-count ${MAX_NODE_COUNT} \
    --ssh-key-value ${PATH_KEY} \
    --aks-custom-headers "usegen2vm=true"


 az aks get-credentials --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME} --overwrite-existing
 
 kubectl create namespace ingress-basic
 
 helm repo add stable https://kubernetes-charts.storage.googleapis.com

 helm install nginx-ingress stable/nginx-ingress \
    --namespace ingress-basic \
    --set controller.replicaCount=2 \
    --set controller.nodeSelector."beta\.kubernetes\.io/os"=linux \
    --set defaultBackend.nodeSelector."beta\.kubernetes\.io/os"=linux \
    --set rbac.create=true

kubectl create clusterrolebinding cluster-admin-binding-"${USER}" \
  --clusterrole=cluster-admin --user="${USER}"

kubectl create namespace test-pods

openssl rand -hex 20 > $PWD/hmac

kubectl create secret generic hmac-token --from-file=$PWD/hmac

kubectl create secret generic oauth-token --from-file=$PWD/oauth

kubectl -n test-pods create secret generic gcs-credentials --from-file=service-account.json

kubectl apply -f test-infra/config/prow/cluster/configs.yaml
kubectl apply -f test-infra/config/prow/cluster/hook_deployment.yaml
kubectl apply -f test-infra/config/prow/cluster/hook_service.yaml
kubectl apply -f test-infra/config/prow/cluster/plank_deployment.yaml
kubectl apply -f test-infra/config/prow/cluster/sinker_deployment.yaml
kubectl apply -f test-infra/config/prow/cluster/deck_deployment.yaml
kubectl apply -f test-infra/config/prow/cluster/deck_service.yaml
kubectl apply -f test-infra/config/prow/cluster/horolgium_deployment.yaml
kubectl apply -f test-infra/config/prow/cluster/tide_deployment.yaml
kubectl apply -f test-infra/config/prow/cluster/tide_service.yaml
kubectl apply -f test-infra/config/prow/cluster/ing_ingress.yaml
kubectl apply -f test-infra/config/prow/cluster/statusreconciler_deployment.yaml
kubectl apply -f test-infra/config/prow/cluster/test_pods.yaml
kubectl apply -f test-infra/config/prow/cluster/deck_rbac.yaml
kubectl apply -f test-infra/config/prow/cluster/horolgium_rbac.yaml
kubectl apply -f test-infra/config/prow/cluster/plank_rbac.yaml
kubectl apply -f test-infra/config/prow/cluster/sinker_rbac.yaml
kubectl apply -f test-infra/config/prow/cluster/hook_rbac.yaml
kubectl apply -f test-infra/config/prow/cluster/tide_rbac.yaml
kubectl apply -f test-infra/config/prow/cluster/statusreconciler_rbac.yaml
kubectl apply -f test-infra/config/prow/cluster/crier_rbac.yaml
kubectl apply -f test-infra/config/prow/cluster/crier_deployment.yaml

kubectl get service -l app=nginx-ingress --namespace ingress-basic

kubectl get deployments -w 