package com.andersen.test_ufm.repository;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by abraxas on 15.01.2018.
 */
public class FileRepositoryTest {

    private File fileInputFilesDir = mock(File.class);
    private static final String OUTPUT_FILES_DIR = "src" + File.separator + "test" + File.separator + "resources";

    private FileRepository repository = new FileRepository();


    @Before
    public void init(){
        ReflectionTestUtils.setField(repository, "fileInputFilesDir", fileInputFilesDir);
        ReflectionTestUtils.setField(repository, "outputFilesDir", OUTPUT_FILES_DIR);
    }

    @Test
    public void shouldGetAllInputFiles(){
        File[] files = new File[1];

        when(fileInputFilesDir.listFiles()).thenReturn(files);

        List<File> actualFiles = repository.getListInputFiles();

        verify(fileInputFilesDir).listFiles();

        Assert.assertNotNull(actualFiles);

    }

    @Test
    public void shouldCreateNewList(){
        when(fileInputFilesDir.listFiles()).thenReturn(null);

        List<File> actualFiles = repository.getListInputFiles();

        verify(fileInputFilesDir).listFiles();

        Assert.assertNotNull(actualFiles);

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE() throws NoSuchFileException {
        File secondFile = new File(OUTPUT_FILES_DIR + File.separator + "input/test.json");
        repository.moveFile(null, secondFile);
    }

    @Test(expected = java.nio.file.NoSuchFileException.class)
    public void shouldThrowNoSuchFileException() throws NoSuchFileException {
        File firstFile = new File("input/test.json");
        File secondFile = new File(OUTPUT_FILES_DIR + File.separator + "input/test.json");
        repository.moveFile(firstFile, secondFile);
    }

    @Test
    public void shouldMoveFile() throws NoSuchFileException {
        File firstFile = new File(OUTPUT_FILES_DIR + File.separator + "input/test.json");
        File secondFile = new File(OUTPUT_FILES_DIR + File.separator + "input/test.json");
        repository.moveFile(firstFile, secondFile);
    }


    @Test
    public void shouldSuccessfullyCreateOutputFile(){
        String fileName = "shouldCreateOutputFile";
        JSONObject jsonObject = new JSONObject();

        repository.createOutputFile(fileName, jsonObject);
    }

    @Test
    public void shouldCatchIOException(){
        JSONObject jsonObject = new JSONObject();
        repository.createOutputFile(null, jsonObject);
    }

}