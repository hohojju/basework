package com.rest.api.controller.v1;

import com.rest.api.advice.exception.CUserNotFoundException;
import com.rest.api.entity.User;
import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.ListResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags={"1.User"})  //swagger:UserController를 대표하는 최상단 타이틀 영역에 표시될 값을 셋팅
@RequiredArgsConstructor
//class 상단에 선언하면 class내부에 final로 선언된 객체에 대해서
//Contructor Injection을 수행
//해당 어노테이션을 사용하지 않고 선언된 객체에 @Autowired를 사용해도 됨
@RestController // 결과값을 JSON으로 출력합니다.
@RequestMapping(value = "/v1") //api resource를 버전별로 관리 모든 리소스 주소에 적용되도록 처리
public class UserController {
    private final UserJpaRepo userJpaRepo;

    private final ResponseService responseService; // 결과를 처리할 Service

    @ApiOperation(value="회원리스트 조회", notes="모든 회원을 조회한다.") //swagger:각각의 resuorce에 제목과 설명
    @GetMapping(value = "/user") //User테이블에 있는 데이터를 모두 읽어온다
    public ListResult<User> findAllUser(){
        //결과가 여러건인 경우 getListResult를 이용해서 결과를 출력
        return responseService.getListResult(userJpaRepo.findAll());
    }
    /*
    public List<User> findAllUser() { //데이터가 한개 이상일 수 있어 리턴타입은 List<User>선언
        return userJpaRepo.findAll(); //JPA를 사용하면 기본적으로 CRUD지원
                                      //select msrl, name, uid from user; 쿼리를 실행해줌
    }
    */
    @ApiOperation(value="개인회원 조회", notes="userId로 회원을 조회한다.") //swagger:각각의 resuorce에 제목과 설명
    @GetMapping(value = "/user/{msrl}") //User테이블에 있는 데이터를 모두 읽어온다
    public SingleResult<User> findUserById(@ApiParam(value="회원ID", required = true) @PathVariable long msrl,
                 @ApiParam(value="언어", defaultValue="ko")@RequestParam String lang)
    //throws Exception //에러 생성
    {
        //결과가 단일건인 경우 getListResult를 이용해서 결과를 출력
        return responseService.getSingleResult(userJpaRepo.findById(msrl).orElseThrow(CUserNotFoundException::new));
        //Exception::new
    }

    // 데이터를 1건 입력
    // insert문 지원
    // http://localhost:8080/v1/user 실행, 최초는 []return
    // 웹브라우저 입력으로는 GET URL만 호출 가능, 테스트를 위해 postman을 다운받아 실행
    @ApiOperation(value="회원 입력", notes="모든 회원을 입력한다.") //swagger:각각의 resuorce에 제목과 설명
    @PostMapping(value = "/user")
    //public User save(@ApiParam(value="회원ID",  required = true)@RequestParam String uid,
    //                 @ApiParam(value="회원이름", required = true)@RequestParam String name ) {
    public SingleResult<User> save(@ApiParam(value="회원ID",  required = true)@RequestParam String uid,
                                   @ApiParam(value="회원이름", required = true)@RequestParam String name ) {
        User user = User.builder()
                .uid(uid) // .uid("yumi@naver.com")
                .name(name) // .name("유미")
                .build(); //  .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
       // return userJpaRepo.save(user);
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(
            @ApiParam(value = "회원번호", required = true) @RequestParam long msrl,
            @ApiParam(value = "회원아이디", required = true) @RequestParam String uid,
            @ApiParam(value = "회원이름", required = true) @RequestParam String name) {
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();

        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete(
            @ApiParam(value = "회원번호", required = true) @PathVariable long msrl) {
        userJpaRepo.deleteById(msrl);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }
}
