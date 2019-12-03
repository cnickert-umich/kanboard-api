package edu.umich.kanboard.column;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umich.kanboard.CustomGlobalExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@RunWith(MockitoJUnitRunner.Silent.class)
public class ColumnControllerTest {

    @Mock
    private ColumnService columnService;

    @InjectMocks
    private ColumnController columnController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(columnController)
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    public void getAllColumns_success() throws Exception {
        List<ColumnEntity> columns = new ArrayList<>();
        for (int i = 0; i < 10 * Math.random(); i++) {
            columns.add(new ColumnEntity());
        }

        when(columnService.getAllColumns()).thenReturn(columns);
        get("/column").andExpect(status().isOk()).andReturn();
    }

    @Test
    public void createColumn_badName() throws Exception {
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setId((long) 1);
        columnEntity.setName("yes");
        when(columnService.createOrUpdateColumn(columnEntity)).thenThrow(new ColumnExceptions.ColumnInvalidNameException(columnEntity.getName()));

        post("/column", objectMapper.writeValueAsString(columnEntity)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void createColumn_tooManyColumns() throws Exception {
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setId((long) 1);
        columnEntity.setName("yes");
        when(columnService.createOrUpdateColumn(columnEntity)).thenThrow(new ColumnExceptions.ColumnTooManyException());

        post("/column", objectMapper.writeValueAsString(columnEntity)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void createColumn_columnNameTooLong() throws Exception {
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setId((long) 1);
        columnEntity.setName("yes");
        when(columnService.createOrUpdateColumn(columnEntity)).thenThrow(new ColumnExceptions.ColumnNameTooLong());

        post("/column", objectMapper.writeValueAsString(columnEntity)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void createColumn_success() throws Exception {
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setId((long) 1);
        columnEntity.setName("yes");
        when(columnService.createOrUpdateColumn(columnEntity)).thenReturn(columnEntity);

        post("/column", objectMapper.writeValueAsString(columnEntity)).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void deleteColumn_tooFewColumns() throws Exception {
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setId((long) 1);
        columnEntity.setName("yes");
        Mockito.doThrow(ColumnExceptions.ColumnTooFewException.class).when(columnService).deleteColumn(columnEntity.getId());
        delete("/column/" + columnEntity.getId(), objectMapper.writeValueAsString(columnEntity)).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void deleteColumn_success() throws Exception {
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setId((long) 1);
        columnEntity.setName("yes");
        Mockito.doNothing().when(columnService).deleteColumn(columnEntity.getId());
        delete("/column/" + columnEntity.getId(), objectMapper.writeValueAsString(columnEntity)).andExpect(status().isOk()).andReturn();
    }


    private ResultActions get(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(url))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions post(String url, String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions delete(String url, String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }
}