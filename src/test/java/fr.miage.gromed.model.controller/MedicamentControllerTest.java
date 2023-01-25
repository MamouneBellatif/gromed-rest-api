package fr.miage.gromed.model.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.google.gson.Gson;
import fr.miage.gromed.model.controller.CustomUtils;
import fr.miage.gromed.model.controller.MedicamentController;
import fr.miage.gromed.model.dto.MedicamentDto;
import fr.miage.gromed.model.mapper.EntityMapper;
import fr.miage.gromed.model.mapper.MedicamentMapper;
import fr.miage.gromed.model.medicament.Medicament;
import fr.miage.gromed.model.service.MedicamentService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

@Transactional
public class MedicamentControllerTest {
    private static final String ENDPOINT_URL = "/api/medicament";
    @InjectMocks
    private MedicamentController medicamentController;
    @Mock
    private MedicamentService medicamentService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(medicamentController)
                //.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                //.addFilter(CustomFilter::doFilter)
                .build();
    }

    @Test
    public void findAllByPage() throws Exception {
        Page<MedicamentDto> page = new PageImpl<>(Collections.singletonList(MedicamentBuilder.getDto()));

        Mockito.when(medicamentService.findByCondition(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(page);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content", Matchers.hasSize(1)));

        Mockito.verify(medicamentService, Mockito.times(1)).findByCondition(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(medicamentService);

    }

    @Test
    public void getById() throws Exception {
        Mockito.when(medicamentService.findById(ArgumentMatchers.anyLong())).thenReturn(MedicamentBuilder.getDto());

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)));
        Mockito.verify(medicamentService, Mockito.times(1)).findById("1");
        Mockito.verifyNoMoreInteractions(medicamentService);
    }

    @Test
    public void save() throws Exception {
        Mockito.when(medicamentService.save(ArgumentMatchers.any(MedicamentDto.class))).thenReturn(MedicamentBuilder.getDto());

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(MedicamentBuilder.getDto())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Mockito.verify(medicamentService, Mockito.times(1)).save(ArgumentMatchers.any(MedicamentDto.class));
        Mockito.verifyNoMoreInteractions(medicamentService);
    }

    @Test
    public void update() throws Exception {
        Mockito.when(medicamentService.update(ArgumentMatchers.any(), ArgumentMatchers.anyLong())).thenReturn(MedicamentBuilder.getDto());

        mockMvc.perform(
                        MockMvcRequestBuilders.put(ENDPOINT_URL + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(MedicamentBuilder.getDto())))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(medicamentService, Mockito.times(1)).update(ArgumentMatchers.any(MedicamentDto.class), ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(medicamentService);
    }

    @Test
    public void delete() throws Exception {
        Mockito.doNothing().when(medicamentService).deleteById(ArgumentMatchers.anyLong());
        mockMvc.perform(
                MockMvcRequestBuilders.delete(ENDPOINT_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CustomUtils.asJsonString(MedicamentBuilder.getIds()))).andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(medicamentService, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(medicamentService);
    }
}