package com.rest.api.advice;

import com.rest.api.advice.exception.CUserNotFoundException;
import com.rest.api.model.response.CommonResult;
import com.rest.api.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice //Spring에서는 이와 같은 처리를 위한 annotation을 제공, JSON형태의 결과 반환
                      //@ControllerAdvice
                      //annotation에 추가로 패키지를 적용할 경우 특정 패키지 하위에만 적용되게 설정 가능
                      //@RestControllerAdvice(basePackages="com.rest.api"
public class ExceptionAdvice {
    private final ResponseService responseService;

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
//        return responseService.getFailResult();
//    }

    /*
    * @ExceptionHandler:: Exception이 발생하면 해당 Handler로 처리하겠다는 @
    * ()안에는 어떤 Exception 발생시 handler를 적용할 것인지 Exception Class를 인자로 넣음
    * Exception.class는 최상위 예외객체, 다른 ExceptionHandler에서 걸리지 않으면 최종으로 정리됨
    * =defaultException
    * */
    @ExceptionHandler(CUserNotFoundException.class)
    /*
    *@ResponseStatus
    * 해당 Exception이 발생하면 Response에 출력되는 HttpStatus Code가 500으로 내려가도록 설정합니다.
    *참고로 성공 시엔 HttpStatus code가 200으로 내려갑니다.
    *실습에서 HttpStatus Code의 역할은 성공이냐(200) 아니냐 정도의 의미만 있고
    *실제 사용하는 성공 실패 여부는 json으로 출력되는 정보를 이용합니다.
    *  */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult();
        //Exception 발생 시 이미 만들어둔 CommonResult의 실패결과를 json형태로 출력
        //HttpStatus Code이외 추가로 api성공 실패를 다시 세팅하는 이유는 상황에 따라 다양한 메시지를 전달하기 위해서
        //HttpStatus Code가 이미 정형화된 스펙이기 땜ㄴ에 상세한 예외메시지를 전달하는데 한계가 있음
    }
}


