package com.example.flexsim_simulation_service.util;

import java.io.File;
import java.nio.file.Paths;

public class FlexsimPathUtil {
    public static String getRootDir(){
        return new File(System.getProperty("user.dir")).getParent();
    }

    public static String getFlexsimModelPath(){
        return Paths.get(getRootDir(), "flexsim", "model", "Koge Model.fsm").toString();
    }

    public static String getFlexsimStartUpScriptPath(){
        return Paths.get(getRootDir(), "flexsim", "script", "script.txt").toString();
    }
}
