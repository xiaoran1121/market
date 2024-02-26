package com.example.myapplication.enity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class GoodsInfo {

    public int id;
    // 名称
    public String name;
    // 描述
    public String description;
    // 价格
    public float price;
    // 大图的保存路径
    public String picPath;
    // 大图的资源编号
    public int pic;

    public String type;

    public String mouth_sales;

    // 声明一个手机商品的名称数组
    private static String[] mNameArray = {
            "iPhone11", "Mate30", "小米10", "OPPO Reno3", "vivo X30", "荣耀30S",
            "阿玛尼415", "dw粉底液", "nars大白饼", "圣罗兰唇釉", "兰蔻口红", "花西子蜜粉饼",
            "兰蔻持妆粉底液", "花西子眉笔", "鱼豆腐", "干脆面", "果肉果冻", "芒果干", "无骨鸡爪",
            "手撕面包", "魔芋爽", "猪肉脯", "螺狮粉", "西瓜子", "印花T恤", "牛仔裤", "连衣裙", "衬衫",
            "旗袍", "遮阳帽", "阔腿裤", "板鞋", "儿童裙", "卫衣"
    };
    // 声明一个手机商品的描述数组
    private static String[] mDescArray = {
            "Apple iPhone11 256GB 绿色 4G全网通手机",
            "华为 HUAWEI Mate30 8GB+256GB 丹霞橙 5G全网通 全面屏手机",
            "小米 MI10 8GB+128GB 钛银黑 5G手机 游戏拍照手机",
            "OPPO Reno3 8GB+128GB 蓝色星夜 双模5G 拍照游戏智能手机",
            "vivo X30 8GB+128GB 绯云 5G全网通 美颜拍照手机",
            "荣耀30S 8GB+128GB 蝶羽红 5G芯片 自拍全面屏手机",
            "阿玛尼红管415山楂红 臻致丝绒哑光红管唇釉口红",
            "雅诗兰黛DW持妆粉底液17#象牙白(1W1)30ml油皮遮瑕持久化妆品母亲节礼物,",
            "NARS纳斯 流光美肌轻透蜜粉饼大白饼10g 版本随机发货 彩妆礼物",
            "【多色可选】圣罗兰黑管唇釉 滋润持久 镜面玻璃",
            "兰蔻22年全新粉金小蛮腰唇膏口红 196 羊绒朱砂",
            "服贴定妆组合/蜜粉控油持久不浮粉定妆喷雾便携不脱妆干油皮",
            "兰蔻 持妆轻透粉底液30ml#PO-01象牙白 哑光遮瑕清透",
            "极细三角眉笔/持久防水防汗初学者不容易脱色晕染超细网红",
            "盐津铺子鱼豆腐约20包 零食小吃休闲食品 豆腐干小包装 豆干即食200g",
            "比比赞掌心脆干脆面 干吃面整箱小零食 休闲食品小吃大礼包 解馋怀旧",
            "亲亲桔子果肉果冻大袋装 婚庆喜糖果540g*1袋 休闲食品小吃 儿童零食 ",
            "良品铺子芒果干 108g水果干 果脯年货节零食 网红零食 休闲小吃食品 ",
            "脱骨侠无骨鸡爪 蒜香 柠檬 酸辣 去骨泡椒凤爪零食 休闲食品小吃",
            "三只松鼠手撕面包 1000gX1箱零食 早餐休闲食品 蛋糕点心吐司",
            "卫龙辣条素肉魔芋爽 香辣味180g*1袋 休闲小吃零食品 即食素毛肚",
            "良品铺子猪肉脯 原味100g*1袋 靖江特产 肉干类小吃 办公室休闲零食品",
            "李子柒方便速食柳州螺蛳粉 240g随心包螺狮粉 广西螺丝特产粉丝米线",
            "洽洽话梅味西瓜子 大片400g罐装 坚果炒货干果 休闲零食品 恰恰瓜子",
            "优衣库名侦探柯南联名 男装/女装UT 青山刚昌亲笔签绘 印花T恤456314",
            "男士牛仔裤 男款春秋潮流 直筒宽松休闲长裤子 夏季学生阔腿九分裤",
            "2023年新款 茶歇法式气质衬衫裙 小香赫本风黑色polo连衣裙子 女夏季",
            "白衬衫女长袖 职业春秋夏季 短袖宽松工作服 正装大码 工装女装白衬衣",
            "EIA一尧【花灯游】 新中式复古改良旗袍 连衣裙女 春秋 收腰显瘦裙子 ",
            "三福防晒帽 淑女空顶宽檐帽子 礼帽遮阳帽两用 贝壳帽女夏季2023新款",
            "西装阔腿裤 女裤夏季薄款 2023新款 高腰休闲 高级感垂感直筒雪纺西裤",
            "回力官方旗舰店 板鞋女 2023年新款 春季厚底增高 休闲百搭运动鞋女鞋",
            "女童汉服夏款 中国风2023新款 超仙古装夏季儿童装裙子 女孩古风春秋",
            "长袖t恤 男潮牌潮流春秋款卫衣 男圆领体恤 夏季打底衫 青少年上衣服"
    };
    // 声明一个手机商品的价格数组
    private static float[] mPriceArray = {
            6299, 4999, 3999, 2999, 2998, 2399,
            265, 420, 269, 279, 209, 139, 289, 69,
            11, 5, 16, 19, 50, 30, 14, 15, 11, 60,
            99, 37, 60, 17, 139, 26, 34, 105, 88, 49,
    };
    private static String[] mMouth_salesArray = {
            "1000+", "3000+", "4000+", "500+", "5000+", "1000+",
            "1000+", "1w+", "3000+", "300+", "1000+", "1000+",
            "4000+", "10w+",
            "4000+", "10w+", "9w+", "6w+", "7w+", "5w+", "6w+", "6w+", "6w+", "4w+", "4w+", "7w+", "6000+",
            "3w+", "1w+", "3000+", "7000+", "6000+", "6000+", "8000+", "1w+", "4000+", "1000+"
    };
    // 声明一个手机商品的大图数组
    private static int[] mPicArray = {
            R.drawable.iphone, R.drawable.huawei, R.drawable.xiaomi,
            R.drawable.oppo, R.drawable.vivo, R.drawable.rongyao,
            R.drawable.armani, R.drawable.dw, R.drawable.nars, R.drawable.ysl,
            R.drawable.lancome, R.drawable.huazizi, R.drawable.lancome_makeup,
            R.drawable.meibi,
            R.drawable.yudoufu, R.drawable.gancuimian, R.drawable.guorouguodong, R.drawable.mangguogan, R.drawable.wugujizhua,
            R.drawable.shousimianbao, R.drawable.moyushuang, R.drawable.zhuroufu, R.drawable.luosifen, R.drawable.siguazi,
            R.drawable.yinhua, R.drawable.niuzaiku, R.drawable.lianyiqun, R.drawable.chenshan, R.drawable.qipao,
            R.drawable.zeyangmao, R.drawable.kuotuiku, R.drawable.banxie, R.drawable.ertongqun, R.drawable.weiyi
    };
    private static String[] mTypeArray = {
            "数码", "数码", "数码", "数码", "数码", "数码", "美妆",
            "美妆", "美妆", "美妆", "美妆", "美妆", "美妆", "美妆",
            "食品", "食品", "食品", "食品", "食品", "食品", "食品", "食品", "食品", "食品",
            "服饰", "服饰", "服饰", "服饰", "服饰", "服饰", "服饰", "服饰", "服饰", "服饰"

    };

    // 获取默认的手机信息列表
    public static ArrayList<GoodsInfo> getDefaultList() {
        ArrayList<GoodsInfo> goodsList = new ArrayList<GoodsInfo>();
        for (int i = 0; i < mNameArray.length; i++) {
            GoodsInfo info = new GoodsInfo();
            info.id = i;
            info.name = mNameArray[i];
            info.description = mDescArray[i];
            info.price = mPriceArray[i];
            info.pic = mPicArray[i];
            info.type = mTypeArray[i];
            info.mouth_sales = mMouth_salesArray[i];
            goodsList.add(info);
        }
        return goodsList;
    }
}
