package com.cnl.mybot.ys.signin.entity;

public class GameRole {
    public Long getGameUid() {
        return gameUid;
    }

    public GameRole setGameUid(Long gameUid) {
        this.gameUid = gameUid;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public GameRole setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public GameRole setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public String getGame_biz() {
        return game_biz;
    }

    public GameRole setGame_biz(String game_biz) {
        this.game_biz = game_biz;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public GameRole setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getRegion_name() {
        return region_name;
    }

    public GameRole setRegion_name(String region_name) {
        this.region_name = region_name;
        return this;
    }

    public String getCookie() {
        return cookie;
    }

    public GameRole setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    public Integer getSignDay() {
        return signDay;
    }

    public GameRole setSignDay(Integer signDay) {
        this.signDay = signDay;
        return this;
    }

    private Long gameUid;
    private String nickname;
    private Integer level;
    private String game_biz;
    private String region;
    private String region_name;
    private String cookie;
    private Integer signDay;
}
