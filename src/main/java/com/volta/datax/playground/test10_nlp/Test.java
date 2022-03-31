package com.volta.datax.playground.test10_nlp;

public class Test {

    public static void fn() {
        String content = "确实当年有过申请挪威permanent residential permit的机会，不过还是觉得祖国好。当然主要的原因除了大部分亲朋好友割舍不下以外就是女朋友在国内。\n" +
                "回到这个问题上个人觉得提问有点无的放矢，没法判断谁对谁错，当然我能推断大概是什么问题导致交流进入这种下三路的层次；假设确实是对方无理取闹，那么要我说这个问题其实没有办法反驳的，或者没必要反驳。\n" +
                "如果你确实有移民的机会有给予了肯定的回答，那么对方具备相当的可能性反诘【那你就滚出去】；如果你确实暂时没有移民的机会或者想法，那么在斗嘴环节指出别人逻辑的缺失是毫无意义的，对方根本就没有准备和你就事实理论了。实际上能说出这话脑子里也不是不明白那边发展水平高一些，为啥那么多LDGB子女被往外送，都快202年了没必要装外宾，很没意思。\n" +
                "我觉得要么不应答，要么最合适的应对方式就是返璞归真用小学用过的【反弹！】大法。\n" +
                "\n";
        // 筛选掉非疑问句
        boolean isQuery = interrogativeSentimentScore(content, 3);
        // 筛选移民关键字
        if(isQuery){
            int keywordScore = keywordScore(content);
            System.out.println(keywordScore);
        }else{
            System.out.println(0);
        }
    }

    public static boolean interrogativeSentimentScore(String content, Integer threshold) {
        String[] interrogativeSentimentString = new String[]{
                "？", "?",
                "啊", "吗", "可以吗",
                "请问", "请教", "求教",
                "不知道", "想知道",
                "我想", "想了解", "我想要",
                "咋样", "怎么样",
                "好不好",
                "能否"
        };
        int score = 0;
        for (String s : interrogativeSentimentString) {
            if (content.contains(s)) score++;
        }
        return score >= threshold;
    }

    public static int keywordScore(String content) {
        String[] keywordList = new String[]{
                "子女教育以及医疗",
                "自由", "出行",
                "海外", "房产", "投资",
                "离岸", "架构",
                "资产", "分配",
                "双重", "国籍",
                "临时", "居留",
                "华侨生", "国际生", "留学",
                "快速入籍",
                "移民监",
                "CRS",
                "语言",
                "华侨", "华人",
                "自然", "环境，食品安全",
                "联邦", "技术移民",
                "税务", "规划",
                "低分", "清华", "北大",
                "欧盟", "护照"
        };
        int score = 0;
        for (String keyword : keywordList) {
            if (content.contains(keyword)) {
                score++;
            }
        }
        return score;
    }

    public static void main(String[] args) {
        fn();
    }
}
