package main;

import data.Data;
import data.DataFilter;
import data.DataProcessor;
import file.DataReader;
import file.FileReaderInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    private final int THREAD_COUNT = 4;
    private Thread[] threads;
    private CounterInterface counter;
    private MonitorInterface<Data> finalData;
    private MonitorInterface<Data> monitor;

    private final String[] FILES = new String[] {
            "IFF-7-2_Kristinaitis_Giedrius_L1_dat_1.json",
            "IFF-7-2_Kristinaitis_Giedrius_L1_dat_2.json",
            "IFF-7-2_Kristinaitis_Giedrius_L1_dat_3.json"
    };

    private final String FILE_RESULTS = "IFF-7-2_Kristinaitis_Giedrius_L1_rez.txt";

    private Main() {
        File resultFile = new File(FILE_RESULTS);

        if (resultFile.exists()) {
            resultFile.delete();
        }

        for (String file: FILES) {
            counter = new Counter(25);
            finalData = new Monitor<Data>(25);
            monitor = new Monitor<Data>(10);

            spawnThreads(THREAD_COUNT);
            populateMonitor(file);
            waitForThreadsToFinish();
            printResults(FILE_RESULTS, file);
        }
    }

    private void printResults(String file, String dataFile) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file, true));

            writer.println("----------------------------------------------------------");
            writer.println(dataFile + " Results");
            writer.println("----------------------------------------------------------");
            writer.printf("%25s|%10s|%10s|%10s\n", "Title", "Price", "Quantity", "Result");
            writer.println("----------------------------------------------------------");

            if (finalData.size() > 0) {
                for (int i = 0; i < finalData.size(); i++) {
                    Data data = finalData.get(i);

                    writer.printf("%25s|%10f|%10d|%10s\n", data.getTitle(), data.getPrice(), data.getQuantity(), data.getResult());
                }
            } else {
                writer.println("No results - no elements match the filter");
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
