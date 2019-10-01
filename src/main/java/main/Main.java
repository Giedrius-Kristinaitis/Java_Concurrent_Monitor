package main;

import data.Data;
import data.DataFilter;
import data.DataProcessor;
import file.DataReader;
import file.FileReaderInterface;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    private final int THREAD_COUNT = 4;
    private Thread[] threads;
    private CounterInterface counter = new Counter(25);
    private MonitorInterface<Data> finalData = new Monitor<Data>(25);
    private MonitorInterface<Data> monitor = new Monitor<Data>(10);

    private final String FILE_ONE = "IFF-7-2_Kristinaitis_Giedrius_L1_dat_1.json";
    private final String FILE_TWO = "";
    private final String FILE_THREE = "";
    private final String FILE_RESULTS = "IFF-7-2_Kristinaitis_Giedrius_L1_rez.txt";

    private Main() {
        spawnThreads(THREAD_COUNT);
        populateMonitor(FILE_ONE);
        waitForThreadsToFinish();
        printResults(FILE_RESULTS);
    }

    private void printResults(String file) {

    }

    private void populateMonitor(String file) {
        FileReaderInterface<Object> reader = new DataReader();

        for (Object data : reader.readAll(file)) {
            monitor.add((Data) data);
        }

        monitor.setWillHaveMoreData(false);
    }

    private void waitForThreadsToFinish() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void spawnThreads(int count) {
        threads = new Thread[count];

        for (int i = 0; i < count; i++) {
            threads[i] = new Thread(new ThreadAction(monitor, finalData, new DataFilter(), new DataProcessor(), counter));
            threads[i].start();
        }
    }
}
