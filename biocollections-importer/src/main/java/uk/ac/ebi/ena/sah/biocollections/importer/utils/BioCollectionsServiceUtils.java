package uk.ac.ebi.ena.sah.biocollections.importer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

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

    public static String[] getSortListIdxForCleanup (Map<LocalDateTime, String> unsortedMap) {
        log.debug("Unsorted Map :\n");
        for (Map.Entry<LocalDateTime, String> entry : unsortedMap.entrySet()) {
            log.debug(entry.getKey() + "   " + entry.getValue());
        }

        System.out.println("\n");
        System.out.println("Sorting Map Based on Keys :\n");
        Map<LocalDateTime, String> keySortedMap = new TreeMap<LocalDateTime, String>(unsortedMap);
        for (Map.Entry<LocalDateTime, String> entry : keySortedMap.entrySet()) {
            log.debug(entry.getKey() + "   " + entry.getValue());
        }

        String[] array = keySortedMap.values().toArray(new String[0]);
        for (int i=0; i< array.length-1; i++) {
            log.debug("array["+i+"] -->  " + array[i]);
        }
        return array;
    }


}
