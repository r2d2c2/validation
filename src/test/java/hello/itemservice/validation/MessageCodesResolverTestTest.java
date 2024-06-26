package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MessageCodesResolverTestTest {
    MessageCodesResolver codesResolver  =new DefaultMessageCodesResolver();
    @Test
    public void 메시지_오브젝트(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        assertThat(messageCodes).containsExactly("required.item","required");
    }
    @Test
    public void 메시지_코드보기(){//bindingResult.rejectValue 코드가 메시지 찾는 방법
        String[] messageCodes = codesResolver.resolveMessageCodes("required",
                "item", "itemName", String.class);
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required");
        //보면 우선 순위를 볼수 있는데 특이 하게 중간에 String머시기가 있다
        //"required.java.lang.String",
        //                "required"
        //혹시 2개가 있으면 required.java.lang.String의 내용이 나간다

    }
}