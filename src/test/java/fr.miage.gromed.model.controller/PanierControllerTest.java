package fr.miage.gromed.model.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miage.gromed.controller.PanierController;
import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.dto.PanierItemDto;
import fr.miage.gromed.service.PanierService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(PanierController.class)
public class PanierControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PanierService panierService;


    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldInsertPanier() throws Exception {

        PanierItemDto panierItemDto =PanierItemDto.builder().presentationCip(3400956216468L).quantite(1).delayed(false).build();


        given(this.panierService.createPanier(any(PanierItemDto.class)))
                .willReturn(new PanierDto());

        MockHttpServletResponse response = mvc.perform(
                                post("/panier/create")
                                .content(mapper.writeValueAsString(panierItemDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
//        this.mvc.perform(
//                        post("/api/session")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(mapper.writeValueAsString(panierItemDto))
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is("id")))
//                .andExpect(jsonPath("$.title", is("My Spring session")));
    }

}