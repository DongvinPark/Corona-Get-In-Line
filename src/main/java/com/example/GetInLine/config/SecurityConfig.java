package com.example.GetInLine.config;


import com.example.GetInLine.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*@Bean
    public PasswordEncoder passwordEncoder(){
        //페스워드 인코더의 선택을 상황에 맞게 자동으로 설정될 수 있도록 위임하겠다는 의미이다.
        //단, 이 경우, resources 패키지 내의 data.sql에서 관리자의 정보를 담고 있는 테이블의
        //튜플에서 'password'항목의 내용물들을 '1234'에서 '{noop}1234'로 바꿔줘야 한다.
        //실제 서비스 단계에서는 {bcrypt}같은 암호화 알고리즘을 넣어야 하지만, 그럴 경우 1234 대신
        //1234가 bcrypt에 의해서 암호화된 내용으로 바꿔줘야 하기 때문에 개발단계에서는 {bcrypt} 대신
        //{noop}으로 한다.
        //이 부부은 아마 admin이 회원가입하는 기능을 추가하는 과정에서도 중요한 영향을 끼칠 것이다.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }*/


    /*
    * 위의 @Bean PasswordEncoder 코드와 아래의 configureGlobal(){...} 코드에서 중대한 오류가 발생하고 있다.
    * configureGlobal 메서드에서 파라미터로 PasswordEncoder를 요구하고 있기 때문에
    * PasswordEncoder를 주입받기 위해 위에 있는 @Bean PasswordEncoder를 보게 된다.
    * 그렇데 하필 그게 같은 SecurityConfig.java 클래스 내부에 있어서 SecurityConfig.java가 다시 SecurityConfig.java를
    * 참조하는 circular dependency 상황이 발생하고 있는 것이다.
    * 이를 해결하기 위해서는 PasswordEncoder 빈을 SecurityConfig.java 외부에서 만들고 주입해야 한다.
    * 따라서 config 패키지 내부에 ForPasswordEncoder 클래스를 만들고, 그 안에 @Bean이 붙은
    * static 메서드를 정의한 후 그 메서드는 바로 위의 PasswordEncoder 메서드에서 언급했었던
    * return PasswordEncoderFactories.createDelegatingPasswordEncoder(); 구문을 포함하게 했다.
    * 그 후, 바로 아래에서 해당 메서드를 활용하여 인스턴스를 만들어주었고, 그 인스턴스를 아래의 메서드에서 활용하게
    * 하였다.
    * 이렇게 해 줌으로써 circular dependency 문제를 해결할 수 있었다.
    * */

    @Autowired
    PasswordEncoder injectedPasswordEncoder = ForPasswordEncoder.injectPasswordEncoder();



    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth,
            PasswordEncoder injectedPasswordEncoder,
            AdminService adminService
    ) throws Exception
    {
        auth.userDetailsService(adminService).passwordEncoder(injectedPasswordEncoder);
    }//fund



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/events/**", "/places/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .permitAll()
                    .loginPage("/login")
                    .defaultSuccessUrl("/admin/places")
                .and()
                .logout()
                    .permitAll()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");
    }//func

}//end of class




















