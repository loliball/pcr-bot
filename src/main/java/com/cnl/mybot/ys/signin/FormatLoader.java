package com.cnl.mybot.ys.signin;

import com.cnl.mybot.ys.signin.entity.Result;
import com.cnl.mybot.ys.signin.entity.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FormatLoader {

    private final String strFormat;
    Logger logger = Logger.getLogger(this.getClass());

    public FormatLoader() {
        this(System.getProperty("user.dir") + File.separator + "award-format.txt");
    }

    public FormatLoader(String path) {
        strFormat = getFormatFile(path);
    }

    public String format(User user, List<Result> results) {
        if (strFormat == null) return null;
        StringBuffer sb = new StringBuffer();
        sb.append(strFormat, 0, strFormat.indexOf("=start"));
        String ys = strFormat.substring(strFormat.indexOf("=start") + 7, strFormat.indexOf("=end"));
        results.forEach(result -> {
            sb.append(ys.replace("{nickname}", result.getNickname())
                    .replace("{award}", result.getAward().getName())
                    .replace("{signDay}", result.getSignDay().toString())
                    .replace("{cnt}", result.getAward().getCnt().toString())
                    .replace("{signResult}", result.getSignResult()));
        });
        sb.append(strFormat.substring(strFormat.indexOf("=end") + 4));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        int start = sb.indexOf("{time}");
        if (start != -1)
            sb.replace(start, start + 6, date);
        while (sb.length() > 1 && sb.charAt(sb.length() - 1) == '\n') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString().trim();
    }

    private String getFormatFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("未找到推送格式化文件文件，并且创建新文件失败，程序退出");
                System.exit(0);
            }
        }
        try {
            InputStream fis = new FileInputStream(file);
            Reader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.length() > 1 && line.indexOf("#") != 0) {
                    sb.append(line).append("\n");
                }
            }
            if (sb.charAt(sb.length() - 1) == '\n') {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("cookie加载失败，程序退出");
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }


}
