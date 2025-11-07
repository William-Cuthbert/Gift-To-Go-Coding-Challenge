package com.gift2go;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileService {

    @Autowired
    public FileService() {}

    public OutcomeFileDto generateOutcomeFile(EntryFileDto entryFile) {
        log.info("Generate outcome file process has started with file id= {}", entryFile.uuid());

        log.info("Generate outcome file process has completed without errors with file id= {}", entryFile.uuid());
        return null;
    }
}
