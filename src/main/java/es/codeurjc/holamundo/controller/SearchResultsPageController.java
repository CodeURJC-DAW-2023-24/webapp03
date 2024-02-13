package es.codeurjc.holamundo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchResultsPageController {

    @GetMapping("/search")
    public String loadSearchResultsPage(Model model, @RequestParam String query) {
        model.addAttribute("searchInputQuery", query);
        return "searchResultsPage";
    }
}
