package com.zplay.playable.zplayvideoplayer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

class MonitoringParser {
    static final private String json = "{\n" +
            "    \"videoDuration\": 39.084,\n" +
            "    \"ctrls\": [{\n" +
            "        \"id\": 0,\n" +
            "        \"span\": {\n" +
            "            \"start\": 0.029825745884773658,\n" +
            "            \"loopStart\": 0.05246913580246913,\n" +
            "            \"end\": 0.11419753086419752\n" +
            "        },\n" +
            "        \"events\": [{\n" +
            "            \"block\": [0, 0, 0.748792270531401, 0.41469332298136646],\n" +
            "            \"action\": [0, 0, 0, 0, 0, 1, 0, 0, 0]\n" +
            "        }]\n" +
            "    }, {\n" +
            "        \"id\": 1,\n" +
            "        \"span\": {\n" +
            "            \"start\": 0.24793274176954735,\n" +
            "            \"loopStart\": 0.2726337448559671,\n" +
            "            \"end\": 0.3631687242798354\n" +
            "        },\n" +
            "        \"events\": [{\n" +
            "            \"block\": [0, 0.03881987577639751, 0.6928916494133885, 0.34782608695652173],\n" +
            "            \"action\": [0, 0, 0, 0, 0, 1, 0, 0, 0]\n" +
            "        }]\n" +
            "    }, {\n" +
            "        \"id\": 2,\n" +
            "        \"span\": {\n" +
            "            \"start\": 0.41254179526748974,\n" +
            "            \"loopStart\": 0.43724279835390945,\n" +
            "            \"end\": 0.5010288065843621\n" +
            "        },\n" +
            "        \"events\": [{\n" +
            "            \"block\": [0, 0, 1, 1],\n" +
            "            \"action\": [0, 0, 0, 0, 0, 0, 0, 1, 0]\n" +
            "        }]\n" +
            "    }, {\n" +
            "        \"id\": 3,\n" +
            "        \"span\": {\n" +
            "            \"start\": 0.6265335648148148,\n" +
            "            \"loopStart\": 0.6512345679012346,\n" +
            "            \"end\": 0.7109053497942387\n" +
            "        },\n" +
            "        \"events\": [{\n" +
            "            \"block\": [0, 0.040372670807453416, 0.6570048309178744, 0.3167701863354037],\n" +
            "            \"action\": [0, 0, 0, 0, 0, 1, 0, 0, 0]\n" +
            "        }]\n" +
            "    }, {\n" +
            "        \"id\": 4,\n" +
            "        \"span\": {\n" +
            "            \"start\": 0.8086323302469136,\n" +
            "            \"loopStart\": 0.8333333333333334,\n" +
            "            \"end\": 0.8930041152263375\n" +
            "        },\n" +
            "        \"events\": [{\n" +
            "            \"block\": [0, 0, 1, 1],\n" +
            "            \"action\": [0, 1, 0, 0, 0, 0, 0, 0, 0]\n" +
            "        }]\n" +
            "    }]\n" +
            "}";

    static Monitoring fromJson() {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, Monitoring.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
