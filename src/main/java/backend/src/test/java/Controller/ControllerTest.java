package Controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testConsultaRoteiro() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/roteiros/GRU-CDG")
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(content().string(equalTo("Best route: GRU -> BRC -> SCL -> ORL -> CDG > 40.0")))
                .andExpect(status().isOk());
    }

    @Test
    public void testInsereRoteiro() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/roteiros/AAA,ZZZ,99/insert")
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk());
    }

    @Test
    public void testConsultaRoteiroInserido() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/roteiros/AAA-ZZZ")
        		.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string(equalTo("Best route: AAA -> ZZZ > 99.0")))
                .andExpect(status().isOk());
    }

    @Test
    public void testPing() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/")
        		.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Sorry! My answers are limited. You have to ask the right questions!")));
    }
    
    @Test
    public void testTesting() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/test")
        		.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Testing 1, 2, 3!")));
    }
}
