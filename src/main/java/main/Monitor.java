package main;

@SuppressWarnings("unchecked")
public class Monitor<T extends Comparable<T>> implements MonitorInterface<T> {

    private volatile Object[] data;
    private volatile int count;
    private volatile boolean willHaveMoreData = true;

    public Monitor(int size) {
        data = new Object[size];
    }

    @Override
    public synchronized void add(T t) {
        while (count == data.length) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        int index = findIndex(t);

        if (data[index] != null) {
            shiftElements(index);
        }

        data[index] = t;

        count++;

        notifyAll();
    }

    private synchronized void shiftElements(int index) {
        for (int i = data.length - 1; i > index; i--) {
            data[i] = data[i - 1];
        }

        data[index] = null;
    }

    private synchronized int findIndex(T t) {
        int index = 0;

        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                break;
            }

            if (((Comparable<T>) data[i]).compareTo(t) <= 0) {
                index = i + 1;
            } else {
                break;
            }
        }

        return index;
    }

    @Override
    public synchronized T pop() {
        while (count == 0) {
            try {
                if (willHaveMoreData) {
                    wait();
                } else if (count == 0) {
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
    public synchronized T get(int index) {
        if (index < 0 || index > data.length) {
            throw new IllegalArgumentException("Invalid index");
        }

        return (T) data[index];
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public synchronized void clear() {
        data = new Object[data.length];
        count = 0;
        notifyAll();
    }

    @Override
    public void setWillHaveMoreData(boolean willHaveMoreData) {
        this.willHaveMoreData = willHaveMoreData;
    }
}
