package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "/validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "/validation/v4/addForm";
    }


    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //ModelAttribute는 생략시 다음 클래스의 이름을 소문자로 시작하여 저장한다

        if(form.getPrice()!=null && form.getQuantity()!=null){//오브젝트 에러 처리
            int resultP=form.getPrice()*form.getQuantity();
            if(resultP<10000){
                //ObjectError 는 키 값이 없어도 된다
                //bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000,resultP},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultP},null);
            }
        }

        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v4/addForm";//bindingResult 자동으로 view 넘어간다
        }

        //추가
        Item item=new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        //변경 (저장은 item이여야 한다)
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }
//    @PostMapping("/add")//@Validated 츠기
    public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //몌게변수 순서가 중요하다

        if(item.getPrice()!=null && item.getQuantity()!=null){//오브젝트 에러 처리
            int resultP=item.getPrice()*item.getQuantity();
            if(resultP<10000){
                //ObjectError 는 키 값이 없어도 된다
                //bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000,resultP},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultP},null);
            }
        }

        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v4/addForm";//bindingResult 자동으로 view 넘어간다
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")//검증 추가
    public String edit(@PathVariable Long itemId, @Validated  @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {
        if(form.getPrice()!=null && form.getQuantity()!=null){//오브젝트 에러 처리
            int resultP=form.getPrice()*form.getQuantity();
            if(resultP<10000){
                //ObjectError 는 키 값이 없어도 된다
                //bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000,resultP},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultP},null);
            }
        }
        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v4/editForm";//bindingResult 자동으로 view 넘어간다
        }
        //추가
        Item item=new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        //변경 (저장은 item이여야 한다)
        itemRepository.update(itemId,item);
        return "redirect:/validation/v4/items/{itemId}";
    }
//    @PostMapping("/{itemId}/edit")//검증 추가
    public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class)  @ModelAttribute Item item, BindingResult bindingResult) {
        if(item.getPrice()!=null && item.getQuantity()!=null){//오브젝트 에러 처리
            int resultP=item.getPrice()*item.getQuantity();
            if(resultP<10000){
                //ObjectError 는 키 값이 없어도 된다
                //bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000,resultP},null));
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultP},null);
            }
        }
        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v4/editForm";//bindingResult 자동으로 view 넘어간다
        }
        itemRepository.update(itemId,item);
        return "redirect:/validation/v4/items/{itemId}";
    }
}

