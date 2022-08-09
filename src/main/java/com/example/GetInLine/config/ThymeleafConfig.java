package com.example.GetInLine.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

/*
* 디커플드 로직을 추가하기 위한 출발점은 ThymeleafAutoConfiguration.java클래스 파일 내부의
*       @Bean
		SpringResourceTemplateResolver defaultTemplateResolver() {
			SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
			resolver.setApplicationContext(this.applicationContext);
			resolver.setPrefix(this.properties.getPrefix());
			resolver.setSuffix(this.properties.getSuffix());
			resolver.setTemplateMode(this.properties.getMode());
			if (this.properties.getEncoding() != null) {
				resolver.setCharacterEncoding(this.properties.getEncoding().name());
			}
			resolver.setCacheable(this.properties.isCache());
			Integer order = this.properties.getTemplateResolverOrder();
			if (order != null) {
				resolver.setOrder(order);
			}
			resolver.setCheckExistence(this.properties.isCheckTemplate());
			return resolver;
		}
		* 이라는 Bean이다.
		* 이 빈을 자세히 들여다보면, SppringResourceTemplateResolver 타입의 resolver라는
		* 객체를 중심으로 template resolver의 속성들이 정의 된 후에 다시 리턴 되는 것을 확인할 수 있다.
		* 따라서, 내가 원하는 디커플드 로직을 활성화 시키기 위해서는,
		* SpringResourceTemplateResolver 타입의 객체를 나도 똑같이 만들어서 디커플드 로직을 true로 만든 후,
		* 그대로 SpringResourceTemplateResolver 타입의 객체를 리턴하면 된다.
		* 따라서 현재의 ThymeleafConfig.java 클래스 파일 내부를 다음과 같이 작성해주면,
		* 자바 코드만으로 타임리프의 디커플드 로직을 활성화 시킬 수 있다.
@Configuration
public class ThymeleafConfig {

    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver
    ){
        defaultTemplateResolver.setUseDecoupledLogic(true);
        return defaultTemplateResolver;
    }

}
*
* 물론 이것 만으로도 디커플드 로직을 활성화 할 수 있지만, 여기서 멈출 경우
* 다른 개발자가 application.properties파일만을 봤을 때, 디커플드 로직이 활성화 돼 있는지 알 수가 없다.
* 따라서, application.properties 파일 내부에서 바로 디커플드 로직을 활성화 시킬 수 있도록 설정을 추가해 주는 것이
* 필요하다고 할 수 있다.
*
* 앞서 디커플드 로직을 추가했던 과정과 비슷하게, 이번 로직을 추가하기 위한 출발점은 ThymeleafProperties.java라는
* 클래스 파일이다.
* 해당 클래스 파일을 ThymeleafConfig.java 클래스 파일 내부에 nested된 형식으로 static 클래스로 만들어두고,
* 이것을 @ConfigurationProperties("spring.thymeleaf3")라고 이름을 지어서 어노테이션을 붙여준다.
*
* @ConfigurationProperties("spring.thymeleaf3") 이 어노테이션을 처음 붙여줬을 때는 스프링프레임워크에서
* 이러한 ConfigurationProperty가 존재하는지를 알수가 없기 때문에 빨간줄이 그어지면서 에러 표시가 뜬다.
* 이것을 해결하기 위해서는 스프링 프레임워크의 main 메서드가 위치하는 GetInLineApplication.java파일 내부가
* 아래와 같을 때, @ConfigurationPropertiesScan 을 아래와 같이 추가해줘야 한다.
* 그러면 비로소 스프링프레임워크에서 내가 새로 추가한 ConfigurationProperty를 감지해서 추가해 주고 오류가 해결된다.
*
@ConfigurationPropertiesScan
@SpringBootApplication
public class GetInLineApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetInLineApplication.class, args);
	}

}
* 그 후, 아래와 같이 Thymeleaf3Properties에 적절한 바인딩, 생성자, 롬복을 추가해주고, ThymeleafConfig.java
* 클래스 파일 내부의 thymeleafTemplateResolver()라는 메서드에 인자로서 전달하고 사용해주면 된다.
*
* 그후, build.gradle에 가서 dependencies{...}에
* annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'를 추가해준다.
*
* 그 다음 gradle을 clean 해주고 다시 build 해준 다음에, application.properties파일에 가서
* spring.thymeleaf3.decoupled-logic=true를 입력하여 최종적으로 De-coupled 로직을 활성화 시킨다.
*
* 그 다음 resources 폴더의 뷰 템플릿들을 모아 놓은 곳에 가서, 예를 들어서 event 폴더 내부에서 디커플드로직을
* 작동사키고 싶다면, 해당 폴더 내부에 index.th.xml파일을 추가하고, 같은 폴더 내부의 html파일들을 HTML5형식에
* 맞게 정리해주면 최종적으로 디커플드 로직을 활용할 수 있다.
* index.th.xml 파일은 event폴더 내의 index.html파일을 위한 타임리프 로직을 설명하겠다는 뜻이다.
* 따라서 같은 폴더 내부의 detail.html을 위한 타임리프 로직은 detail.th.xml로 따로 만들어줘야 한다.
*
* 디커플드 로직의 장점은 타임리프 로직과 html코드를 완전히 분리시켜서 프런트엔드 엔지니어가 봤을 때 쉽게 이해할 수
* 있다는 점이다.
* 그리고 이렇게 할 경우, TailWinde 같은 CSS 프레임 워크를 이용한 디자인 작업에서도 매우 유용하다.
* 왜냐하면, 이 프레임워크는 HTML 문서의 태그들 안에 class = "..."라는 클래스들을 명시하는 것으로써 사용되는데,
* HTML 태그들이 타임리프 로직과 함께 작성돼 있으면 한 개의 태그 내에서 담아야 하는 내용이 너무 많아져서 가독성이 떨어지고,
* 그 결과 유지보수도 어렵게 되기 때문이다.
* */

@Configuration
public class ThymeleafConfig {

    @Getter
    @RequiredArgsConstructor
    @ConstructorBinding
    @ConfigurationProperties("spring.thymeleaf3")
    public static class Thymeleaf3Properties{
        /**
         * Thymeleaf에서 De-coupled 기능의 활성화
         */
        private final boolean decoupledLogic;
    }

    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver,
            Thymeleaf3Properties thymeleaf3Properties
    ){
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());
        return defaultTemplateResolver;
    }

}//end of class
/*

헤로쿠에 배포하자 이런 오류가 발생했다.

2022-08-09T07:25:08.836967+00:00 app[web.1]: ***************************
2022-08-09T07:25:08.836967+00:00 app[web.1]: APPLICATION FAILED TO START
2022-08-09T07:25:08.836967+00:00 app[web.1]: ***************************
2022-08-09T07:25:08.836967+00:00 app[web.1]:
2022-08-09T07:25:08.836968+00:00 app[web.1]: Description:
2022-08-09T07:25:08.836968+00:00 app[web.1]:
2022-08-09T07:25:08.836969+00:00 app[web.1]: Parameter 1 of method thymeleafTemplateResolver in com.example.GetInLine.config.ThymeleafConfig required a bean of type 'com.example.GetInLine.config.ThymeleafConfig$Thymeleaf3Properties' that could not be found.
2022-08-09T07:25:08.836969+00:00 app[web.1]:
2022-08-09T07:25:08.836969+00:00 app[web.1]:
2022-08-09T07:25:08.836970+00:00 app[web.1]: Action:
2022-08-09T07:25:08.836970+00:00 app[web.1]:
2022-08-09T07:25:08.836971+00:00 app[web.1]: Consider defining a bean of type 'com.example.GetInLine.config.ThymeleafConfig$Thymeleaf3Properties' in your configuration.

* */











