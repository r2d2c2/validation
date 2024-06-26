package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.validation.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator; //v5

    //v6
    @InitBinder //여기 클레스가 호출되면 항상 초기화
    public void init(WebDataBinder dataBinder){
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "/validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "/validation/v2/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, RedirectAttributes redirectAttributes,Model model
            ,BindingResult bindingResult) {//BindingResult 추가
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //글자 없음
            bindingResult.addError(new FieldError("item","itemName","상품 이름은 필수 입니다."));
        }
        if(item.getPrice()==null||item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item","price","가격은 1,000~1,000,000 까지 허용"));
        }
        if(item.getQuantity()==null||item.getQuantity()>=9999){
            bindingResult.addError(new FieldError("item","quantity","수량은 최대 9,999까지 입니다"));
        }
        if(item.getPrice()!=null && item.getQuantity()!=null){
            int resultP=item.getPrice()*item.getQuantity();
            if(resultP<10000){
                //ObjectError 는 키 값이 없어도 된다
                bindingResult.addError(new ObjectError("item","가격*수량의 합은 10,000원 이상이야 됩니다. 현제값="+resultP));
            }
        }
        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v2/addForm";//bindingResult 자동으로 view 넘어간다
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, RedirectAttributes redirectAttributes,Model model
            ,BindingResult bindingResult) {//BindingResult 추가
        //프로퍼티 없이 기본 값에 에러문자를 입력하여 사용
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //글자 없음
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,null,null,"상품 이름은 필수 입니다."));
            // (객체의이름,필드이름,에러 발생시 출력할 문자열)
            // (객체의이름,필드이름,에러시 대체할 값,필터사용?,코드사용?,아그먼트사용?, 에러 발생시 출력할 문자열)
            //에러가 나도 사용자가 입력한 문자를 계속 확인 가능
        }
        if(item.getPrice()==null||item.getPrice()<1000||item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,null,null,"가격은 1,000~1,000,000 까지 허용"));
        }
        if(item.getQuantity()==null||item.getQuantity()>=9999){
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,null,null,"수량은 최대 9,999까지 입니다"));
        }
        if(item.getPrice()!=null && item.getQuantity()!=null){
            int resultP=item.getPrice()*item.getQuantity();
            if(resultP<10000){
                //ObjectError 는 키 값이 없어도 된다
                bindingResult.addError(new ObjectError("item",null,null,"가격*수량의 합은 10,000원 이상이야 됩니다. 현제값="+resultP));
                //바인딩 할것이 없어 코드,아그먼트만 널추가
            }
        }
        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v2/addForm";//bindingResult 자동으로 view 넘어간다
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, RedirectAttributes redirectAttributes,Model model
            ,BindingResult bindingResult) {//BindingResult 추가
        //프로퍼티에서 에러 문자를 당겨와서 사용
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //글자 없음
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,new String[]{"required.item.itemName"},null,""));
            // (객체의이름,필드이름,에러 발생시 출력할 문자열)
            // (객체의이름,필드이름,에러시 대체할 값,필터사용?,코드사용?,아그먼트사용?, 에러 발생시 출력할 문자열)
            //에러가 나도 사용자가 입력한 문자를 계속 확인 가능(배열 이다 보니 에러 문자를 여러변수에 저장하여 가지고 올수 있다 구지?)
        }
        if(item.getPrice()==null||item.getPrice()<1000||item.getPrice()>1000000){
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000},"기본값"));
        }
        if(item.getQuantity()==null||item.getQuantity()>=9999){
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{"9999"},"수량은 최대 9,999까지 입니다"));
        }
        if(item.getPrice()!=null && item.getQuantity()!=null){
            int resultP=item.getPrice()*item.getQuantity();
            if(resultP<10000){
                //ObjectError 는 키 값이 없어도 된다
                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000,resultP},null));
                //바인딩 할것이 없어 코드,아그먼트만 널추가
                //v3 기본 초기 에러 값을 없에고 파라미터 코드와 해당 아그먼트 추가
            }
        }
        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v2/addForm";//bindingResult 자동으로 view 넘어간다
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, RedirectAttributes redirectAttributes,Model model
            ,BindingResult bindingResult) {//BindingResult 추가
        //프로퍼티에서 에러 문자를 당겨와서 사용
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //글자 없음
            //bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,new String[]{"required.item.itemName"},null,""));
            bindingResult.rejectValue("itemName","required");
            // String[] 처럼 우선 순위가 있는데 required.item.itemName 있으면 사용하고 없으면
            //required를 찾아서 사용한다
            //이코드는 알아서 required만드로 아라서 찾아 간다
            //글러벌 에러 문자와 상세 에러 문자를 만들어서 사용 가능 하다
        }
        //글자 없음 간소화
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult,"itemName","required");
        //동일 기능

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
        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v2/addForm";//bindingResult 자동으로 view 넘어간다
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, RedirectAttributes redirectAttributes,Model model
            ,BindingResult bindingResult) {//BindingResult 추가
        //프로퍼티에서 에러 문자를 당겨와서 사용

        //글자 없음 간소화
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult,"itemName","required");
        //동일 기능

        itemValidator.validate(item,bindingResult);

        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v2/addForm";//bindingResult 자동으로 view 넘어간다
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")//@Validated 츠기
    public String addItemV6(@Validated @ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model
            , BindingResult bindingResult) {
        //안됨

        if(bindingResult.hasErrors()){//에러가 있으면
            //입력 폼으로
            log.info("error={}",bindingResult);
            return "/validation/v2/addForm";//bindingResult 자동으로 view 넘어간다
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

