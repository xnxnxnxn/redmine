package com.xnjava.controller;

import com.xnjava.redmine.Crawler;
import com.xnjava.redmine.Info;
import com.xnjava.redmine.RedmineIssue;
import com.xnjava.redmine.RedmineProject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xnjava.redmine.Crawler.getProject;
import static com.xnjava.redmine.Crawler.getRedmineByDay;

/**
 * @author xinn
 * @date 2020/5/23 0023 下午 4:49
 */
@Controller
@RequestMapping(value = "/xn")
public class RedmineController {

    @RequestMapping("/info")
    public String info(ModelMap modelMap) {
        // return模板文件的名称，对应src/main/resources/templates/redmine.html
        modelMap.addAttribute("info",new Info());
        return "info";
    }

    @PostMapping("/info")
    public String greetingSubmit(ModelMap modelMap,@ModelAttribute Info info) {
        List<RedmineProject> work = Crawler.getWork(info);
        //向模板中添加属性
        modelMap.put("hello","helloweb2");
        modelMap.put("projectList",work);
        return "redmine";
    }

}
