Lab 1: Migratable Process

Ankit Chheda (achheda) and Vinay Balaji(vbalaji)

Brief:
Process Migration framework enables to move work around to worker nodes. You can start work on the master console and then move it to any node using console commands.


1. High-Level Design Description:

When you run the master, a master console interacts with the admin controlling the execution. A 'MasterConnectionAcceptor' is monitoring for the incoming connections from the workers. Whenever a worker node connects to a master, a 'MasterListener' is instantiated to communicate with the worker node.

A bunch of workers on other machinces using <program.jar> -s <master hostname>. Whenever a worker node is started, it starts of a main thread which monitors for the incoming messages from the master. Another thread is responsible for monitoring that all the work being assigned to the master is being done.

The admin starts a process from the console. It can then direct the process to the node using :
"cmd start <processID> <nodeID>" 

A process that is started can be suspended using: 
"cmd suspend <processID>" 

The process can again be started using "cmd start <processID> <nodeID>".

The nodes can be listed using 
"cmd ls nodes"

A list of running and suspended processes is obtained by 
"cmd ls ps"

Migratable Processes:

Encode <infile>  <outfile>

This process takes the input file and encodes into output file which can be decoded by the the Decode class included. The encoding process can be suspended by calling the suspend(). It can again be resumed on any node.

Decode <infile> <outfile>

Takes in a encoded 'infile' and decodes it to get the original text content back to the 'outfile'. The decoding process can also be suspended using the suspend(). It can again be started using "cmd start <processID> <nodeID>"


Assumptions:
We assume that once the worker node joins in; it shall not leave.
The infile for the migratable process are always present.
The configuration parameters are set in the config file which will describe the location where all the processes are serialized.