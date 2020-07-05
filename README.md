# lazy-benchmark-annotation
timing benchmark for class methods

# How to use
1. download ![lazy-benchmark-annotation.jar](
https://github.com/DmitryBelenov/lazy-benchmark-annotation/blob/master/lazy-benchmark-annotation.jar)
2. append it as library into your project
3. use over methods you wish to measure execution time for:
# @LazyBenchmark 
4. use with parameter to set priority of method invokation:
# @LazyBenchmark(priority = 1) 
5. create class with 'main' method in 'test' path of your project (or in 'src/main/java/...' path)
6. write in 'main' method: 
# new Benchmark(objects, classes...) 
   and put into constructor the array of your custom reference types and classes contains methods you want to measure execution time for
   
   Example: see BenchmarkTest class in /test path of project 
   
7. start 'main' method, see results. Easy) 

This simple timing benchmark is best to use for measure of timing alghorithms invocations and make faster if your results are not satisfactory.
