package hello.itemservice.web.validation.form;

import hello.itemservice.domain.item.UpdateCheck;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemUpdateForm {
    @NotNull(groups = UpdateCheck.class)//수정 시에만 검증
    private Long id;
    @NotBlank
    private String itemName;
    @NotNull
    @Range(min = 1000,max = 1000000)
    private Integer price;
    //수정에는 자유롭게 변경
    private Integer quantity;
}
