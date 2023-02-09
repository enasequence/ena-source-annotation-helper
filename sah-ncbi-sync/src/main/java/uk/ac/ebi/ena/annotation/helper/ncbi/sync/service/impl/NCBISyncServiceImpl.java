package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service.impl;

import lombok.extern.slf4j.Slf4j;
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

    @Override
    public boolean processNCBIDataRead() {
        try {
            //fetch biocollections data
            boolean fetchSuccessful = importBioCollectionsData();
            if (!fetchSuccessful) {
                return false;
            }
            // persist biocollections data
            persistBioCollectionsData();
            return true;
        } catch (Exception ex) {
            log.info("Error occurred while processing the NCBI data import.");
            return false;
        }
    }

    /**
     * importBioCollectionsData.
     *
     * @return
     */
    public boolean importBioCollectionsData() {
        log.info("Importing Data for Institutions - Started");
        //fetch the data from the FTP server
        boolean instImport = institutionDataReadService.fetchDataFileFromFTP();
        if (!instImport) {
            log.info("Failed to import the Institutions data.");
            return false;
        }
        //fetch the data from the FTP server
        log.info("Importing Data for Collections - Started");
        boolean collImport = collectionDataReadService.fetchDataFileFromFTP();
        if (!collImport) {
            log.info("Failed to import the Collections data.");
            return false;
        }
        return true;
    }

    /**
     * persistBioCollectionsData.
     *
     * @return
     */
    public boolean persistBioCollectionsData() {
        //fetch and persist Collections data
        log.info("Persisting Data for Bio-collections - Started");
        try {
            // processing for the institutions
            // create a new index
            institutionRepository.createInstitutionIndex();
            // populate the index with data fetched from FTP server
            institutionRepository.saveAll(SAHDataObject.mapInstitutions.values());
            log.info("Processing Data for Institutions - Finished");
            //TODO move the alias to new index
            if (institutionRepository.moveInstitutionIndexAlias()) {
                log.info("Alias moved to new Institution Index Successfully.");
            }

            // processing for the collections
            //fetch the data from the FTP server
            collectionDataReadService.fetchDataFileFromFTP();
            // create a new index
            collectionRepository.createCollectionIndex();
            // populate the index with data fetched from FTP server
            collectionRepository.saveAll(SAHDataObject.mapCollections.values());
            log.info("Processing Data for Collections - Finished");
            //TODO move the alias to new index
            if (collectionRepository.moveCollectionIndexAlias()) {
                log.info("Alias moved to new Collection Index Successfully.");
            }
            return true;
        } catch (IOException e) {
            log.info("Failed - Persisting Data for Bio-collections");
            return false;
        }
    }

}
