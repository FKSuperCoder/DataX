package com.volta.datax.projectTemplates.zhihu.v4.downloader.seleniumUtils;

import com.volta.datax.core.data.downloader.seleniumUtils.SeleniumUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

public class ZhiHuSeleniumUtils extends SeleniumUtils {
    public static String CHROME_DRIVER_URL = "D:\\chromedriver.exe";

    public static Document downloadScrollPage(String pageUrl) {
        WebDriver driver = driverInit(CHROME_DRIVER_URL, false, false, false);
        String pageSrc = null;
        try {
            driver.get(pageUrl);

            scrollCore(driver);
            waitMsg(driver);

            pageSrc = driver.getPageSource();
            driver.quit();
        } catch (Exception e) {
            driver.quit();
            System.out.println("selenium异常，已退出WebDriver");
            e.printStackTrace();
        }
        return Jsoup.parse(pageSrc);
    }

    public static Document downloadScrollAndClickPage(String pageUrl, String btnTagClassName) {
        WebDriver driver = driverInit("D:\\chromedriver.exe", false, false, false);
        String pageSrc = null;
        try {
            driver.get(pageUrl);
            clickCore(driver, btnTagClassName);
            scrollCore(driver);

            waitMsg(driver);
            pageSrc = driver.getPageSource();
            driver.quit();
        } catch (Exception e) {
            driver.quit();
            System.out.println("selenium异常，已退出WebDriver");
            e.printStackTrace();
        }
        return Jsoup.parse(pageSrc);
    }

    public static void waitMsg(WebDriver driver) {
//        CLIProgressBar questionProgress = new CLIProgressBar();
//        questionProgress.initPrintProgress();
        a:
        while (true) {
            LogEntries logEntries = driver.manage().logs().get("browser");
            for (LogEntry entry : logEntries) {
                String msg = entry.getMessage();
                // ---滑动终止逻辑
                if (msg.contains("到底了")) {// 满足信号，停止循环
                    System.out.println("滑动加载完毕");
                    break a;
                }
                // ---报告进度逻辑
//                if (msg.contains("进度")) {
//                    String progressRaw = msg.substring(msg.indexOf("进度"), msg.length() - 1);
//                    progressRaw = progressRaw.substring(3, progressRaw.length() - 4);// 百分号乃至小数点
//                    Integer progress = Integer.parseInt(progressRaw);
//                    questionProgress.alterPrintProgress(progress);
//                }
            }
        }
    }

    public static void scrollCore(WebDriver driver) {
        executeJsCode(driver, "try {\n" +
                "    // scroll logic\n" +
                "    window.navigator.webdriver = undefined;\n" +
                "\n" +
                "    var lastScrollHeight = 0, tolerance = 5;\n" +
                "    var scrollInterval = setInterval(function () {\n" +
                "        window.scrollTo(0, document.body.scrollHeight);\n" +
                "        window.scrollTo(0, 0);\n" +
                "        window.scrollTo(0, document.body.scrollHeight);\n" +
                "        if (lastScrollHeight != document.body.scrollHeight) { // 高度仍然在增加\n" +
                "            lastScrollHeight = document.body.scrollHeight;\n" +
                "        } else { // 高度未变\n" +
                "            var loadingBarIsActiveDom = document.getElementsByClassName(\"LoadingBar is-active\")[0]\n" +
                "\n" +
                "            if (loadingBarIsActiveDom === undefined) {\n" +
                "                tolerance--;\n" +
                "                if (tolerance === 0) {\n" +
                "                    console.warn(\"到底了\");\n" +
                "                    clearInterval(scrollInterval)\n" +
                "                }\n" +
                "            } else {\n" +
                "                tolerance++;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "    }, 1000);\n" +
                "\n" +
                "    //  progress logic\n" +
                "    var totalLength, currentLength = 0, lastLength = 0;\n" +
                "    totalLength = parseInt(document.getElementsByClassName(\"List-headerText\")[0].firstChild.innerText)\n" +
                "    setInterval(() => {\n" +
                "        currentLength = document.getElementsByClassName(\"List-item\").length\n" +
                "        if (currentLength !== lastLength) {\n" +
                "            console.warn(\"进度：\" + (currentLength / totalLength * 100).toFixed(2) + \"%\");\n" +
                "            lastLength = currentLength\n" +
                "        }\n" +
                "    }, 1000)\n" +
                "\n" +
                "} catch (e) {\n" +
                "}");

    }

    public static void clickCore(WebDriver driver, String btnTagClassName) {
        executeJsCode(driver, "try{\n" +
                "    document.getElementsByClassName('" + btnTagClassName + "')[0].click();\n" +
                "}catch (e) {}\n");
    }

}

