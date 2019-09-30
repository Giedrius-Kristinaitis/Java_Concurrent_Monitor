package data;

public class DataProcessor implements ProcessorInterface<Data> {

    @Override
    public void process(Data data) {
        if (data != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
        }
    }
}
