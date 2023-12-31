package com.web.config;

import com.web.dto.CustomUserDetails;
import com.web.entity.Authority;
import com.web.entity.Person;
import com.web.jwt.JwtTokenProvider;
import com.web.repository.AuthorityRepository;
import com.web.repository.PersonRepository;
import com.web.utils.Contains;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private PersonRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        Optional<Person> user = userRepository.findByUsername(email);
        String token = "";
        Person person= null;
        if(user.isPresent()){
            CustomUserDetails customUserDetails = new CustomUserDetails(user.get());
            token = jwtTokenProvider.generateToken(customUserDetails);
            person = user.get();
        }
        else{
            Person u = new Person();
            u.setPersonId(UUID.randomUUID().toString());
            Authority existed = authorityRepository.findByName(Contains.ROLE_GUEST);
            if (existed!=null){
                u.setAuthorities(existed);
                u.setActived(true);
                u.setUsername(email);
                Person p = userRepository.save(u);
                CustomUserDetails customUserDetails = new CustomUserDetails(u);
                token = jwtTokenProvider.generateToken(customUserDetails);
                person = p;
            }
        }

        // xu ly chuyen trang khi login thanh cong(dinh kem token vao url
        // hoặc set vào session hay cookie sẽ đảm bảo hơn)
        HttpSession session = request.getSession();
        session.setAttribute("token", token);
        if(person.getAuthorities().getName().equals(Contains.ROLE_ADMIN)){
            response.sendRedirect(Contains.URL  + "/api/admin/home?token="+token);
        }
        if(person.getAuthorities().getName().equals(Contains.ROLE_STUDENT)){
            response.sendRedirect(Contains.URL + "/api/student/home?token="+token);
        }
        if(person.getAuthorities().getName().equals(Contains.ROLE_GUEST)){
            response.sendRedirect(Contains.URL  + "/?token="+token);
        }
        if(person.getAuthorities().getName().equals(Contains.ROLE_LECTURER)){
            response.sendRedirect(Contains.URL  + "/api/lecturer/home?token="+token);
        }
        if(person.getAuthorities().getName().equals(Contains.ROLE_HEAD)){
            response.sendRedirect(Contains.URL + "/api/head/home?token="+token);
        }
    }

}
