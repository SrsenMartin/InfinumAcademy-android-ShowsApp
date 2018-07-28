package srsen.martin.infinum.co.hw3_and_on.database;

public interface DatabaseCallback<T> {

    void onSuccess(T data);
    void onError(Throwable t);
}
