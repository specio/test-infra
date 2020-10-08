#!/bin/bash 
set +x

#Create directories if they do not exist

echo "Creating config directories if they do not exist.."

# Get all build config types
build_configs=$(yq r $PWD/config.yml build-configs)
org=openenclave
i=0
for build_config in $build_configs
do
    # Get all supported repos
    repos=$(yq r $PWD/config.yml repos)

    # Generate $build_config...
    for repo in $repos
    do
        echo "Creating $repo config directories if they do not exist.."

        mkdir "$PWD/$repo" -p

        echo "Get templates for $repo.."
        
        headers=$(cat $PWD/../templates/headers/$build_config.yml)
        echo "$headers" > $PWD/$repo/$repo-$build_config.yaml

        if [ "$build_config" = "pre-submits" ] || [ "$build_config" = "postsubmits" ] 
        then
            echo "  openenclave-ci/${repo}:" >> $PWD/$repo/$repo-$build_config.yaml
        fi

        operating_systems=$(yq r $PWD/config.yml $repo.prowjob-operating-systems)
        for operating_system in $operating_systems
        do
            
            build_modes=$(yq r $PWD/config.yml $repo.build-modes)
            for build_mode in $build_modes
            do

                build_types=$(yq r $PWD/config.yml $repo.build-types-lin)
                for build_type in $build_types
                do

                    compilers=$(yq r $PWD/config.yml $repo.compilers)
                    for compiler in $compilers
                    do

                        if [ "$build_mode" = "simulation" ]
                        then
# TODO: Not evaluating correctly, seems the tabs are being injected into the job config which is not what we want,
# as it negatively effects user readability.
eval "cat <<EOF
$(<$PWD/../templates/general/$build_config.yml)        
EOF
" >> $PWD/$repo/$repo-$build_config.yaml
                        else
eval "cat <<EOF
$(<$PWD/../templates/general/acc/$build_config.yml)        
EOF
" >> $PWD/$repo/$repo-$build_config.yaml
                        fi
                        i=$((i+1))
                    done
                done
            done
        done
## jenkins jobs
        #windows_version=$(yq r $PWD/config.yml $repo.win-operating-systems)
        for windows_version in 2016 2019
        do
            
            build_modes=$(yq r $PWD/config.yml $repo.build-modes)
            for build_mode in $build_modes
            do

                build_types=$(yq r $PWD/config.yml $repo.build-types-win)
                for build_type in $build_types
                do


eval "cat <<EOF
$(<$PWD/../templates/general/jenkins/windows/$build_config.yml)        
EOF
" >> $PWD/$repo/$repo-$build_config.yaml
                i=$((i+1))
                done
            done
        done
#windows_version=$(yq r $PWD/config.yml $repo.win-operating-systems)
        for rhel_version in 8.1
        do
            
            build_modes=$(yq r $PWD/config.yml $repo.build-modes)
            for build_mode in $build_modes
            do

                build_types=$(yq r $PWD/config.yml $repo.build-types-lin)
                for build_type in $build_types
                do

eval "cat <<EOF
$(<$PWD/../templates/general/jenkins/rhel/$build_config.yml)        
EOF
" >> $PWD/$repo/$repo-$build_config.yaml
                i=$((i+1))
                done
            done
        done 
        for lin_version in 1604 1804
        do 
            for win_version in 2016 2019
            do
            build_types=$(yq r $PWD/config.yml $repo.build-types-win)
            for build_type in $build_types
            do

eval "cat <<EOF
$(<$PWD/../templates/general/jenkins/complex/host-verify/$build_config.yml)        
EOF
" >> $PWD/$repo/$repo-$build_config.yaml
                i=$((i+1))
                done
            done
        done
        for lin_version in 1604 1804
        do 
            for win_version in 2016 2019
            do
            build_types=$(yq r $PWD/config.yml $repo.build-types-win)
            for build_type in $build_types
            do

eval "cat <<EOF
$(<$PWD/../templates/general/jenkins/complex/linuxelf/$build_config.yml)        
EOF
" >> $PWD/$repo/$repo-$build_config.yaml
                i=$((i+1))
                done
            done
        done 
    done
done