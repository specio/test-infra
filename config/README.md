# Getting Started on AKS with Prow


## Install dependencies

```
apt install -y python3-pip curl git vim 

```
## Install Azure CLI
```
curl -sL https://aka.ms/InstallAzureCLIDeb | bash
```

## Install Kubectl
``` 
curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin/kubectl
kubectl version --client
```
## Install Helm
```
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash
```

## Install Bazel
```
apt install curl gnupg
curl https://bazel.build/bazel-release.pub.gpg | sudo apt-key add -
echo "deb [arch=amd64] https://storage.googleapis.com/bazel-apt stable jdk1.8" | sudo tee /etc/apt/sources.list.d/bazel.list
apt update && apt install bazel
apt update && apt full-upgrade -y
```
## Get Prow tools
```
git clone https://github.com/kubernetes/test-infra.git /tools/
```

## AZ login
```
az login
```

## Set Subscription
```
az account set --subscription "${SUBSCRIPTION_ID}"
```

## Create AKS RBAC Service Principal
```
az ad sp create-for-rbac --skip-assignment --name myAKSClusterServicePrincipal
```

## Create Resource Group
```
az group create --location ${LOCATION} --name ${RESOURCE_GROUP}
```

## Create AKS Cluster
```
az aks create --resource-group ${RESOURCE_GROUP} \
    --name ${AKS_CLUSTER} \
    --node-count ${NODE_COUNT} \
    --node-vm-size ${NODE_SIZE} \
    --max-count ${MAX_NODE_COUNT} \
    --vm-set-type VirtualMachineScaleSets \
    --load-balancer-sku standard \
    --location ${LOCATION} \
    --enable-cluster-autoscaler \
    --service-principal ${SERVICE_PRINCIPAL} \
    --client-secret ${CLIENT_SECRET} \
    --min-count 3 \
    --max-count 10 \
    --ssh-key-value ${PATH_KEY}
```

## Get Credentials
```
az aks get-credentials --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER}
```

## Create a namespace for your ingress resources
```
kubectl create namespace ingress-basic
```

## Get Nodes
```
kubectl get nodes
```
## Add the official stable repository
```
helm repo add stable https://kubernetes-charts.storage.googleapis.com/
```

## Use Helm to deploy an NGINX ingress controller
```
helm install nginx-ingress stable/nginx-ingress \
    --namespace ingress-basic \
    --set controller.replicaCount=2 \
    --set controller.nodeSelector."beta\.kubernetes\.io/os"=linux \
    --set defaultBackend.nodeSelector."beta\.kubernetes\.io/os"=linux \
    --set rbac.create=true
```
## Watch Progress
```
kubectl --namespace ingress-basic get services -o wide -w nginx-ingress-controller
```
## Create cluster role bindings
```
kubectl create clusterrolebinding cluster-admin-binding-"${USER}" \
  --clusterrole=cluster-admin --user="${USER}"
```

## Create GH secrets:
```
openssl rand -hex 20 > /secret
kubectl create secret generic hmac-token --from-file=/hmac
kubectl create secret generic oauth-token --from-file=/oauth
```

## Add the prow components to the cluster
```
kubectl apply -f config/prow/cluster/starter.yaml
```

## Check deployment status
```
kubectl get deployments -w
```
## Get Ingress IP address
```
kubectl get ingress ing
```

## Get Public IP
```
kubectl get service -l app=nginx-ingress --namespace ingress-basic
```

## Clean up
```
az group delete --name ${RESOURCE_GROUP} --yes --no-wait
az group delete --name MC_$