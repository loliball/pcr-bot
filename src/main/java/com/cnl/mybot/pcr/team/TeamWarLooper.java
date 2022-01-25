package com.cnl.mybot.pcr.team;

import com.cnl.mybot.pcr.team.TeamWarAnalyse.KILL_TYPE;
import com.cnl.mybot.pcr.team.entity.bossnow.CurrentData;
import com.cnl.mybot.pcr.team.entity.kill.Bean;
import com.cnl.mybot.pcr.team.entity.kill.JsonRootBean;
import com.cnl.mybot.pcr.team.entity.search.SearchData;
import com.cnl.mybot.pcr.team.entity.timerange.TimeRangeData;
import com.cnl.mybot.pcr.team.entity.user.Damage_list;
import com.cnl.mybot.pcr.team.entity.user.Data2;
import com.cnl.mybot.system.MessageOutput;
import com.cnl.mybot.system.Utils;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TeamWarLooper {

    private Timer dailyCleanTimer;
    private Timer dailyReportTimer;
    private ScheduledFuture<?> lastFuture;
    private TeamWarBossListAnalyse bossList;
    private final BossBookManager bookManager;
    private final Callback callback;
    private final ScheduledExecutorService threadPool;
    private final Map<String, Float> killMap;   //name - kill
    private final Map<Long, Bean> lists;        //time - bean
    private long lastTime = 0;
    private int maxLapNum = 0;
    private float totalDamageNum = 0;

    public TeamWarLooper(MessageOutput output, Function<MessageChain, MessageReceipt<?>> output2) {

        this(new Callback() {

            final DateFormat daf = new SimpleDateFormat("HH:mm:ss");
            final DateFormat dafyyyy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final DecimalFormat df = new DecimalFormat("####,####");

            @Override
            public void onDailyReport(String summary) {
                summary = String.format("公会日报%n现在是%s%n==========%n%s",
                        dafyyyy.format(new Date()), summary);
                output.sendMessage(summary);
            }

            @Override
            public void onKill(ResultBean bean) {
                output.sendMessage(String.format(
                        "%s于%s%n对 %s(%d) 造成了%n%s万伤害(%s)%n(第%.1f刀，%s)%n当前进度：%d周目%d王%n%s/%s%n公会总计刀数(%.1f/90)",
                        bean.getName(),
                        daf.format(new Date(bean.getDatetime() * 1000)),
                        bean.getBoss_name(),
                        bean.getLap_num(),
                        df.format(bean.getDamage() / 10000),
                        df.format(bean.getDamage()),
                        bean.getKillCount(),
                        TeamWarAnalyse.getKillType(bean.getKill(), bean.getReimburse()).getName(),
                        bean.getCurrentLap(),
                        bean.getBossIndex(),
                        df.format(bean.getCurrentLife()),
                        df.format(bean.getTotalLife()),
                        bean.getTotalKillCount()
                ));
            }

            @Override
            public void onBook(long qq, int id, String boss) {
                At at = new At(qq);
                PlainText txt = new PlainText(String.format(
                        "订阅的%d王%s到了",
                        id, boss));
                output2.apply(at.plus(txt));
            }
        });
    }

    public TeamWarLooper(Callback cb) {
        bookManager = new BossBookManager();
        threadPool = Executors.newScheduledThreadPool(1);
        killMap = new HashMap<>();
        lists = new HashMap<>();
        callback = cb;
        run();
        clean();
        dailyReport();
    }

    private void clean() {
        if (dailyCleanTimer != null)
            dailyCleanTimer.cancel();
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)
                , 5, 1, 0);
        if (now.get(Calendar.HOUR_OF_DAY) >= 5)
            calendar.add(Calendar.DATE, 1);
        dailyCleanTimer = new Timer();
        dailyCleanTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                threadPool.execute(() -> clearAllData());
            }
        }, calendar.getTime(), 24 * 60 * 60 * 1000);
    }

    private long remain() {
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.set(target.get(Calendar.YEAR)
                , target.get(Calendar.MONTH)
                , target.get(Calendar.DAY_OF_MONTH)
                , 5, 0, 0);
        if (now.get(Calendar.HOUR_OF_DAY) >= 5) {
            target.add(Calendar.DATE, 1);
        }
        return target.getTimeInMillis() - now.getTimeInMillis();
    }

    public void testDailyReport() {
        getBossInfo(callback::onDailyReport);
    }

    private void dailyReport() {
        if (dailyReportTimer != null)
            dailyReportTimer.cancel();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                5, 30, 0);
        if (Calendar.getInstance().after(calendar))
            calendar.add(Calendar.DATE, 1);
        dailyReportTimer = new Timer();
        dailyReportTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getBossInfo(callback::onDailyReport);
            }
        }, calendar.getTime(), 24 * 60 * 60 * 1000);
    }

    private void init() {
        clearAllData();
        try {
            String bListData = TeamWarAPI.getBOSSList();
            bossList = TeamWarBossListAnalyse.analyse(bListData);
            for (JsonRootBean root : TeamWarAPI.getTeamWarInfoAll()) {
                List<Bean> bb = root.getData().getList();
                if (bb == null) continue;
                for (Bean b : bb) {
                    lastTime = Math.max(lastTime, b.getDatetime());
                    maxLapNum = Math.max(maxLapNum, b.getLap_num());
                    lists.put(b.getDatetime(), b);
                    KILL_TYPE type = TeamWarAnalyse.getKillType(b.getKill(), b.getReimburse());
                    killMap.put(b.getName(), killMap.getOrDefault(b.getName(), 0f) + type.getCount());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        calcTotalKillCount();
    }

    private void content() {
        try {
            String bossInfoData = TeamWarAPI.getNowBossInfo();
            CurrentData bossInfo = TeamWarNowBossAnalyse.analyse(bossInfoData).getRoot().getData();
            int currentLap = bossInfo.getBoss_info().getLap_num();
            int bossIndex = bossList.name2id(bossInfo.getBoss_info().getName());
            long currentLife = bossInfo.getBoss_info().getCurrent_life();
            long totalLife = bossInfo.getBoss_info().getTotal_life();
            String data = TeamWarAPI.getTeamWarInfo(1);
            TeamWarAnalyse analyse = TeamWarAnalyse.analyse(data);
            int cDMG = analyse.getRoot().getData().getTotal_damage_num();
            //10分钟出了30刀的情况???
            if (cDMG - totalDamageNum > 30) {
                init();
            } else {
                List<Bean> bList = analyse.getRoot().getData().getList();
                if (bList == null) {
                    System.out.println("bList is null, skip this loop");
                    return;
                }
                Collections.reverse(bList);
                long nowLastTime = 0;
                for (Bean b : bList) {
                    long nowTime = b.getDatetime();
                    if (lists.containsKey(nowTime)) {
                        continue;
                    }
                    nowLastTime = Math.max(nowLastTime, nowTime);
                    maxLapNum = Math.max(maxLapNum, b.getLap_num());
                    KILL_TYPE type = TeamWarAnalyse.getKillType(b.getKill(), b.getReimburse());
                    killMap.put(b.getName(), killMap.getOrDefault(b.getName(), 0f) + type.getCount());
                    totalDamageNum += type.getCount();
                    if (callback != null) {
                        callback.onKill(new ResultBean(b,
                                killMap.getOrDefault(b.getName(), 0f),
                                totalDamageNum,
                                currentLap,
                                bossIndex,
                                currentLife,
                                totalLife));
                        bookManager.update(
                                bossList.name2id(b.getBoss_name()),
                                b.getKill(),
                                b.getBoss_name(), callback);
                    }
                    lists.put(nowTime, b);
                }
                lastTime = nowLastTime;
            }
        } catch (Exception e) {
            System.out.println("something went wrong, please check the error output.");
            e.printStackTrace();
        } finally {
            lastFuture = threadPool.schedule(this::content, Utils.random(6 * 60, 12 * 60), TimeUnit.SECONDS);
        }
    }

    private void run() {
        threadPool.execute(this::init);
        lastFuture = threadPool.schedule(this::content, Utils.random(6 * 60, 12 * 60), TimeUnit.SECONDS);
    }

    private void calcTotalKillCount() {
        totalDamageNum = 0;
        for (Long key : lists.keySet()) {
            Bean bean = lists.get(key);
            KILL_TYPE type = TeamWarAnalyse.getKillType(bean.getKill(), bean.getReimburse());
            totalDamageNum += type.getCount();
        }
    }

    private void clearAllData() {
        if (killMap != null)
            killMap.clear();
        if (lists != null)
            lists.clear();
        lastTime = 0;
        maxLapNum = 0;
        totalDamageNum = 0;
    }

    public void getAllUsers(SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            StringBuilder sb = new StringBuilder();
            String dat = TeamWarAPI.getTeamWarAllUsers();
            List<Data2> list = TeamWarUserAnalyse.analyse(dat).getRoot().getData();
            list.forEach(dd -> sb.append(dd.getName()).append('\n'));
            sb.append("==========\n");
            sb.append(String.format("成员统计：(%d/30)", list.size()));
            cb.onSuccess(sb.toString());
        });
    }

    public void bookBoss(int id, long qq, SummaryCallback cb) {
        if (cb == null || id < 1 || id > 5) return;
        bookManager.book(qq, id);
        int preBoss = TeamWarBossListAnalyse.preBoss(id);
        cb.onSuccess(String.format("成功订阅%d王 %s%n将于%d王 %s%n尾刀时at提醒",
                id, bossList.id2name(id), preBoss, bossList.id2name(preBoss)));
    }

    public void getPersonalSummary(String name, SummaryCallback cb) {
        if (name == null || cb == null) return;
        threadPool.execute(() -> {
            StringBuilder sb = new StringBuilder("查询到以下信息：");
            String dat = TeamWarAPI.getTeamWarAllUsers();
            List<Data2> list = TeamWarUserAnalyse.analyse(dat).getRoot().getData();
            Collections.reverse(list);
            boolean queueSuccess = false;
            String lowerCaseQuery = name.toLowerCase(Locale.ROOT);
            for (Data2 dat2 : list) {
                if (dat2.getName().toLowerCase(Locale.ROOT).contains(lowerCaseQuery)) {
                    sb.append("\n==========");
                    sb.append(String.format("%n%s(%.1f刀)",
                            dat2.getName(),
                            dat2.getNumber()));
                    if (dat2.getNumber() == 0) {
                        sb.append("\n尚未出刀");
                    } else for (Damage_list dList : dat2.getDamage_list()) {
                        sb.append(String.format(
                                "%n对%d王造成%s万伤害(%.1f刀)",
                                bossList.name2id(dList.getBoss_name()),
                                dList.getDamage() / 10000,
                                TeamWarAnalyse.getKillType(dList.getKill(), dList.getReimburse()).getCount()));
                    }
                    queueSuccess = true;
                }
            }
            if (queueSuccess) cb.onSuccess(sb.toString());
            else cb.onSuccess("请使用游戏内名称查询");
        });
    }

    public void getSummary(SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            StringBuilder sb = new StringBuilder("今日余刀：\n");
            String dat = TeamWarAPI.getTeamWarAllUsers();
            List<Data2> list = TeamWarUserAnalyse.analyse(dat).getRoot().getData();
            Collections.reverse(list);
            int member = 0, total = 0;
            for (Data2 dat2 : list) {
                total++;
                if (dat2.getNumber() == 3) continue;
                sb.append(String.format(
                        "剩%.1f刀, %s%n",
                        3f - dat2.getNumber(),
                        dat2.getName()));
                member++;
            }
            sb.append("==========\n");
            sb.append(String.format(
                    "总出刀：%.1f/90(剩%.1f刀)%n",
                    totalDamageNum,
                    90f - totalDamageNum));
            sb.append(String.format(
                    "总人数：%d/%d(剩%d人)",
                    total - member, total,
                    member));
            cb.onSuccess(sb.toString());
        });
    }

    public void getRemain(SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            StringBuilder sb = new StringBuilder("剩余刀：\n");
            String dat = TeamWarAPI.getTeamWarAllUsers();
            List<Data2> list = TeamWarUserAnalyse.analyse(dat).getRoot().getData();
            Collections.reverse(list);
            int member = 0;
            List<String> sortList = new ArrayList<>();
            for (Data2 dat2 : list) {
                if (dat2.getNumber() == 3) continue;
                if (dat2.getNumber() == (int) dat2.getNumber()) continue;
                Damage_list lastDamageList = null;
                for (Damage_list dList : dat2.getDamage_list()) {
                    KILL_TYPE type = TeamWarAnalyse.getKillType(dList.getKill(), dList.getReimburse());
                    if (type != KILL_TYPE.KILL) continue;
                    if (lastDamageList == null || dList.getDatetime() > lastDamageList.getDatetime()) {
                        lastDamageList = dList;
                    }
                }
                if (lastDamageList != null) {
                    sortList.add(String.format(
                            "%d王%s万，%s%n",
                            bossList.name2id(lastDamageList.getBoss_name()),
                            lastDamageList.getDamage() / 10000,
                            dat2.getName()));
                }
                member++;
            }
            if (!sortList.isEmpty()) {
                sortList.sort((o1, o2) -> {
                    int i1 = Integer.parseInt(o1.substring(0, 1));
                    int i2 = Integer.parseInt(o2.substring(0, 1));
                    if (i1 == i2) return 0;
                    return (i1 > i2) ? 1 : -1;
                });
                for (String item : sortList) {
                    sb.append(item);
                }
            }
            sb.append("==========\n");
            sb.append(String.format(
                    "总剩余刀数：%d",
                    member));
            cb.onSuccess(sb.toString());
        });
    }

    public void getBossInfo(SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            StringBuilder sb = new StringBuilder();
            DecimalFormat df = new DecimalFormat("####,####");
            String dat = TeamWarAPI.getNowBossInfo();
            CurrentData data = TeamWarNowBossAnalyse.analyse(dat).getRoot().getData();
            sb.append(String.format("%s%n当前排名%d(%s)%n",
                    data.getClan_info().getName(),
                    data.getClan_info().getLast_ranking(),
                    data.getClan_info().getLast_total_ranking()));
            sb.append("==========\n");
            sb.append(String.format("%s boss信息%n现在%d周目，%s(%d)%n%s/%s%n",
                    data.getBattle_info().getName(),
                    data.getBoss_info().getLap_num(),
                    data.getBoss_info().getName(),
                    bossList.name2id(data.getBoss_info().getName()),
                    df.format(data.getBoss_info().getCurrent_life()),
                    df.format(data.getBoss_info().getTotal_life())
            ));
            sb.append("==========\n");
            sb.append(String.format("会战将于%s结束",
                    data.getDay_list().get(0)));
            cb.onSuccess(sb.toString());
        });
    }

    public void historyTerm(SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            String json = TeamWarAPI.getClanBattleList();
            var analyse = TeamWarHistoryTermAnalyse.analyse(json);
            if (analyse == null) {
                System.out.println(json);
                cb.onSuccess("请求失败");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("往期会战\n");
            for (var data : analyse.getData()) {
                sb.append(String.format("%s: %d%n", data.name, data.id));
            }
            sb.deleteCharAt(sb.length() - 1);
            cb.onSuccess(sb.toString());
        });
    }

    public void yesterdayRank(SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            String json = TeamWarAPI.getClanBattleList();
            var analyse = TeamWarHistoryTermAnalyse.analyse(json);
            if (analyse == null) {
                System.out.println(json);
                cb.onSuccess("请求失败");
                return;
            }
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) < 5) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            calendar.set(Calendar.HOUR_OF_DAY, 5);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            TimeRangeData trd = new TimeRangeData(
                    analyse.getNowId(),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    5,
                    0);
            SearchData sd = historyRankOnce(trd);
            DateFormat daf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DecimalFormat df = new DecimalFormat("####,####");
            if (sd == null) {
                System.out.println("sd is null!");
                cb.onSuccess("获取失败");
                return;
            }
            cb.onSuccess(String.format("%s%n排名: %d%n分数: %s",
                    daf.format(calendar.getTime()),
                    sd.getRank(),
                    df.format(sd.getDamage())));
        });
    }

    public void historyRankSync(SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            String json = TeamWarAPI.getClanBattleList();
            var analyse = TeamWarHistoryTermAnalyse.analyse(json);
            if (analyse == null) {
                System.out.println(json);
                cb.onSuccess("请求失败");
                return;
            }
            historyRank(analyse.getNowId(), cb);
        });
    }

    public void historyRankSync(int battle, SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            historyRank(battle, cb);
        });
    }

    private static void historyRank(int battle, SummaryCallback cb) {
        List<TimeRangeData> timeRangeData = historyTimeRange(battle);
        if (timeRangeData == null) {
            cb.onSuccess("时间区域获取失败！");
            return;
        }
        List<TimeRangeData> times = timeRangeData.stream()
                .filter(time -> time.getHour() == 5 && time.getMinute() == 0)
                .collect(Collectors.toList());
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.clear();
        calendar2.clear();
        timeRangeData.stream().max((time1, time2) -> {
            //noinspection MagicConstant
            calendar.set(time1.getYear(),
                    time1.getMonth() - 1,
                    time1.getDay(),
                    time1.getHour(),
                    time1.getMinute(),
                    0);
            //noinspection MagicConstant
            calendar2.set(time2.getYear(),
                    time2.getMonth() - 1,
                    time2.getDay(),
                    time2.getHour(),
                    time2.getMinute(),
                    0);
            return calendar.compareTo(calendar2);
        }).ifPresent(times::add);
        DateFormat daf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df = new DecimalFormat("####,####");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("第%d期会战%n", battle));
        sb.append("==========\n");
        for (TimeRangeData time : times) {
            //noinspection MagicConstant
            calendar.set(time.getYear(),
                    time.getMonth() - 1,
                    time.getDay(),
                    time.getHour(),
                    time.getMinute(),
                    0);
            try {
                SearchData sd = historyRankOnce(time);
                if (sd == null) {
                    continue;
                }
                sb.append(String.format("%s%n排名: %d%n分数: %s%n",
                        daf.format(calendar.getTime()),
                        sd.getRank(),
                        df.format(sd.getDamage())));
            } catch (Exception e) {
                e.printStackTrace();
                sb.append(daf.format(calendar.getTime())).append("\n获取失败\n");
            } finally {
                sb.append("==========\n");
            }
        }
        sb.append(String.format("总计%d条记录", times.size()));
        cb.onSuccess(sb.toString());
    }

    @Nullable
    private static List<TimeRangeData> historyTimeRange(int battle) {
        String json = TeamWarAPI.getTimeRange(battle);
        List<TimeRangeData> data = TeamWarHistoryTermTimeRangeAnalyse.analyse(json).getRoot().getData();
        if (data == null || data.size() == 0) return null;
        return data;
    }

    private static SearchData historyRankOnce(TimeRangeData time) {
        String json = TeamWarAPI.getMyClanRanking(time.getYear(), time.getMonth(), time.getDay(),
                time.getHour(), time.getMinute(), time.getBattle_id());
        return TeamWarHistoryRankAnalyse.analyse(json).getData();
    }

    public void searchByName(String name, SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            String json = TeamWarAPI.searchByName(name);
            search(cb, json);
        });
    }

    public void searchByRank(String rank, SummaryCallback cb) {
        if (cb == null) return;
        threadPool.execute(() -> {
            String json = TeamWarAPI.searchByRank(rank);
            search(cb, json);
        });
    }

    private void search(SummaryCallback cb, String json) {
        DecimalFormat df = new DecimalFormat("####,####");
        StringBuilder sb = new StringBuilder();
        List<SearchData> data = SearchResultAnalyse.analyse(json).getRoot().getData();
        if (data == null || data.size() == 0) {
            sb.append("没有结果");
        } else {
            for (SearchData data1 : data) {
                sb.append(String.format("公会: %s%n排名: %d%n会长: %s%n分数: %s%n",
                        data1.getClan_name(),
                        data1.getRank(),
                        data1.getLeader_name(),
                        df.format(data1.getDamage())
                ));
                sb.append("==========\n");
            }
            sb.append(String.format("共计%d个结果", data.size()));
        }
        cb.onSuccess(sb.toString());
    }

    public String debug() {
        System.out.println(killMap);
        System.out.println(lists);
        System.out.println(bookManager.list);
        System.out.printf("lastTime = %d%nmaxLapNum = %d%ntotalDamageNum = %f%n", lastTime, maxLapNum, totalDamageNum);
        return "OK";
    }

    public String wake() {
        if (lastFuture != null) return "already running";
        lastFuture = threadPool.schedule(this::content, 1, TimeUnit.SECONDS);
        clean();
        dailyReport();
        return "OK";
    }

    public String cancel() {
        StringBuilder sb = new StringBuilder();
        if (lastFuture != null) {
            boolean cancel = lastFuture.cancel(false);
            lastFuture = null;
            sb.append("cancel lastFuture ").append(cancel ? "success" : "failed").append('\n');
        } else sb.append("Future is NULL\n");
        if (dailyCleanTimer != null) {
            dailyCleanTimer.cancel();
            dailyCleanTimer = null;
            sb.append("cancel dailyCleanTimer success\n");
        } else sb.append("dailyCleanTimer is NULL\n");
        if (dailyReportTimer != null) {
            dailyReportTimer.cancel();
            dailyReportTimer = null;
            sb.append("cancel dailyReportTimer success\n");
        } else sb.append("dailyReportTimer is NULL\n");
        return sb.toString();
    }

    public String sleep() {
        if (lastFuture != null) {
            boolean cancel = lastFuture.cancel(false);
            lastFuture = null;
            if (!cancel) return "cancel lastFuture failed";
        }
        long remainTime = remain() + Utils.random(10_000, 100_000);
        lastFuture = threadPool.schedule(this::run, remainTime, TimeUnit.MILLISECONDS);
        return "Thread will sleep " + remainTime + " milliseconds";
    }

    public String sync() {
        threadPool.execute(this::init);
        return "OK";
    }

    public interface Callback {
        void onDailyReport(String summary);

        void onKill(ResultBean bean);

        void onBook(long qq, int id, String boss);
    }

    public interface SummaryCallback {
        void onSuccess(String str);
    }

    private static class BossBookManager {

        private final List<BookBean> list;

        public BossBookManager() {
            list = new ArrayList<>();
        }

        public void book(long qq, int id) {
            list.add(new BookBean(qq, id));
        }

        public void update(int id, int kill, String name, Callback cb) {
            if (kill != 1) return;
            id = TeamWarBossListAnalyse.nextBoss(id);
            Iterator<BookBean> iterator = list.iterator();
            while (iterator.hasNext()) {
                BookBean bean = iterator.next();
                if (id == bean.id) {
                    iterator.remove();
                    cb.onBook(bean.qq, id, name);
                }
            }
        }

        private static class BookBean {
            long qq;
            int id;

            public BookBean(long qq, int id) {
                this.qq = qq;
                this.id = id;
            }
        }
    }

    public static class ResultBean extends Bean {
        private final float killCount;
        private final float totalKillCount;
        private final int currentLap;
        private final int bossIndex;
        private final long currentLife;
        private final long totalLife;

        public ResultBean(
                Bean bean,
                float killCount,
                float totalKillCount,
                int currentLap,
                int bossIndex,
                long currentLife,
                long totalLife) {
            this.killCount = killCount;
            this.totalKillCount = totalKillCount;
            this.currentLap = currentLap;
            this.bossIndex = bossIndex;
            this.currentLife = currentLife;
            this.totalLife = totalLife;
            setDatetime(bean.getDatetime());
            setName(bean.getName());
            setBoss_name(bean.getBoss_name());
            setLap_num(bean.getLap_num());
            setDamage(bean.getDamage());
            setKill(bean.getKill());
            setReimburse(bean.getReimburse());
            setScore(bean.getScore());
        }

        public float getKillCount() {
            return killCount;
        }

        public float getTotalKillCount() {
            return totalKillCount;
        }

        public int getCurrentLap() {
            return currentLap;
        }

        public int getBossIndex() {
            return bossIndex;
        }

        public long getCurrentLife() {
            return currentLife;
        }

        public long getTotalLife() {
            return totalLife;
        }

    }
}
