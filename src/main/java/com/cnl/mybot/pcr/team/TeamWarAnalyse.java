package com.cnl.mybot.pcr.team;

import com.alibaba.fastjson.JSONObject;
import com.cnl.mybot.pcr.team.entity.kill.JsonRootBean;

public class TeamWarAnalyse {

    private JsonRootBean root;

    private TeamWarAnalyse() {
    }

    public static TeamWarAnalyse analyse(String data) {
        TeamWarAnalyse info = new TeamWarAnalyse();
        info.root = JSONObject.parseObject(data, JsonRootBean.class);
        return info;
    }

    public boolean hasNext() {
        return root.getData().getHas_next_page() == 1;
    }

    public JsonRootBean getRoot() {
        return root;
    }

    public static KILL_TYPE getKillType(int kill, int ram) {
        if (kill == 1 && ram == 0) {
            return KILL_TYPE.KILL;
        } else if (kill == 0 && ram == 1) {
            return KILL_TYPE.REMAIN;
        } else if (kill == 0 && ram == 0) {
            return KILL_TYPE.NORMAL;
        } else {
            return KILL_TYPE.OTHER;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static enum KILL_TYPE {
        KILL("收尾刀", 0.5f),
        REMAIN("剩余刀", 0.5f),
        NORMAL("完整刀", 1),
        OTHER("断尾刀", 0.5f);

        private final String name;
        private final float count;

        public float getCount() {
            return count;
        }

        public String getName() {
            return name;
        }

        KILL_TYPE(String name, float count) {
            this.name = name;
            this.count = count;
        }
    }

}
