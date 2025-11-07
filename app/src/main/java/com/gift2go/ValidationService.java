package com.gift2go;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {

    @Autowired
    public ValidationService() {}

    public void validate(List<EntryFileDto> entries) {
        for (EntryFileDto entry : entries) {

        }
    }
}
