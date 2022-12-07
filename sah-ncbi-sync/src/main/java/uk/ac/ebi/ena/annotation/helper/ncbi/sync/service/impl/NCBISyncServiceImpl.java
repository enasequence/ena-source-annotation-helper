package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.impl;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.config.SAHDataSyncShutdownManager;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.data.SAHDataObject;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository.InstitutionRepository;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.NCBISyncService;

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
        institutionDataReadService.fetchDataFileFromFTP();
        collectionDataReadService.fetchDataFileFromFTP();
        persistDataObjects();
        sahDataSyncShutdownManager.initiateShutdown(0);
    }

    public boolean persistDataObjects() {
        institutionRepository.saveAll(SAHDataObject.mapInstitutions.values());
        collectionRepository.saveAll(SAHDataObject.mapCollections.values());
        return true;
    }


}
