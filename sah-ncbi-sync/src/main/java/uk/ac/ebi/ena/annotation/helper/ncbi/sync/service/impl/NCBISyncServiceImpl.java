package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.impl;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.config.SAHDataSyncShutdownManager;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.NCBISyncService;

@Component
public class NCBISyncServiceImpl implements NCBISyncService {

    final FTPDataReadServiceImpl ftpDataReadService;
    final SAHDataSyncShutdownManager sahDataSyncShutdownManager;

    // files storage path on local server (used for processing and maintaining the history)
    public static final String local_server_path = "/hps/nobackup/cochrane/ena/index/source-attribute-helper";

    public NCBISyncServiceImpl(FTPDataReadServiceImpl ftpDataReadService,
                               SAHDataSyncShutdownManager sahDataSyncShutdownManager) {
        this.ftpDataReadService = ftpDataReadService;
        this.sahDataSyncShutdownManager = sahDataSyncShutdownManager;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void processNCBIDataRead() {
        ftpDataReadService.fetchInstitutionsFileFromFTP();
        ftpDataReadService.fetchCollectionsFileFromFTP();;
        sahDataSyncShutdownManager.initiateShutdown(0);
    }


}
