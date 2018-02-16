# Wee Cluster Boot

This is an example of using leadership election for performing certain 
actions.

The "simple-cluster" module is nothing more than a main class 
annotated with @SpringBootApplication. All you need to do to get
the "wee-cluster-boot" going, is to include it on the classpath 
of your project.

## To Change The Message

Modify "./simple-cluster/src/main/resources/application.yml" by setting 
ot changing the value for the "xitikit.cluster-boot.allHereMessage" property.

Example:

    xitikit:
        cluster-boot:
            allHereMessage: Why does a mouse when it spins?

## To Run On Windows

- Open a command prompt in the root project directory.
- Execute the "run-cluster.cmd" script.
- WARNING! This script will open 10 additional command 
prompt windows, each running a simple-cluster node that 
has started on a random port value.

## To Run on Linux

- Open a terminal in the root project directory.
- Make sure that the "run-cluster.sh" file is executable
    
    sudo chmod +x ./run-cluster.sh
    
- Run the Script

    ./run-cluster.sh

- WARNING! This will start 10 processes (by default) 
running in the background. They should terminate once 
the terminal closes, but I cannot make any promises.

## To Debug in IDE of Choice

Google it :)

