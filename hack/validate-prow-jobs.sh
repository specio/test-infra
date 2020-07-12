# We use the  Kubernetes tools
git clone https://github.com/kubernetes/test-infra/ /prow-tools
cd /prow-tools

# Validate prow jobs
bazel-3.0.0 run //prow/cmd/checkconfig -- --plugin-config=/config/prow/plugins.yaml --config-path=/config/prow/config.yaml