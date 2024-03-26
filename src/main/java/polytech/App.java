package polytech;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void init() {
        logger.info("---Start application---");
    }

    public void run() {
        logger.info("---The end---");
    }

    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.run();
    }
}