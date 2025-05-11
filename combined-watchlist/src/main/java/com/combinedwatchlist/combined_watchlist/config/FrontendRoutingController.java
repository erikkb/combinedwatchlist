package com.combinedwatchlist.combined_watchlist.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@ConditionalOnProperty(name = "app.react-mode", havingValue = "true")
public class FrontendRoutingController {

    @RequestMapping(value = { "/", "/watchlist", "/reset-password", "/privacy-policy" })
    public String index() {
        return "forward:/index.html";
    }
}
