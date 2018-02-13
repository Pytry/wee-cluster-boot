#!/usr/bin/env bash

if [ -z "$JAVA_HOME" ]; then
    echo "You need to install Java 8"
    exit 1
fi

echo 'WARNING! This script will run 10 new java processes in the background.'
echo 'Continue? (y/n)'
read answer

if [ ${answer} != 'y' ]; then
    echo "DONE"
    exit 0
fi

./mvnw -U clean install

mkdir ./target
mkdir ./target/cluster

i=0
while [ ${i} -lt 10 ]; do
    mkdir ./target/cluster/node${i}
    cp ./simple-cluster/target/simple-cluster.jar ./target/cluster/node${i}
    java -jar target/cluster/node${i}/simple-cluster.jar &
    let i=i+1
done

echo DONE
