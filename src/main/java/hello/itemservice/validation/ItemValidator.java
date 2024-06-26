package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component //자동 빈 주입
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Item.class.isAssignableFrom(aClass);//자식 클레스 까지 검증한다
        // item==cClass
        // item.작식=cClass
    }

    @Override
    public void validate(Object o, Errors bindingResult) {
        Item item=(Item) o;
        if(item.getPrice()==null||item.getPrice()<1000||item.getPrice()>1000000){
            //bindingResult.addError(new FieldError("item","price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000},"기본값"));
            bindingResult.rejectValue("price","range",new Object[]{1000,1000000},null);
        }
        if(item.getQuantity()==null||item.getQuantity()>=9999){
//            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{"9999"},"수량은 최대 9,999까지 입니다"));
            bindingResult.rejectValue("quantity","max",new Object[]{9999},null);
        }

        if(item.getPrice()!=null && item.getQuantity()!=null){
            int resultP=item.getPrice()*item.getQuantity();
            if(resultP<10000){
                //ObjectError 는 키 값이 없어도 된다
                //bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000,resultP},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultP},null);
            }
        }
    }
}
