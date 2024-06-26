package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Log4j2
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes,Model model) {
        //오류 결과를 보관
        Map<String,String > errors=new HashMap<>();
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //글자 없음
            errors.put("itemName","상품이름은 필수 입니다.");
            log.info("errors={}",errors);
        }
        if(item.getPrice()==null||item.getPrice()>1000000){
            errors.put("price","가격은 1,000~1,000,000 까지 허용");
            log.info("errors={}",errors);
        }
        if(item.getQuantity()==null||item.getQuantity()>=9999){
            errors.put("quantity","수량은 최대 9,999까지 입니다");
            log.info("errors={}",errors);
        }
        if(item.getPrice()!=null && item.getQuantity()!=null){
            int resultP=item.getPrice()*item.getQuantity();
            if(resultP<10000){
                errors.put("globalError","가격*수량의 합은 10,000원 이상이야 됩니다. 현제값="+resultP);
            }
            log.info("errors={}",errors);
        }
        if(!errors.isEmpty()){//에러가 있으면
            //입력 폼으로
            model.addAttribute("errors",errors);
            return "validation/v1/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

