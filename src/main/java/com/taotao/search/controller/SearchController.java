package com.taotao.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.search.bean.SearchResult;
import com.taotao.search.service.ItemSearchService;

@Controller
public class SearchController {

    @Autowired
    private ItemSearchService itemSearchService;
    
    private static final Integer ROWS = 32;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keyWord,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        ModelAndView mv = new ModelAndView("search");

        // get请求乱码解决
        try {
            keyWord = new String(keyWord.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            keyWord = "";
        }
        
        // 搜索关键字
        mv.addObject("query", keyWord);

        SearchResult result = null;
        try {
            result = this.itemSearchService.search(keyWord, page, ROWS);
        } catch (Exception e) {
            e.printStackTrace();
            result = new SearchResult(0L, null);
        }

        // 搜索结果
        mv.addObject("itemList", result.getList());

        // 页数
        mv.addObject("page", page);

        int total = result.getTotal().intValue();
        int pages = total % ROWS == 0 ? total / ROWS : total / ROWS + 1;
        // 总页数
        mv.addObject("pages", pages);
        return mv;
    }

}
