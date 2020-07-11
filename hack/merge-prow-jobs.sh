# Script for merging in all prow jobs into one config for consumption

# Find mainline prow Config.yaml
configFile=$PWD/config/prow/config.yaml

# Find prow job yaml configs to merge
find $PWD/config/jobs -name config.yaml

configArray=$(find $PWD/config/jobs -name *.yaml)
for config in $configArray
do
    yq m -i $configFile $config
done

cat $PWD/config/prow/config.yaml