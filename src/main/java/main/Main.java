package main;

import data.Data;
import data.DataFilter;
import data.DataProcessor;
import file.DataReader;
import file.FileReaderInterface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

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
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(file));

            writer.printf("%25s|%10s|%10s|%10s\n", "Title", "Price", "Quantity", "Result");
            writer.println("----------------------------------------------------------");

            for (int i = 0; i < finalData.size(); i++) {
                Data data = finalData.get(i);

                writer.printf("%25s|%10f|%10d|%10s\n", data.getTitle(), data.getPrice(), data.getQuantity(), data.getResult());
            }

            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
