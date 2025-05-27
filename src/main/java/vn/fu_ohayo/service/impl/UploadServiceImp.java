package vn.fu_ohayo.service.impl;

import jakarta.servlet.ServletContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.service.UploadService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class UploadServiceImp implements UploadService {

    private final ServletContext servletContext;

    public UploadServiceImp(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public String handleUploadFile(@RequestParam("file") MultipartFile file, String targetFolder) {
        // don't upload file
        if (file.isEmpty()) {
            return "";
        }
        // relative path: absolute path
        String rootPath = this.servletContext.getRealPath("/resources/images");
        String finalName = "";
        try {
            byte[] bytes = file.getBytes();

            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists())
                dir.mkdirs();

            // Create the file on server
            finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);
            // uuid - monggoDB - 100 năm mới cso khả năng trùng
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return finalName;
    }

}
