package main;

public class Counter implements CounterInterface {

    private volatile int finalCount;
    private volatile int count;

    public Counter(int finalCount) {
        this.finalCount = finalCount;
    }

    @Override
    public synchronized void count() {
        if (count < finalCount) {
            count++;
        }
    }

    @Override
    public boolean finished() {
        return count == finalCount;
    }
}
