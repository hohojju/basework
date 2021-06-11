package com.rest.api.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping(value = "/helloworld/string")
    //@GetMapping 해당주소의 Resource사용시 Get방식호출
    //http:localhost:8080/helloworld/string

    @ResponseBody
    //결과를 응답에 그대로 출력한다, 미지정시 return에 지정된 helloworld/string 이름으로 된 파을을 찾아 화면 출력
    public String helloworldString() {
        return "helloworld/string";
    }

    @GetMapping(value = "/helloworld/json")
    @ResponseBody
    public Hello helloworldJson() {

        Hello hello = new Hello();
        hello.message = "helloworld/json";
        return hello;

    }


    @GetMapping(value = "/helloworld/page")
    public String helloworld() {
        return "fffffff";
    }



    @Setter

    @Getter

    public static class Hello {

        private String message;

    }

}

