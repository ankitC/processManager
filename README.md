ProcessManager:

Make a jar for eg. processManager.jar

To start the master:

java -jar processManager.jar


To start the worker:

java -jar processManager.jar -s <IP/hostname of the master>


On the master, a console is shown like:

Master-> <enter your commands here>

Available commands:

cmd ls nodes ->lists the nodes
cmd ls ps -> lists the running and suspended processes

Encode <path to input file> <path to the output file> ->Create a migratable process of class Encode
Decode <path to input file> <path to output file> ->Create a migratable process of class Decode

start <processid> <nodeid> -> starts the process "processid" on node "nodeid"
suspend <processid> -> suspends the particular processid

Testing done:

Master node comes up.
Worker nodes come up and connect to the master.
Process gets created and serialized

Tests now to be done:
start command
suspend command

