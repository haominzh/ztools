### Getting Started Guide

1. Implement DataSupplier
```java
public class MyDataSupplier implements DataSupplier<MyOriginalData> {
    
    @Override
    public MyOriginalData get() {
 
        // This method will fetch data from source,
        // and return MyOriginalData object
    }
}
```

2. Implement JobSteps
```java
public class MyFirstStep implements JobStep<MyOriginalData, MyFirstOutput> {
 
    @Override
    public MyFirstOutput execute(MyOriginalData input, JobContext context) {
        // This method will convert MyOriginalData to MyFirstOutput.
    }
}
```

3. Add more steps
```java
public class MySecondStep implements JobStep<MyFirstOutput, MySecondOutput> {
 
    @Override
    public MySecondOutput execute(MyFirstOutput input, JobContext context) {
        // This method will convert MyFirstOutput to MySecondOutput. 
        // The MyFirstOutput is coming from MyFirstStep.
    }
}
```

4. Implement LastStep
```java
public class MyLastStep implements LastStep<MySecondOutput> {

    @Override
    public void end(MySecondOutput input, JobContext context) {
        // This method will consume MySecondOutput object coming
        // from MySecondStep
    }
}
```

5. Assemble and run the pipeline
```java
SimpleJobSequence.startFrom(new MyFirstStep())
                 .andThen(new MySecondStep())
                 .andThen(new MyLastStep())
                 .buildPipelineWithDataSupplier(new MyDataSupplier())
                 .run();
```

#### Recommendations
1. It's recommended to implement `LastStep`, and override `end` method if the JobStep is the last one.
2. You can override `getJobStepName` (for JobSteps), or `getDataSupplierName` (for DataSuppliers) to provide a "display" name if needed. The default name is `getClass().getSimpleName()`
3. `JobContext` currently provide
    * getJobId(): returning a job UUID
    * get execution stats by name for both data supplier and job steps. The `name` is defined by `getJobStepName` and `getDataSupplierName`
    * get all execution stats: returning a map of execution stats.

   When you write log in JobStep implementations, it's recommended to include JobID for better troubleshooting.