package kr.co.farmstory.controller;

import kr.co.farmstory.dto.ArticleDTO;
import kr.co.farmstory.dto.PageRequestDTO;
import kr.co.farmstory.dto.PageResponseDTO;
import kr.co.farmstory.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class EventController {

    private final ArticleService articleService;

    // 글 목록 조회
    @GetMapping("/event/list")
    public String event(Model model, String cate, PageRequestDTO pageRequestDTO){
        PageResponseDTO pageResponseDTO = null;

        if(pageRequestDTO.getKeyword() == null) {
            // 일반 글 목록 조회
            pageResponseDTO = articleService.selectArticles(pageRequestDTO);
        }else {
            // 검색 글 목록 조회
            pageResponseDTO = articleService.searchArticles(pageRequestDTO);
        }
        log.info("pageResponseDTO : " + pageResponseDTO);

        model.addAttribute(pageResponseDTO);
        return "/event/list";
    }
    // 글 상세 보기
    @GetMapping("/event/view")
    public String eventView(Model model, String cate, int ano, PageRequestDTO pageRequestDTO){
        log.info("eventView...1 : " + ano);
        // 글 조회
        ArticleDTO article = articleService.selectArticleAndNick(ano);

        log.info("eventView...2 : " + article.toString());

        // 페이지 정보 build
        PageResponseDTO pageResponseDTO = PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .build();

        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("article", article);

        return "/event/view";
    }
    // 글 쓰기
    @GetMapping("/event/write")
    public String write(Model model, @ModelAttribute("cate") String cate, PageRequestDTO pageRequestDTO){

        PageResponseDTO pageResponseDTO = PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .build();

        model.addAttribute(pageResponseDTO);
        return "/event/write";
    }

    @PostMapping("/event/write")
    public String write(ArticleDTO articleDTO){

        articleService.insertArticle(articleDTO);
        return "redirect:/event/list?cate="+articleDTO.getCate();
    }
    // 글 수정 - 글 상세 정보
    @GetMapping("/event/modify")
    public String modify(Model model, int ano, PageRequestDTO pageRequestDTO) {

        log.info("글 수정 글 조회 ...1 : " + ano);
        // 글 조회
        ArticleDTO article = articleService.selectArticleAndNickForModify(ano);

        log.info("글 수정 글 조회 ...2 : " + article.toString());
        // 페이지 정보 build
        PageResponseDTO pageResponseDTO = PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .build();

        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("article", article);

        return "/event/modify";
    }
    // 글 수정
    @PostMapping("/event/modify")
    public String modify(ArticleDTO articleDTO, PageRequestDTO pageRequestDTO) {

        log.info("글 수정 Cont : " + articleDTO.toString());
        articleService.updateArticle(articleDTO);
        // 페이지 정보 build
        PageResponseDTO pageResponseDTO = PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .build();

        // view로 리턴
        return "redirect:/event/view?ano=" + articleDTO.getAno() + "&cate=" + articleDTO.getCate() + "&pg=" + pageRequestDTO.getPg();
    }
    // 글 삭제
    @GetMapping("/event/delete")
    public String deleteArticle(Model model, int ano, PageRequestDTO pageRequestDTO){
        log.info("글 삭제 Cont : " + ano);
        articleService.deleteArticle(ano);
        // 페이지 정보 build
        PageResponseDTO pageResponseDTO = PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .build();
        log.info("글 삭제 Cont 2 : ");
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("ano", ano);

        return "/event/list";
    }
}