package file;

public interface FileReaderInterface<T> {

    T[] readAll(String file);
}
