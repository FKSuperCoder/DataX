package com.volta.datax.core.data.downloader.seleniumUtils;

import com.google.common.io.Files;
import com.volta.datax.core.utils.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

import java.io.File;
import java.io.IOException;

public class SeleniumUtils {

    public static String CHROME_DRIVER_URL = "D:\\chromedriver.exe";


    /**
     * 初始化selenium
     *
     * @param chromeDriverURL
     * @return
     */
    public static WebDriver driverInit(String chromeDriverURL, boolean isShowPicture, boolean isShowWindow, boolean isUseLocalUserData) {
        System.setProperty("webdriver.chrome.driver", chromeDriverURL);
        // ---WebDriver设置与初始化
        ChromeOptions chromeOptions = new ChromeOptions();
        if (!isShowWindow) {
            chromeOptions.addArguments("--headless");// 不显示图形界面
        }
        if (isUseLocalUserData) {
            chromeOptions.addArguments("--user-data-dir=C:\\Users\\Alice\\AppData\\Local\\Google\\Chrome\\User Data");// 不显示图形界面
        }
        if (!isShowPicture) {
            chromeOptions.addArguments("blink-settings=imagesEnabled=false");
        }
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");// 不搞的话，知乎加载不完全
        chromeOptions.addArguments("--window-size=1920x1080");
        chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");// 防止无头模式下功能异常
        WebDriver driver = new ChromeDriver(chromeOptions);
//        driver.manage().window().maximize();// 调试浏览器 全屏
        return driver;
    }

    public static void executeJsCode(WebDriver driver, String jsCode) {
        ((JavascriptExecutor) driver).executeScript(jsCode);
    }

    public static Document downloadPage(String pageUrl) {
        WebDriver driver = SeleniumUtils.driverInit("D:\\chromedriver.exe", false, false, false);
        driver.get(pageUrl);
        String pageSrc = driver.getPageSource();
        return Jsoup.parse(pageSrc);
    }


}
