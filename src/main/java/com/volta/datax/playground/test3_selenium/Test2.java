package com.volta.datax.playground.test3_selenium;

import com.volta.datax.core.data.downloader.seleniumUtils.SeleniumUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

public class Test2 {

    public static void fn1() {
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
        // ---WebDriver设置与初始化
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("blink-settings=imagesEnabled=false");
        WebDriver driver = new ChromeDriver(chromeOptions);
        // ---打开网页
//        String url = "https://www.zhihu.com/question/68679336";
        String url = "https://www.baidu.com/s?wd=%E5%8D%81%E4%B8%89%E5%B1%8A%E5%85%A8%E5%9B%BD%E4%BA%BA%E5%A4%A7%E4%BA%94%E6%AC%A1%E4%BC%9A%E8%AE%AE%E9%97%AD%E5%B9%95&sa=fyb_n_homepage&rsv_dl=fyb_n_homepage&from=super&cl=3&tn=baidutop10&fr=top1000&rsv_idx=2&hisfilter=1";
        driver.get(url);

        // ---执行脚本
        String jsCode = "" +
                "var lastScrollHeight = 0\n" +
                "setInterval(function(){\n" +
                "    window.scrollTo(0,document.body.scrollHeight);\n" +
                "    \n" +
                "    if(lastScrollHeight != document.body.scrollHeight){\n" +
                "        lastScrollHeight = document.body.scrollHeight\n" +
                "    }else{\n" +
                "        console.log(\"到底了\")\n" +
                "    }\n" +
                "}, 1000)";
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(jsCode);
    }

    public static void fn2() {
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");

        // ---WebDriver设置与初始化
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("blink-settings=imagesEnabled=false");
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");// 不搞的话，知乎加载不完全
        WebDriver driver = new ChromeDriver(chromeOptions);
        //        driver.manage().window().maximize();// 调试浏览器 全屏
        // ---打开网页
        String url = "https://www.zhihu.com/question/68679336";
        driver.get(url);


        // ---执行脚本
        String jsCode = "" +
                "// 初始化设置\n" +
                "window.navigator.webdriver = undefined\n" +
                "var lastScrollHeight = 0,checkRound=0\n" +
                "setInterval(function(){\n" +
                "    window.scrollTo(0,document.body.scrollHeight);\n" +
                "    \n" +
                "    if(lastScrollHeight != document.body.scrollHeight){\n" +
                "        lastScrollHeight = document.body.scrollHeight\n" +
                "    }else{\n" +
                "        checkRound++\n" +
                "        if(checkRound==5){\n" +
                "            console.warn(\"到底了\")\n" +
                "        }\n" +
                "    }\n" +
                "}, 1000)\n" +
                "    ";
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(jsCode);
        // ---判断页面是否加载完成
        a:
        while (true) {
            LogEntries logEntries = driver.manage().logs().get("browser");
            for (LogEntry entry : logEntries) {
                String msg = entry.getMessage();
                // 满足信号，停止循环
                if (msg.contains("到底了")) {
                    break a;
                }
            }
        }
        System.out.println("ok");
        // ---爬取页面
        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);
        // 页面解析操作
        Elements elements = doc.getElementsByClass("List-item");
        System.out.println(elements.size());


    }

    public static void fn3(String singleQuestionPageUrl) {
        // ---启动driver并获取完整页面源码
        WebDriver driver = SeleniumUtils.driverInit("D:\\chromedriver.exe", true, false, false);
        driver.get("https://www.zhihu.com/question/68679336");
        String jsCode = "window.navigator.webdriver = undefined;var lastScrollHeight = 0,checkRound=0;setInterval(function(){window.scrollTo(0,document.body.scrollHeight);if(lastScrollHeight != document.body.scrollHeight){lastScrollHeight = document.body.scrollHeight;}else{checkRound++;if(checkRound==5){console.warn(\"到底了\");}}}, 1000);";
        SeleniumUtils.executeJsCode(driver, jsCode);
        a:
        while (true) {
            LogEntries logEntries = driver.manage().logs().get("browser");
            for (LogEntry entry : logEntries) {
                String msg = entry.getMessage();
                // 满足信号，停止循环
                if (msg.contains("到底了")) {
                    break a;
                }
            }
        }
        String pageSrc = driver.getPageSource();
        Document doc = Jsoup.parse(pageSrc);
        // ---解析页面
        Elements elements = doc.getElementsByClass("List-item");
        System.out.println(elements.size());

    }

    public static void fn4() {

    }

    public static void main(String[] args) {
//        fn1();
//        fn2();
//        fn3();
        fn4();
    }


}
