package main;

public interface MonitorInterface<T> {

    void add(T t);

    T pop();

    int size();

    void clear();

    void setWillHaveMoreData(boolean willHaveMoreData);
}
