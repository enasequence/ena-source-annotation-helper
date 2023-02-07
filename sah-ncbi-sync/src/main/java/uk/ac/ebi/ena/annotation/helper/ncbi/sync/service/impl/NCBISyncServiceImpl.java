package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.impl;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.config.SAHDataSyncShutdownManager;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.data.SAHDataObject;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository.InstitutionRepository;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.NCBISyncService;

import java.io.IOException;

@Component
public class NCBISyncServiceImpl implements NCBISyncService {

    final InstitutionDataReadServiceImpl institutionDataReadService;
    final CollectionDataReadServiceImpl collectionDataReadService;
    final SAHDataSyncShutdownManager sahDataSyncShutdownManager;
    final InstitutionRepository institutionRepository;
    final CollectionRepository collectionRepository;

    // files storage path on local server (used for processing and maintaining the history)
    public static final String local_server_path = "/hps/nobackup/cochrane/ena/index/source-attribute-helper";

    public NCBISyncServiceImpl(InstitutionDataReadServiceImpl institutionDataReadService,
                               CollectionDataReadServiceImpl collectionDataReadService,
                               SAHDataSyncShutdownManager sahDataSyncShutdownManager,
                               InstitutionRepository institutionRepository, CollectionRepository collectionRepository) {
        this.institutionDataReadService = institutionDataReadService;
        this.collectionDataReadService = collectionDataReadService;
        this.sahDataSyncShutdownManager = sahDataSyncShutdownManager;
        this.institutionRepository = institutionRepository;
        this.collectionRepository = collectionRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void processNCBIDataRead() {
        //fetch and persist institutions data
        try {
            institutionDataReadService.fetchDataFileFromFTP();
            institutionRepository.saveAll(SAHDataObject.mapInstitutions.values());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //fetch and persist collections data
        try {
            collectionDataReadService.fetchDataFileFromFTP();
            collectionRepository.saveAll(SAHDataObject.mapCollections.values());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sahDataSyncShutdownManager.initiateShutdown(0);
    }


}
