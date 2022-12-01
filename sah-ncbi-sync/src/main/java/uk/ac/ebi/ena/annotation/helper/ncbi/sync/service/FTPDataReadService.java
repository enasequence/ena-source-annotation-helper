package uk.ac.ebi.ena.annotation.helper.ncbi.sync.service;

import org.springframework.stereotype.Service;

public interface FTPDataReadService {
    public boolean fetchInstitutionsFileFromFTP();
    public boolean fetchCollectionsFileFromFTP();
}
