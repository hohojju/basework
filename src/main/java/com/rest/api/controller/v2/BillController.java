package com.rest.api.controller.v2;


import com.rest.api.advice.exception.CUserNotFoundException;
import com.rest.api.entity.Bill;
import com.rest.api.entity.CardCom;
import com.rest.api.entity.User;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repo.BillJpaRepo;
import com.rest.api.repo.CardJpaRepo;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.dom4j.util.StringUtils;
import org.springframework.web.bind.annotation.*;


//http://localhost:8080/swagger-ui.html#!/1.BillController/saveUsingPOST
@Api(tags={"1.BillController"})  //swagger:BillController 대표하는 최상단 타이틀 영역에 표시될 값을 셋팅
@RequiredArgsConstructor
//class 상단에 선언하면 class내부에 final로 선언된 객체에 대해서
//Contructor Injection을 수행
//해당 어노테이션을 사용하지 않고 선언된 객체에 @Autowired를 사용해도 됨
// http://localhost:8080/v2/bill 실행, 최초는 []return
@RestController // 결과값을 JSON으로 출력합니다.
@RequestMapping(value = "/v2") //api resource를 버전별로 관리 모든 리소스 주소에 적용되도록 처리
public class BillController {
    private final BillJpaRepo billJpaRepo;
    private final CardJpaRepo cardJpaRepo;
    private final ResponseService responseService; // 결과를 처리할 Service



    // 조회
    @ApiOperation(value="결제 조회", notes="결제 조회 입력한다.") //swagger:각각의 resuorce에 제목과 설명
    @PostMapping(value = "/find")
    public SingleResult<Bill> find(@ApiParam(value="관리번호",  required = true)@RequestParam long proc_id) {
        return responseService.getSingleResult(billJpaRepo.findById(proc_id).orElseThrow(CUserNotFoundException::new));

    }

    // 결제
    @ApiOperation(value="고객 결제 요청", notes="결제 요청 보냅니다.") //swagger:각각의 resuorce에 제목과 설명
    @PostMapping(value = "/bill")
    public SingleResult<Bill> payment(@ApiParam(value="카드번호", required = true)@RequestParam String card_info,
                                      @ApiParam(value="유효기간", required = true)@RequestParam String card_mnth,
                                      @ApiParam(value="cvc"   , required = true)@RequestParam long card_cvc,
                                      @ApiParam(value="할부개월", required = true)@RequestParam long bill_mnth,
                                      @ApiParam(value="결제금액", required = true)@RequestParam long bill_amt
                        )  {
        int n =CheckValue(bill_amt);
        if(n==-1) {
            throw new CUserNotFoundException("결제금액이하");
        };
        Bill bill = Bill.builder()
                .card_info(card_info)
                .card_mnth(card_mnth)
                .card_cvc(card_cvc)
                .proc_dvsn("PAYMENT")
                .bill_mnth(bill_mnth)
                .bill_amt(bill_amt)
                .bill_tax(0)
                .build(); //  .build();
        Bill saved = billJpaRepo.save(bill);
        makeSentence(saved);
        return responseService.getSingleResult(saved);
    }
    //취소
    @ApiOperation(value="고객 취소 요청", notes="취소 요청 보냅니다.") //swagger:각각의 resuorce에 제목과 설명
    @PostMapping(value = "/cncl")
    public SingleResult<Bill> cancle(@ApiParam(value="원결제번호", required = true)@RequestParam String orig_bill_id,
                         @ApiParam(value="결제금액"  , required = true)@RequestParam int bill_amt) {
        Bill bill = Bill.builder()
                .proc_dvsn("CANCLE")
                .bill_amt(bill_amt)
                .build(); //  .build();
        Bill saved = billJpaRepo.save(bill);
        makeSentence(saved);
        return responseService.getSingleResult(saved);

    }
    //값검증
    public int CheckValue(long bill_amt) {
        if(bill_amt<1000) {  return -1; }
        return 0;
    }
    
    //스트링 조작자1
    public String makeS(int type, int len, String str) {
        if(str == null) {
          str="";
        }
        StringBuilder rtn =new StringBuilder(str);
        int nPadCnt = len-str.length();
        int i=0;
        for(i=0; i<nPadCnt; i++ ) {
            rtn.append("_");
        }
        return rtn.toString();
    }
    //스트링 조작자2
    public String makeS(int type, int len, int str) {
        //0: (0), 1:(L), 2:좌측정렬
        StringBuilder rtn =new StringBuilder(String.valueOf(str));
        int nPadCnt = len-String.valueOf(str).length();
        int i=0;
        for(i=0; i<nPadCnt; i++ ) {
            if(type==2) {
                rtn.append("_");
            } else {
                rtn.insert(0, type == 0 ? "0" : "_");
            }
            System.out.println("1데이터삽입:"+rtn.toString()+"/");
        }
        return rtn.toString();
    }
    //스트링 조작자3
    public String makeS(int type, int len, long str) {
        //0: (0), 1:(L), 2:좌측정렬
        StringBuilder rtn =new StringBuilder(String.valueOf(str));
        int nPadCnt = len-String.valueOf(str).length();
        int i=0;
        for(i=0; i<nPadCnt; i++ ) {
            if(type==2) {
                rtn.append("_");
            } else {
                rtn.insert(0, type == 0 ? "0" : "_");
            }
            System.out.println("1데이터삽입:"+rtn.toString()+"/");
        }
        return rtn.toString();
    }
    // 전문 생성기& 전송(db)
    public String makeSentence(Bill body) {
        String cryptString = Crypt(body.getCard_info(),body.getCard_mnth(),String.valueOf(body.getCard_cvc()));
        String pass =
                /*01*/   makeS(1, 4,480)
                /*02*/ + makeS(1,10,body.getProc_dvsn())
                /*03*/ + makeS(1,20,body.getProc_id())

                /*01*/ + makeS(1, 20,body.getCard_info())
                /*02*/ + makeS(1,  2,body.getBill_mnth())
                /*03*/ + makeS(1,  4,body.getCard_mnth())
                /*04*/ + makeS(1,  3,body.getCard_cvc())
                /*04*/ + makeS(1, 10,body.getBill_amt())
                /*05*/ + makeS(1, 10,body.getBill_tax())
                /*06*/ + makeS(1, 20,body.getOrig_proc_id())
                /*07*/ + makeS(1,300,cryptString)
                /*08*/ + makeS(1, 47,"")
                ;
        System.out.println("전문출력:"+pass+"/");

        CardCom card = CardCom.builder()
                .full_text(pass)
                .build(); //  .build();
        CardCom saved = cardJpaRepo.save(card);

        return pass;
    }

   //암호화 미구연
    public String Crypt(String data1, String data2, String data3) {
        String tmp = data1 +"-" + data2 + "-" + data3;
        //암호화 추가
        return data1 +"-" + data2 + "-" + data3;
    }
}

