package jpabook.jpashop.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.MemberForm;
import jpabook.jpashop.dto.MemberLogin;
import jpabook.jpashop.dto.MemberSearch;
import jpabook.jpashop.dto.ResponseDto;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm" , new MemberForm());
        return "members/createMemberForm";
    }

    @GetMapping("/members/login")
    public String loginForm(@RequestParam(value = "error",required = false)String error,
                            @RequestParam(value = "exception",required = false)String exception,Model model){
        model.addAttribute("loginForm",new MemberLogin());
        model.addAttribute("error",error);
        model.addAttribute("exception",exception);
        return "members/login";
    }

    @GetMapping("/members/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){

        if(result.hasErrors()) return "members/createMemberForm";

        memberService.join(form);

        return "redirect:/";
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/members")
    public String list(@ModelAttribute("memberSearch") MemberSearch memberSearch, Model model) throws AccessDeniedException {
        System.out.println(memberService.searchMembers(memberSearch).get(0).getUsername());
        model.addAttribute("members",memberService.searchMembers(memberSearch));
        return "members/memberList";
    }

    @PostMapping( value="/members/search",consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> list(@RequestBody MemberSearch memberSearch){
        ResponseDto responseDto = new ResponseDto() ;
        responseDto.setData(memberService.searchMembers(memberSearch));
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @GetMapping("/members/{memberId}")
    public String one(@PathVariable("memberId") Long id , Model model){
        model.addAttribute("member",memberService.findOne(id));
        return "members/member";
    }
}
