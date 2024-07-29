package listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LoggingConfigListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
	    try (InputStream is = getClass().getClassLoader().getResourceAsStream("logging.properties")) {
	        if (is != null) {
	            LogManager.getLogManager().readConfiguration(is);
	            System.out.println("Logging configuration loaded successfully.");
	        } else {
	            System.err.println("Could not find logging.properties file in the classpath");
	        }
	    } catch (IOException e) {
	        System.err.println("Could not load logging.properties file");
	        e.printStackTrace();
	    }
	}

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // コンテキスト終了時の処理（必要があれば）
    }
}