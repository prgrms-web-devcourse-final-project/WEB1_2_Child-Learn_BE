// package com.prgrms.ijuju.domain.chat;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class ChatControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Test
//     @WithMockUser // 인증된 사용자로 테스트
//     public void testGetChatRoomList() throws Exception {
//         mockMvc.perform(get("/api/v1/chat/list"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     @WithMockUser
//     public void testCreateChatRoom() throws Exception {
//         mockMvc.perform(post("/api/v1/chat/rooms")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"friendId\": 2}"))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     @WithMockUser
//     public void testDeleteChatRoom() throws Exception {
//         mockMvc.perform(delete("/api/v1/chat/rooms/{roomId}", 1))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     @WithMockUser
//     public void testSendMessage() throws Exception {
//         mockMvc.perform(post("/chat/message")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"roomId\": 1, \"content\": \"안녕하세요!\", \"imageUrl\": null}"))
//                 .andExpect(status().isOk());
//     }
// }
