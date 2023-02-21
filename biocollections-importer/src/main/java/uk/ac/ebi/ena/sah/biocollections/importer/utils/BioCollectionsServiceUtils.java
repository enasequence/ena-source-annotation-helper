package uk.ac.ebi.ena.sah.biocollections.importer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class BioCollectionsServiceUtils {

    public static String getResourceAsString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static double calculatePercentChanged(long validatedRecords, long existingRecords) {
        if (existingRecords == 0) {
            return 0;
        } else {
            if (validatedRecords > existingRecords) return 0;
            return (existingRecords - validatedRecords) / (double) existingRecords * 100.0;
        }
    }

}
