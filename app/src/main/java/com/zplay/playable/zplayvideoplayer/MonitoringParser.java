package com.zplay.playable.zplayvideoplayer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MonitoringParser {
    static final private String json = "{\n" +
            "    \"videoDuration\": 31.2,\n" +
            "    \"ctrls\": [{\n" +
            "        \"id\": 0,\n" +
            "        \"span\": {\n" +
            "            \"start\": 0.056572788065843614,\n" +
            "            \"loopStart\": 0.1337448559670782,\n" +
            "            \"end\": 0.22016460905349794\n" +
            "        },\n" +
            "        \"events\": [{\n" +
            "            \"block\": [0, 0, 1, 0.4625832093828485],\n" +
            "            \"action\": [0, 0, 0, 0, 1, 0, 0, 0, 0]\n" +
            "        }]\n" +
            "    }, {\n" +
            "        \"id\": 1,\n" +
            "        \"span\": {\n" +
            "            \"start\": 0.2561612654320988,\n" +
            "            \"loopStart\": 0.31584362139917693,\n" +
            "            \"end\": 0.4495884773662551\n" +
            "        },\n" +
            "        \"events\": [{\n" +
            "            \"block\": [0, 0.5103021140669577, 1, 1],\n" +
            "            \"action\": [0, 0, 0, 0, 1, 0, 0, 0, 0]\n" +
            "        }]\n" +
            "    }, {\n" +
            "        \"id\": 2,\n" +
            "        \"span\": {\n" +
            "            \"start\": 0.5720048868312757,\n" +
            "            \"loopStart\": 0.6368312757201646,\n" +
            "            \"end\": 0.8930041152263375\n" +
            "        },\n" +
            "        \"events\": [{\n" +
            "            \"block\": [0, 0, 1, 0.47350710784911365],\n" +
            "            \"action\": [0, 0, 0, 0, 1, 0, 0, 0, 0]\n" +
            "        }]\n" +
            "    }]\n" +
            "}";

    static Monitoring fromeJson() {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, Monitoring.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
