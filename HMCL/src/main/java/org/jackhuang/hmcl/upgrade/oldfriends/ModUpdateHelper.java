package org.jackhuang.hmcl.upgrade.oldfriends;

import com.google.gson.Gson;
import org.jackhuang.hmcl.mod.ModInfo;
import org.jackhuang.hmcl.mod.ModManager;
import org.jackhuang.hmcl.task.FileDownloadTask;
import org.jackhuang.hmcl.task.Task;
import org.jackhuang.hmcl.task.TaskListener;
import org.jackhuang.hmcl.util.io.NetworkUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.jackhuang.hmcl.util.Logging.LOG;

public class ModUpdateHelper {

    private final String SERVER_HOST = "http://4542879.usa3v.vip/mods/";
    private final String PATH_CONFIG = "serverConfig.json";

    private final Gson mGson = new Gson();
    private ServerConfig mServerConfig;

    /**
     * K : Md5
     * V : Mod 路径
     */
    private Map<String, String> mLocalModsMd5;

    private void initServerModConfig() throws IOException {
        mServerConfig = mGson.fromJson(NetworkUtils.doGet(NetworkUtils.toURL(SERVER_HOST + PATH_CONFIG)), ServerConfig.class);
//        mServerConfig = mGson.fromJson("{\"serverVersion\":2,\"versionRecords\":{\"2\":[{\"modName\":\"樱\",\"md5\":\"adc12a8c1862e0b1a8e196d3b559c3dd\",\"modOperate\":2},{\"modName\":\"工业2\",\"md5\":\"7070bbe9f19af722c6b52fc0b612f295\",\"modOperate\":2},{\"modName\":\"重力装甲\",\"modUrl\":\"Gravitation Suite-3.1.1.jar\",\"md5\":\"df11063bc9e72e8287865ff338235cf9\",\"modOperate\":1},{\"modName\":\"高级太阳能\",\"modUrl\":\"Advanced+Solar+Panels-4.3.0.jar\",\"md5\":\"26348e5d32af51a0676b23b39d32671d\",\"modOperate\":1},{\"modName\":\"樱\",\"modUrl\":\"Sakura-1.0.7-1.12.2.jar\",\"md5\":\"e0c01ed0bc7d4d5b9cfba5841cd55cdc\",\"modOperate\":1},{\"modName\":\"妖怪之山前置\",\"modUrl\":\"MMLib-2.3.0.jar\",\"md5\":\"55c095da05b0f4a25b2e75745ada094f\",\"modOperate\":1},{\"modName\":\"工业2\",\"modUrl\":\"industrialcraft-2-2.8.221-ex112.jar\",\"md5\":\"73f8b72d24d1a7458da1daa969e6e75a\",\"modOperate\":1},{\"modName\":\"拔刀剑\",\"md5\":\"25b71cd8a989fc5c64105604d2f6ca1f\",\"modOperate\":2},{\"modName\":\"拔刀剑\",\"modUrl\":\"SlashBlade-mc1.12-r33.jar\",\"md5\":\"ad6f5cc67139afc1b2350903ad1ef558\",\"modOperate\":1},{\"modName\":\"拔刀剑附属\",\"modUrl\":\"SJAP-1.5.0.jar\",\"md5\":\"7d03bcb7558ce911837df7e2981ac7bf\",\"modOperate\":1}]}}", ServerConfig.class);
    }

    private void initLocalModList(ModManager modManager) throws IOException {
        mLocalModsMd5 = new HashMap<>();
        for (ModInfo modInfo : modManager.getMods()) {
            String path = modInfo.getFile().toString();
            mLocalModsMd5.put(path, fileToMD5(path));
        }
    }

    public static void main(String[] args) {
        try {
            new ModUpdateHelper().execDownloadMod();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ModUpdateHelper updateClient(ModManager modManager, CountDownLatch countDownLatch) throws InterruptedException, IOException {
        LOG.info("老同志更新开始");
        initLocalModList(modManager);
        initServerModConfig();
        for (ServerConfig.ModInfo modInfo : mServerConfig.getModInfo()) {
            updateMod(modInfo);
        }
        LOG.info("老同志更新结束");
        countDownLatch.countDown();
        return this;
    }

    private void updateMod(ServerConfig.ModInfo modInfo) throws IOException {
        switch (modInfo.modOperate) {
            case ServerConfig.ModInfo.OPERATE_DELETE:
                execDeleteMod(modInfo);
                break;
            case ServerConfig.ModInfo.OPERATE_ADD:
                execDownloadMod();
                break;
        }
    }

    public void execDownloadMod() throws IOException {
//        !mLocalModsMd5.containsKey(modInfo.md5)
        if (true) {
            String networkUrl = SERVER_HOST + "version_2/" + "df11063bc9e72e8287865ff338235cf9" + ".jar";
            downloadNet(networkUrl, "hello.jar");
        }
    }

    private void execDeleteMod(ServerConfig.ModInfo modInfo) {
        if (mLocalModsMd5.containsKey(modInfo.md5)) {
            File modFile = new File(mLocalModsMd5.get(modInfo.md5));
            if (modFile.exists()) {
                modFile.delete();
            }
        }
    }

     public void downloadNet(String sourceUrl, String destPath) {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = NetworkUtils.toURL(sourceUrl);
        URLConnection conn = null;
        InputStream is = null;
        FileOutputStream fs = null;
        try {
            conn = url.openConnection();
            is = conn.getInputStream();
            fs = new FileOutputStream(destPath);

            byte[] buffer = new byte[1024];
            while ((byteread = is.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String fileToMD5(String path){
        try {
            FileInputStream fis = new FileInputStream(path);
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            fis.close();
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return  bigInt.toString(16);
        } catch (IOException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }
}
