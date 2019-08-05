package com.internship.tmontica_admin.util;

import com.internship.tmontica_admin.menu.exception.SaveImgException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

public class SaveImageFile {
    public static String saveImg(MultipartFile imgFile, String name, String location){
        // file url : imagefile/년/월/일/파일이름
        StringBuilder sb = new StringBuilder("imagefile/");
        Calendar calendar = Calendar.getInstance();
        sb.append(calendar.get(Calendar.YEAR)).append("/");
        sb.append(calendar.get(Calendar.MONTH) + 1).append("/");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH)).append("/");
        // 실제 저장되는 path
        File dirFile = new File(location + sb.toString());
        dirFile.mkdirs(); // 디렉토리가 없을 경우 만든다.

        sb.append(name).append("_").append(UUID.randomUUID().toString()); // 유일한 식별자
        // 확장자 가져오기.
        String extension = getExtensionByStringHandling(imgFile.getOriginalFilename())
                .orElseThrow(() -> new SaveImgException());
        sb.append(".").append(extension);

        String dir = sb.toString();

        try(FileOutputStream fos = new FileOutputStream(location.concat(dir));
            InputStream in = imgFile.getInputStream()){
            byte[] buffer = new byte[1024];
            int readCount = 0;
            while((readCount = in.read(buffer)) != -1){
                fos.write(buffer, 0, readCount);
            }
        }catch(Exception ex){
            throw new SaveImgException();
        }

        return dir;
    }

    // 파일 이름에서 확장자 가져오기
    public static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
