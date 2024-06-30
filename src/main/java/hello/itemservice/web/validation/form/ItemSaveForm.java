package hello.itemservice.web.validation.form;

import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemSaveForm {
    @NotBlank
    private String itemName;
    @NotNull
    @Range(min = 1000,max = 1000000)
    private Integer price;
    @NotNull(message = "null X")//기본 에러 메시지 변경
    @Max(value = 9999)//저장에만 검증
    private Integer quantity;
}
