package com.jerry.project.util;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;

public class FilepathUtils {

    public static final String MAP_URL = "/uploadImages/";
    public static final String MAP_PATH = "/usr/file_server/images/";

    public static String getPath(Class<?> souseClass){
        ApplicationHome h = new ApplicationHome(souseClass);
        File jarF = h.getSource();
        return jarF.getParentFile().toString()+"/upload/";
    }


}
