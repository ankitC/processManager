Lab 1 - Migratable Processes

15-440
Gregory Kesden
15 September 2013

Ankit Chheda (achheda)
Vinay Balaji (vbalaji)

DESIGN

One instance of a master server interacts with one or more instances
of worker servers.

The master has a foreground thread that presents a console (control panel)
along with background threads that manage connections and communications
with workers.

Each worker can be assigned a task (a migratable process) to run, and 
informs the master server once the task has completed.

The master console allows users to create, start, suspend, and migrate
processes from one worker to another, as well as being able to display
a list of all workers and processes along with their statuses.

HOW TO RUN

1. The first step towards getting our project up and running is to make
some changes to the configuration file located at src/common/Config.java.

Here, the parameters of interest are the server port and the serialization directory:
	- the server port is the port which the master will listen on
	- the serialization directory is where all migratable processes
	  are written to / read from; ensure that you have read/write
	  permissions in this directory

2. The next step is to run one or more of the following scripts:
	- package.sh: compiles all java files and creates a jar
	- execute.sh [master server name / IP]: 
		if given the location of the master server, either as 
			the server name or the IP address, starts a worker instance
		otherwise, starts a master instance
	- clean.sh: deletes all generated class files and the jar file
	- run.sh [master server name / IP]:
		same as running 
			package.sh; execute.sh [master server name / IP]

	These scripts MUST be run from the ROOT directory of the project.

USING THE MASTER CONSOLE

Once the master server instance has been started, you will be presented 
with a console. The following commands are available:
	- <MigratableProcessName> [args...]
		constructs a new migratable process with the specified arguments,
		returning a process ID which can be used to start and suspend 
		the newly created process; process is initially suspended
	- cmd start <processID> <nodeID> : 
		starts the specified process on the specified node
	- cmd suspend <processID> :
		suspends the specified process
	- cmd ls nodes
		lists all available workers
	- cmd ls ps
		lists all unfinished processes
	- exit
		terminates the master along with all of its workers and processes

We have provided three examples of migratable processes:
	- Encode <infile> <outfile>
		This process takes the input file and encodes each character into 
		ASCII, writing the results into the specified output file. 
		This encoded file can then be decoded by the the included Decode class 
		mentioned below. 
	- Decode <infile> <outfile>
		This process takes the input file and decodes each character as if 
		it were ASCII, writing the results into the specified output file. 
	- PrintNums
		This process simply prints the numbers 1 through 13 to stdout.

ASSUMPTIONS

1. We have a distributed filesystem.
2. Once a worker joins, it never leaves / terminates.
3. The port specified in the configuration file is not being used.
4. The serialization directory specified in the configuration file
   can be read from / written to.
5. Processes are always created with correct arguments.
