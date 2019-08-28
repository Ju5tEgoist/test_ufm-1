package com.andersen.test_ufm.akka.actors;

import com.andersen.test_ufm.repository.ClientDBRepository;
import com.andersen.test_ufm.repository.FileRepository;
import com.andersen.test_ufm.service.IProcessService;
import com.mongodb.DBObject;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;


/**
 * Created by abraxas on 13.01.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class ActorUtilTest {

    private static final String TEST_DIR = "test";

    @Mock
    private IProcessService processService;
    @Mock
    FileRepository fileRepository;
    @Mock
    private ClientDBRepository clientDBRepository;

    @InjectMocks
    private ActorUtil util = new ActorUtil();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(util, "processedFilesDir", TEST_DIR);
    }


    @Test
    public void shouldProcessSuccessfully(){
        JSONObject jsonObject = mock(JSONObject.class);
        DBObject dbObject = mock(DBObject.class);
        File file = new File("src/test/resources/test.json");

        when(processService.process(Mockito.any(JSONObject.class))).thenReturn(jsonObject);
        when(clientDBRepository.createClient(jsonObject)).thenReturn(dbObject);

        util.process(file);

        verify(processService).process(Mockito.any(JSONObject.class));
        ArgumentCaptor<File> destFileCaptor = ArgumentCaptor.forClass(File.class);
        verify(fileRepository).copyFile(eq(file), destFileCaptor.capture());
        Assert.assertEquals(TEST_DIR + "/test.json", destFileCaptor.getValue().getPath());
        verify(fileRepository).deleteFile(file);
        verify(fileRepository).createOutputFile(file.getName(), jsonObject);
        verify(clientDBRepository).createClient(jsonObject);
    }

    @Test
    public void shouldNotProcessNonJSONFile(){
        DBObject dbObject = mock(DBObject.class);
        File file = new File("README.md");
        JSONObject emptyJson = new JSONObject();

        when(clientDBRepository.createClient(eq(emptyJson))).thenReturn(dbObject);

        util.process(file);

        ArgumentCaptor<File> destFileCaptor = ArgumentCaptor.forClass(File.class);
        verify(fileRepository).copyFile(eq(file), destFileCaptor.capture());
        Assert.assertEquals(TEST_DIR + "/README.md", destFileCaptor.getValue().getPath());
        verify(fileRepository).deleteFile(file);
        verify(fileRepository).createOutputFile(eq(file.getName()), eq(emptyJson));
        verify(clientDBRepository).createClient(eq(emptyJson));
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEOnProcessingNull(){
        util.process(null);
    }

}