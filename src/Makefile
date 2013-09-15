JFLAGS =
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Encode.java \
	Decode.java \
	PrintNums.java \
	main/Main.java \
	common/Command.java \
	common/Config.java \
	common/Marshaller.java \
	common/Status.java \
	master/Master.java \
	master/MasterCommunicator.java \
	master/MasterConnectionHandler.java \
	master/MasterMessage.java \
	migratableProcess/MigratableProcess.java \
	transactionalIO/TransactionalFileInputStream.java \
	transactionalIO/TransactionalFileOutputStream.java \
	worker/WorkerPrime.java \
	worker/WorkerMessage.java \
	worker/WorkerMonitor.java \

RMCLASSES = \
	Encode.class \
	Decode.class \
	PrintNums.class \
	main/Main.class \
	common/Command.class \
	common/Config.class \
	common/Marshaller.class \
	common/Status.class \
	master/Master.class \
	master/MasterCommunicator.class \
	master/MasterConnectionHandler.class \
	master/MasterMessage.class \
	migratableProcess/MigratableProcess.class \
	transactionalIO/TransactionalFileInputStream.class \
	transactionalIO/TransactionalFileOutputStream.class \
	worker/WorkerPrime*.class \
	worker/WorkerMessage.class \
	worker/WorkerMonitor.class \


default: classes

classes: $(CLASSES:.java=.class)

clean:	
	rm $(RMCLASSES)
	
