try {
    // scroll logic
    window.navigator.webdriver = undefined;

    var lastScrollHeight = 0, tolerance = 5;
    var scrollInterval = setInterval(function () {
        window.scrollTo(0, document.body.scrollHeight);
        window.scrollTo(0, 0);
        window.scrollTo(0, document.body.scrollHeight);
        if (lastScrollHeight != document.body.scrollHeight) { // 高度仍然在增加
            lastScrollHeight = document.body.scrollHeight;
        } else { // 高度未变
            var loadingBarIsActiveDom = document.getElementsByClassName("LoadingBar is-active")[0]

            if (loadingBarIsActiveDom === undefined) {
                tolerance--;
                if (tolerance === 0) {
                    console.warn("到底了");
                    clearInterval(scrollInterval)
                }
            } else {
                tolerance++;
            }
        }

    }, 1000);

    //  progress logic
    var totalLength, currentLength = 0, lastLength = 0;
    totalLength = parseInt(document.getElementsByClassName("List-headerText")[0].firstChild.innerText)
    setInterval(() => {
        currentLength = document.getElementsByClassName("List-item").length
        if (currentLength !== lastLength) {
            console.warn("进度：" + (currentLength / totalLength * 100).toFixed(2) + "%");
            lastLength = currentLength
        }
    }, 1000)

} catch (e) {
}



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


document.getElementsById("zh-question-log-list-wrap");
