package main;

import data.Data;
import data.FilterInterface;
import data.ProcessorInterface;

public class ThreadAction implements Runnable {

    private MonitorInterface<Data> monitor;
    private MonitorInterface<Data> finalData;
    private FilterInterface<Data> filter;
    private ProcessorInterface<Data> processor;
    private CounterInterface counter;

    public ThreadAction(MonitorInterface<Data> monitor, MonitorInterface<Data> finalData, FilterInterface<Data> filter, ProcessorInterface<Data> processor, CounterInterface counter) {
        this.monitor = monitor;
        this.finalData = finalData;
        this.filter = filter;
        this.processor = processor;
        this.counter = counter;
    }

    @Override
    public void run() {
        while (!counter.finished()) {
            Data data = monitor.pop();
            counter.count();
            processor.process(data);
            finalData.add(data);
        }
    }
}
