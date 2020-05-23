package com.xnjava.redmine;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.math.MathUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author xinn
 * @date 2020/5/23 0023 下午 2:51
 */
public class Crawler {

    public static void main(String[] args) { }

    public static List<RedmineProject> getWork(Info info)  {
        String cookie = login(info);
        Map<String, List<RedmineIssue>> map = new HashMap<String, List<RedmineIssue>>();
        LocalDate startDate = LocalDate.parse(info.getStartDate());
        LocalDate endDate = LocalDate.parse(info.getEndDate());
        while(startDate.compareTo(endDate) <= 0) {
            getRedmineByDay(startDate,cookie,map);
            startDate = startDate.plusDays(1);
        }
        return getProject(map);
    }

    public static String login(Info info)  {
        String loginHtml = HttpRequest.get("https://www.rococo.net.cn:1443/redmine/login/")
                .execute().body();
        Document loginDocument = Jsoup.parse(loginHtml);
        Element token = loginDocument.select("input[name=authenticity_token]").first();
        String authenticity_token = token.attr("value");
        Map<String,Object> map = new HashMap<>();
        map.put("username",info.getUsername());
        map.put("password",info.getPassword());
        map.put("utf8","✓");
        map.put("back_url","https://www.rococo.net.cn:1443/redmine/");
        map.put("login","登录 »");
        map.put("authenticity_token",authenticity_token);
        HttpResponse execute = HttpRequest.post("https://www.rococo.net.cn:1443/redmine/login")
                .form(map)
                .execute();
        String body = execute.body();
        return execute.getCookieStr();
    }

    public static List<RedmineProject> getProject(Map<String, List<RedmineIssue>> map) {
        List<RedmineProject> redmineProjectList = new ArrayList<>();
        double projectTotal = 0;
        for (Entry<String,List<RedmineIssue>> entry : map.entrySet()) {
            List<RedmineIssue> issueList = entry.getValue();
            double total = issueList.stream().mapToDouble(RedmineIssue::getHour).sum();
            projectTotal += total;
            redmineProjectList.add(new RedmineProject(entry.getKey(),total,issueList));
        }
        for (RedmineProject project : redmineProjectList) {
            project.setRate(project.getHour() / projectTotal * 100);
        }
        redmineProjectList.sort(Comparator.comparing(RedmineProject::getHour));
        Collections.reverse(redmineProjectList);
        return redmineProjectList;
    }

    public static void getRedmineByDay(LocalDate date,String cookie,Map<String,List<RedmineIssue>> map) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        String body = HttpRequest.get("https://www.rococo.net.cn:1443/redmine/work_time/index?day="+day +"&month="+month+"&prj=false&user=1&year="+ year+"")
        .cookie(cookie)
        .execute()
        .body();
//        File file =  new File("C:\\Users\\Administrator\\Desktop\\demo.html");
//        Document document = Jsoup.parse(file,"utf-8");
        Document document = Jsoup.parse(body);
        Element table = document.getElementById("time_input_table");
        Elements trs = table.select("tr");
        String mapKey = "";
        List<RedmineIssue> list;
        //遍历该表格内的所有的<tr> <tr/> (从第二个tr开始)
        for (int i = 1; i < trs.size(); ++i) {
            // 获取一个tr
            Element tr = trs.get(i);
            // 根据样式判断是不是总项目
            String style = tr.attr("style");
            if (style.contains("background:#ddd")) {
                continue;
            }
            if (style.contains("background:#000")) {
                // 主项目
                Element hourElement = tr.select("td:nth-child(2)").first();
                if (!hourElement.text().equals("0.00")) {
                    Element select = tr.select("td:nth-child(1)").first();
                    String projectName = select.text();
                    projectName = projectName.substring(3);
                    mapKey = projectName;
                    list = new ArrayList<RedmineIssue>();
                    if (!map.containsKey(projectName)) {
                        map.put(mapKey,list);
                    }
                }
            } else {
                // 子项目
                Element input = tr.select("td:nth-child(2) input").first();
                String hour = input.attr("value");
                if (StringUtils.isNotBlank(hour) && !"0.00".equals(hour)) {
                    Element titleElement = tr.select("td:nth-child(1) div a:nth-child(2)").first();
                    final String issue = titleElement.attr("data-issue");
                    List<RedmineIssue> issueList = map.get(mapKey);
                    Optional<RedmineIssue> issueOptional = issueList.stream().filter(redmineIssueItem -> issue.equals(redmineIssueItem.getIssue())).findFirst();
                    if (issueOptional.isPresent()) {
                        // 累加时长就好
                        RedmineIssue originIssue = issueOptional.get();
                        originIssue.setHour(originIssue.getHour() + Double.parseDouble(hour));
                    } else {
                        String text = titleElement.text();
                        RedmineIssue redmineIssue = new RedmineIssue(text, Double.valueOf(hour), issue);
                        issueList.add(redmineIssue);
                    }
                }
            }
        }
    }


}