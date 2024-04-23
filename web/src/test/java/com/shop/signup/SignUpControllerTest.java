package com.shop.signup;

import com.shop.security.SignInPasswordAuthenticationProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static com.shop.category.CategoryParameter.getCategoryWithId;
import static com.shop.signup.SignUpController.SIGN_UP_URL;
import static com.shop.signup.SignUpParameter.getSignUpData;
import static com.shop.utilities.JsonUtility.getJsonBody;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBeans({
    @MockBean(PasswordEncoder.class),
    @MockBean(SignInPasswordAuthenticationProvider.class)
})
@WebMvcTest(SignUpController.class)
class SignUpControllerTest {
    @Autowired
    @MockBean
    private SignUpService signUpService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Sign up request")
    void sign_up_request() throws Exception {
        mockMvc.perform(
                post(SIGN_UP_URL)
                    .with(anonymous())
                    .with(user("username").roles("ANONYMOUS"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getJsonBody(getSignUpData()))
            )
            .andExpect(status().isOk());
    }
}
