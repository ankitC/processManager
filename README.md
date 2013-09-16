Lab 1 - Migratable Processes

15-440
Gregory Kesden
15 September 2013

Ankit Chheda (achheda)
Vinay Balaji (vbalaji)

DESIGN

One instance of a master server interacts with one or more instances
of worker servers.

The master instance MUST be started before any worker instances.

The master has a foreground thread that presents a console (control panel)
along with background threads that manage connections and communications
with workers.

Each worker can be assigned a task (a migratable process) to run, and 
informs the master server once the task has completed.

The master console allows users to create, start, suspend, and migrate
processes from one worker to another, as well as being able to display
a list of all workers and processes along with their statuses.

There are three premade migratable processes for your use, although
you may create more to use with our framework. The only requirements
for new migratable processes are that they must adhere to the 
MigratableProcess interface, and correctly set flags indicating
when they are running / suspended / complete.

HOW TO RUN

1. The first step towards getting our project up and running is to make
some changes to the configuration file located at src/common/Config.java.

Here, the parameters of interest are the server port and 
the serialization directory:
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

	The easiest way to start a master instance is to run:
		bash run.sh
	Similarly, the easiest way to start a worker instance is to run:
		bash run.sh <master server port>
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
		lists all available workers (lists the ID's)
	- cmd ls ps
		lists all unfinished processes
	- help
		lists all available commands
	- exit
		terminates the master along with all of its workers and processes

We have provided three examples of migratable processes:
	- Encode <path to infile> <path to outfile>
		This process takes the input file and encodes each character into 
		ASCII, writing the results into the specified output file. This encoded
		file can then be decoded by the the included Decode class 
		mentioned below.
	- Decode <path to infile> <path to outfile>
		This process takes the input file and decodes each character as if 
		it were ASCII, writing the results into the specified output file. 
	- PrintNums
		This process simply prints the numbers 1 through 13 to stdout.

DEMO 1

1. Ensure that you have correctly set the configuration parameters.

2. Navigate to the root of the project directory.

3. Start the master instance by executing the following command:
	$ bash run.sh
   You should now see a command prompt that looks like the following:
   	Master-->

4. On two other machines, repeat steps 1-2 and execute the following command:
	$ bash run.sh <IP address of master server>

5. On the master server, execute:
	Master--> PrintNums
	Master--> cmd start 0 0
   You should see the numbers 1 through 13 print on the screen,
   one number every second.

6. At some point before the number "13" is printed, type in:
	Master--> cmd suspend 0

7. Now, resume the process on the other worker:
	Master--> cmd start 0 1
   You should see the last few numbers print to the screen.	

DEMO 2

1. Ensure that you have correctly set the configuration parameters.

2. Navigate to the root of the project directory.

3. Start the master instance by executing the following command:
	$ bash run.sh
   You should now see a command prompt that looks like the following:
   	Master-->

4. On two other machines, repeat steps 1-2 and execute the following command:
	$ bash run.sh <IP address of master server>

5. On the master server, execute:
	Master--> cmd ls nodes
   You should see that there are now two workers connected to the master.

6. Now type in:
	Master--> Encode demo/demo.txt demo/encoded.txt

7. Type in:
	 Master--> cmd ls ps
   You should see one suspended process.

8. Now run this process (the Encode process) on our first worker:
	Master--> cmd start 0 0    

9. Type in:
	 Master--> cmd ls ps
   You should see one running process.	

10. After around 3-4 seconds, type in:
	Master--> cmd suspend 0
   This should suspend the Encode process.

11. To migrate this process to our second machine:
	 Master--> cmd start 0 1
	Allow up to 30 seconds for this process to complete.

12. Use your favorite text editor to verify that a non-empty file
    "encoded.txt" was created inside the src/ directory.

13. Now to decode this file:
	 Master--> Decode demo/encoded.txt demo/decoded.txt
	 Master--> cmd start 1 0

14. After around 3-4 seconds, type in:
	 Master--> cmd suspend 1
	 Master--> cmd start 1 1
	Allow up to 30 seconds for this process to complete. 

15. Use your favorite text editor to verify that a non-empty file
    "decoded.txt" was created inside the "src/" directory. Also, this file
    should be identical to the file "demo.txt", also located inside "src/". 

ASSUMPTIONS

1. We have a distributed filesystem.
2. Once a worker joins, it never leaves / terminates.
3. The port specified in the configuration file is not being used.
4. The serialization directory specified in the configuration file
   can be read from / written to.
5. Processes are always created with correct arguments.