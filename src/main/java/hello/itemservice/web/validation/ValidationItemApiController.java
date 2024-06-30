package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {
    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form , BindingResult bindingResult){
        log.info("api 호출");
        if (bindingResult.hasErrors()){
            //에러가 있으면(json)
            log.info("검증 오류 발생");
            return bindingResult.getAllErrors();//json으로 오류 전송
        }
        log.info("성공 로직실행");
        return form;
    }

}
