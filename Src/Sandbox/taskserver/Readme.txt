README for the Java Task Server sample


The Java Task Server sample application demonstrates how Java task components 
can interact with the OptimalFlow workflow system. This server queries the 
workflow system for scheduled batch tasks and invokes them as java components. 



Prerequisites
=============

To run the Java Task Server sample, make sure you have the following installed:

* OptimalFlow 2.2 with service pack S002 or OptimalFlow 2.3.

* Java compiler and runtime: JDK 1.3.1 or higher



How to run the sample application
=================================

1. Import both OrgPackage.npx and TestJavaBatch.vpx into the Process Console. 

2. Set the process package TestJavaBatch to production and save.

3. Compile the Java source files by running the batch file build_taskserver.bat.

4. Make sure that the UST specification in the connectionCfg string is the same 
   as a valid UST specification in the urouter asn file. Installation of 
   OptimalFlow automatically created a default UST called unet which points to 
   the OptimalFlow deployment environment. Also make sure that urouter is 
   running.

5. Start the Business Process Server and a Task Console for George.

6. Start the Java Task Server by running the batch file run_taskserver.bat.

7. In the Task Console start the task MT Start [TestJavaBatch - 0.0M1].

8. Enter a value for the task parameter.

9. Wait a few seconds and see that the task MT End gets scheduled for George,
   displaying the modified ProcessName parameter (the string 'OK.' is appended) 
   in the input data of the task. 



Current functionality
=====================

The Java Task Server queries the workflow system for scheduled batch tasks. 
For each of these tasks, it invokes the specified method on the specified 
class using the specified set of parameters. These specifications are all 
modeled as normal batch tasks. The resulting task output is returned to
the workflow system and the task is set to completed. Should the task fail
to execute for some reason, it is set to the failed state. All three parameter 
types in OptimalFlow (string, numeric, boolean) are supported.



Restrictions
============

The Java Task Server executes the same types of tasks that the (4GL) Task 
Server executes. Running both servers concurrently can lead to unpredictable 
results.

The Java Task Server assumes that the order in which the parameters are 
retrieved, is also the order in which the parameters are defined for the task. 
This may not be the case if there is more than one input parameter. In the 
Workflow Connector that will be shipped with OptimalFlow 2.3, there is an 
option to obtain the order of parameters from the definition; see the 
documentation for ParameterData, method getSequenceNumbers().

Only one output parameter is supported for the Java batch task. 

The component and operation specification in the Modeler and Process Console 
are always uppercase, so the Java class and method should also be uppercase.

The consistency check fails on the batch task component because there is no 
signature defined for the batch task. This error can be ignored.



