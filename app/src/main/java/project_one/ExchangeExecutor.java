package project_one;

import java.util.concurrent.Executor;

public class ExchangeExecutor implements Executor {

    public void execute(Runnable task) {
        App.logger.traceEntry();

        task.run();

        App.logger.traceExit();
    }

}