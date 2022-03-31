package com.volta.datax.projectTemplates.zhihu.v5.utils;

public class CLIProgressBar {


    private int index = 0;
    private String finish;
    private String unFinish;

    // 进度条粒度
    private final int PROGRESS_SIZE = 50;
    private int BITE = 2;

    private String getNChar(int num, char ch) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public void initPrintProgress()  {
        System.out.print("Progress:");

        finish = getNChar(index / BITE, '█');
        unFinish = getNChar(PROGRESS_SIZE - index / BITE, '─');
        String target = String.format("%3d%%[%s%s]", index, finish, unFinish);
        System.out.print(target);

    }

    public void alterPrintProgress(Integer currentPercentage) {
        this.index = currentPercentage;
        finish = getNChar(index / BITE, '█');
        unFinish = getNChar(PROGRESS_SIZE - index / BITE, '─');

        String target = String.format("%3d%%├%s%s┤", index, finish, unFinish);
        System.out.print(getNChar(PROGRESS_SIZE + 6, '\b'));// 清空之前的行
        System.out.print(target);
    }

    public static void main(String[] args) throws InterruptedException {
        CLIProgressBar cliProgressBar = new CLIProgressBar();
        cliProgressBar.initPrintProgress();
        cliProgressBar.alterPrintProgress(20);
    }


}
