package com.shkvlnc.bujo_app.web;

import com.shkvlnc.bujo_app.dto.InboxResponse;
import com.shkvlnc.bujo_app.dto.TagResponse;
import com.shkvlnc.bujo_app.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)   // ✅ Correct annotation
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Test
    void getTagsByTagName_returnsTagResponseList() throws Exception {
        // Arrange
        TagResponse tagResponse = new TagResponse(1L, "Work");
        when(tagService.getTagsByTagName("Work")).thenReturn(List.of(tagResponse));

        // Act & Assert
        mockMvc.perform(get("/tags/search")
                        .param("name", "Work")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Work"));
    }

    @Test
    void getInboxesByTagName_returnsInboxResponseList() throws Exception {
        // Arrange
        InboxResponse inboxResponse = new InboxResponse(1L, "Test Task", "HIGH", "OPEN");
        when(tagService.getInboxesByTagName("Work")).thenReturn(List.of(inboxResponse));

        // Act & Assert
        mockMvc.perform(get("/tags/Work/inboxes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }
}
