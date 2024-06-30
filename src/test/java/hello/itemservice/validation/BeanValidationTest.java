package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {
    @Test
    public void 벨리데이션테스트(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item=new Item();
        item.setItemName(" ");
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> validate = validator.validate(item);
        for (ConstraintViolation<Item> v : validate) {
            System.out.println("v = " + v);
            //v = ConstraintViolationImpl{interpolatedMessage='공백일 수 없습니다', propertyPath=itemName, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{javax.validation.constraints.NotBlank.message}'}
            //v.getMessage() = 공백일 수 없습니다
            System.out.println("v.getMessage() = " + v.getMessage());
            //여기서 나오는 오류 메시지는 기본제공해주는 메시지로 무료 한글도 가능
        }
    }
}
