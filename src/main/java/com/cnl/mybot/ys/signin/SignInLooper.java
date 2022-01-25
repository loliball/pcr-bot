package com.cnl.mybot.ys.signin;

import com.cnl.mybot.system.ConfigLoader;
import com.cnl.mybot.system.Utils;
import com.cnl.mybot.ys.signin.entity.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SignInLooper {

    private final Callback callback;
    private final ScheduledExecutorService threadPool;

    private final ConfigLoader config;
    private final List<User> userList = new ArrayList<>();
    private final FormatLoader msgFormat = new FormatLoader();

    public SignInLooper(Callback cb) {
        this.callback = cb;
        this.config = ConfigLoader.getInstance();
        threadPool = new ScheduledThreadPoolExecutor(1);
        run();
    }

    private void loadCookie(ConfigLoader config, List<User> userList) {
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            String cookie = config.getProp("cookie" + i);
            if (cookie == null) break;
            userList.add(new User(cookie));
        }
    }

    public void signIn() {
        threadPool.execute(this::signInAllImpl);
    }

    public void update() {
        threadPool.execute(this::updateImpl);
    }

    private void signInAllImpl() {
        ApiTakumi.getRoles(userList);
        userList.forEach(user -> {
            ApiTakumi.signIn(user, results -> {
                if (callback == null) return;
                callback.onSignIn(msgFormat.format(user, results));
            });
            try {
                Thread.sleep(Utils.random(5, 10) * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateImpl() {
        int count = ApiTakumi.updateSignDay(userList);
        if (callback == null) return;
        callback.onSignIn(String.format("Success reload %d users", count));
    }

    private void run() {
        threadPool.execute(() -> {
            loadCookie(config, userList);
            ApiTakumi.initSignAwards(userList.get(0).getCookie());
            System.out.println("已经开启定时任务,每天将自动签到");
        });

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        calendar.add(Calendar.DATE, 1);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                threadPool.schedule(() -> signInAllImpl(), Utils.random(2 * 60 * 60, 5 * 60 * 60), TimeUnit.SECONDS);
            }
        }, calendar.getTime(), 24 * 60 * 60 * 1000);
    }

    public interface Callback {
        void onSignIn(String msg);
    }
}
