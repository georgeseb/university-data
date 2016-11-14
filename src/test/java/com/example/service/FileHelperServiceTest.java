package com.example.service;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileHelperServiceTest {


    @Test
    public void fileExistsTest() throws IOException {

        FileHelperService service = new FileHelperService();

        String fileName = "test_" + Long.toString(System.currentTimeMillis());

        assertFalse(service.fileExists(fileName));

        File tempFile = new File(FileHelperService.getFileName(fileName));

        tempFile.createNewFile();

        try {
            assertFalse(service.fileExists(fileName));
        } finally {
            tempFile.delete();
        }
    }
}
