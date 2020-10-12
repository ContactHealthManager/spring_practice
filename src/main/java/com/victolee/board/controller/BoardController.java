package com.victolee.board.controller;

import com.victolee.board.dto.BoardDto;
import com.victolee.board.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor


public class BoardController {
    private BoardService boardService;

    /* 메인 화면 */
    @GetMapping("/")
    public String list() {

        return "/index";
    }

    /* 게시글 목록 */
    @GetMapping("/managerlist")
    public String list(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
        List<BoardDto> boardList = boardService.getBoardlist(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "/managerlist";
    }



    /* 게시글 상세 */
    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        BoardDto boardDTO = boardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/detail";
    }


    /* 게시글 쓰기 */
    @GetMapping("/post")
    public String write() {
        return "board/write";
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String write(BoardDto boardDto,Principal principal) {
        String userid = principal.getName();
        boardDto.setWriter(userid);
        boardService.savePost(boardDto);
        return "redirect:/managerlist";
    }



    /* 게시글 수정 */
    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        BoardDto boardDTO = boardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/update";
    }
    /* 수정 폼에서 수정 완료*/
    @PutMapping("/post/edit/{no}")
    public String update(BoardDto boardDTO) {
        boardService.savePost(boardDTO);

        return "redirect:/managerlist";
    }

    /* 게시글 삭제 */
    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        boardService.deletePost(no);

        return "redirect:/managerlist";
    }
    /* 게시글 검색 */
    @GetMapping("/board/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "/managerlist";
    }



}
