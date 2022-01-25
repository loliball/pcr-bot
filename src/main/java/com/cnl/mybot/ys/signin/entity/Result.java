package com.cnl.mybot.ys.signin.entity;

public class Result {
    private String nickname;
    private Reward award;
    private Integer signDay;
    private String signResult;

    public String getNickname() {
        return nickname;
    }

    public Result setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Reward getAward() {
        return award;
    }

    public Result setAward(Reward award) {
        this.award = award;
        return this;
    }

    public Integer getSignDay() {
        return signDay;
    }

    public Result setSignDay(Integer signDay) {
        this.signDay = signDay;
        return this;
    }

    public String getSignResult() {
        return signResult;
    }

    public Result setSignResult(String signResult) {
        this.signResult = signResult;
        return this;
    }
}
