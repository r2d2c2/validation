package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.*;

@Data
//@ScriptAssert(lang = "javascript",script = "_this.price * _this.quantity>=10000",message = "삼품 하나다 만원 이상 일것")
public class Item {

//    @NotNull(groups = UpdateCheck.class)//수정 시에만 검증
    private Long id;
//    @NotBlank(groups ={SaveCheck.class, UpdateCheck.class})// " " 허용 안함
    private String itemName;
//    @Max(1000000)@Min(1000)
//    @Range(min = 1000,max = 1000000,groups ={SaveCheck.class, UpdateCheck.class})
    private Integer price;
//    @NotNull(message = "null X",groups ={SaveCheck.class, UpdateCheck.class})//기본 에러 메시지 변경
//    @Max(value = 9999, groups ={SaveCheck.class})//저장에만 검증
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
