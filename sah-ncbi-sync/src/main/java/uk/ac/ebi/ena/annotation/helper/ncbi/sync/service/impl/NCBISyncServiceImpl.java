package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.config.SAHDataSyncShutdownManager;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.data.SAHDataObject;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.repository.InstitutionRepository;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.NCBISyncService;

import java.io.IOException;

@Slf4j
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

        try {
            //fetch and persist institutions data
            processInstitutionsData();
            //fetch and persist institutions data
            processCollectionsData();
        } catch (Exception ex) {
            log.info("Error occurred while processing the NCBI data import.");
        } finally {
            sahDataSyncShutdownManager.initiateShutdown(0);
        }
    }

    public void processInstitutionsData() {
        //fetch and persist institutions data
        try {
            //fetch the data from the FTP server
            institutionDataReadService.fetchDataFileFromFTP();
            // create a new index
            institutionRepository.createInstitutionIndex();
            // populate the index with data fetched from FTP server
            institutionRepository.saveAll(SAHDataObject.mapInstitutions.values());
        } catch (IOException e) {
            log.info("Failed to process the Institutions data.");
            sahDataSyncShutdownManager.initiateShutdown(0);
        }
        sahDataSyncShutdownManager.initiateShutdown(0);
    }

    public void processCollectionsData() {
        //fetch and persist institutions data
        try {
            //fetch the data from the FTP server
            collectionDataReadService.fetchDataFileFromFTP();
            // create a new index
            collectionRepository.createCollectionIndex();
            // populate the index with data fetched from FTP server
            collectionRepository.saveAll(SAHDataObject.mapCollections.values());
        } catch (IOException e) {
            log.info("Failed to process the Collections data.");
            sahDataSyncShutdownManager.initiateShutdown(0);
        }
    }


}
