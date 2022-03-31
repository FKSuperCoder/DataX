window.navigator.webdriver = undefined;
var lastScrollHeight = 0, checkRound = 0;
var scrollInterval = setInterval(function () {
    window.scrollTo(0, document.body.scrollHeight);
    if (lastScrollHeight != document.body.scrollHeight) {
        lastScrollHeight = document.body.scrollHeight;
    } else { // 高度未变
        // 判断此时是
        var loadingBar = document.getElementsByClassName("LoadingBar is-active")[0]
        if (loadingBar === undefined) {
            console.warn("到底了")
        }else{
            console.warn("未到底")
        }
    }
}, 100);


try {
    document.getElementsByClassName('btnTagClassName')[0].click();
} catch (e) {
}


var closeLoginWidgetInterval = setInterval(() => {
    try {
        document.getElementsByClassName("Button Modal-closeButton Button--plain")[0].click();
    } catch (e) {
    }
}, 1000)

