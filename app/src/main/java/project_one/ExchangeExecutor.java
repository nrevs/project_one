package project_one;




public class RequestExecutor{

    public void execute(Runnable task) {
        App.logger.traceEntry();

        task.run();

        App.logger.traceExit();
    }

}