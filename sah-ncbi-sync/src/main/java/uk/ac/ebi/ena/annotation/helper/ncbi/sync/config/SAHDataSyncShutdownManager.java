package uk.ac.ebi.ena.annotation.helper.ncbi.sync.config;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SAHDataSyncShutdownManager {

    private final ApplicationContext appContext;

    public SAHDataSyncShutdownManager(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    /*
     * Invoke with `0` to indicate no error or different code to indicate
     * abnormal exit. es: shutdownManager.initiateShutdown(0);
     **/
    public void initiateShutdown(int returnCode) {
        SpringApplication.exit(appContext, () -> returnCode);
    }
}
