package main;

@SuppressWarnings("unchecked")
public class Monitor<T extends Object> implements MonitorInterface<T> {

    private volatile Object[] data;
    private volatile int count;
    private volatile boolean willHaveMoreData = true;

    public Monitor(int size) {
        data = new Object[size];
    }

    @Override
    public synchronized void add(T t) {
        while (count == data.length - 1) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        data[count++] = t;

        notifyAll();
    }

    @Override
    public synchronized T pop() {
        while (count == 0) {
            try {
                if (willHaveMoreData) {
                    wait();
                } else {
                    return null;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        T item = (T) data[count - 1];
        data[count - 1] = null;
        count--;

        notifyAll();

        return item;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public synchronized void clear() {
        data = new Object[data.length];
        count = 0;
    }

    @Override
    public void setWillHaveMoreData(boolean willHaveMoreData) {
        this.willHaveMoreData = willHaveMoreData;
    }
}
