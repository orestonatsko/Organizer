package ua.com.info.data;

/**
 * Створив VM 03.12.2017.
 */

public interface IDataBase {

    interface Listener {
        /**
         * Виконується коли запит виконано
         */
        void onExecuted(Result result);
    }

    void Execute(String cmd, Parameters parameters, Listener listener);

    void getTable(String cmd, Parameters parameters, Listener listener);

    Result getTable(String cmd, Parameters parameters);

    void getData(String cmd, Parameters parameters, Listener listener);

    void Batch(Data batch, Listener listener);

    void close();

    boolean isConnected();

    void openConnection();
}
