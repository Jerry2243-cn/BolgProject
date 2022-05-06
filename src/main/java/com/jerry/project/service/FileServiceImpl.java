package com.jerry.project.service;

import com.jerry.project.config.MapPath;
import com.jerry.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private MapPath mapPath;
    @Override
    public String saveFile(MultipartFile file) {
        String dirPath = mapPath.getPath();
        File filePath = new File(dirPath);
        if(!filePath.exists()){
            filePath.mkdirs();
        }
        // 对上传的文件重命名，避免文件重名
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."));
        try {
            // 文件保存
            file.transferTo(new File(dirPath+newName));
            System.out.println(dirPath+newName);
        } catch (IOException e) {

        }
        return mapPath.getUrl()+newName;
    }

    @Override
    public String saveFile(MultipartFile file,String oldFilePath) {
        deleteFile(oldFilePath);
       return saveFile(file);
    }
    @Override
    public void deleteFile(String path){
        if("".equals(path) || path == null){
            return;
        }
        String[] s = path.split("/");
        String fileName = s[s.length-1];
        File file = new File(mapPath.getPath()+fileName);
        file.delete();
    }
}
