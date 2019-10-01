package main;

public interface MonitorInterface<T extends Comparable<T>> {

    void add(T t);

    T pop();

    T get(int index);

    int size();

    void clear();

    void setWillHaveMoreData(boolean willHaveMoreData);
}
