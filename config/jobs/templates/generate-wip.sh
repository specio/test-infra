#!/bin/bash 
set +x

# Get repo name
jobs=$(yq r $PWD/config.yml jobs)

for job in $jobs
do
    jobmaps=$(yq r $PWD/config.yml jobmaps.$job)
    for jobmap in $jobmaps
    do
        echo "$jobmap - > $job" 
    done
done

