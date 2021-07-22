package org.jackhuang.hmcl.upgrade.oldfriends;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static org.jackhuang.hmcl.util.Logging.LOG;

public class ServerConfig {


    @SerializedName("serverVersion")
    public int serverVersion;

    @SerializedName("versionRecords")
    public JsonObject versionRecords;


    private List<ModInfo> _modInfos;

    public void initModInfos() {
        Gson mGson = new Gson();
        for (int i = 1; i <= serverVersion ; i++) {
            LOG.info("版本" + i + " ： " + versionRecords.get(i+""));
            if (versionRecords.get(i+"") != null) {
                List<ModInfo> tempMods = mGson.fromJson(versionRecords.get(i+""), new TypeToken<ArrayList<ModInfo>>(){}.getType());
                _modInfos.addAll(tempMods);
            }
        }
    }

    public List<ModInfo> getModInfo() {
        if (_modInfos == null) {
            _modInfos = new ArrayList<ModInfo>();
        }
        return _modInfos;
    }

    public class ModInfo {

        public static final int OPERATE_DELETE = 2;
        public static final int OPERATE_ADD = 1;

        @SerializedName("modName")
        public String modName;
        @SerializedName("md5")
        public String md5;
        @SerializedName("modOperate")
        public int modOperate;
        @SerializedName("modUrl")
        public String modUrl;
    }
}


