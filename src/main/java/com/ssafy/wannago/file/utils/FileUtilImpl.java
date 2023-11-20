package com.ssafy.wannago.file.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.ssafy.wannago.media.model.MediaDto;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;


@Slf4j
public class FileUtilImpl implements FileUtil{
	
	private MultipartFile[] files;
	private String uploadPath;
	private String videoThumbPath;
	private String imageThumbPath;
	private int imageThumbRatio;
	
	public FileUtilImpl(MultipartFile[] files,String uploadPath,String videoThumbPath,String imageThumbPath,int imageThumbRatio) {
		this.files=files;
		this.uploadPath=uploadPath;
		this.videoThumbPath=videoThumbPath;
		this.imageThumbPath=imageThumbPath;
		this.imageThumbRatio=imageThumbRatio;
	}
	
	@Override
	public List<MediaDto> saveFiles(int conceptNo) throws Exception {
		long beforeTime = System.currentTimeMillis(); 
        log.debug("start time:"+beforeTime);
        long fileSize=0;
        List<MediaDto> mediaList=new ArrayList<>();
        for(MultipartFile file:files) {
            if(file.isEmpty()) {
                continue;
            }
            String originalFileName = file.getOriginalFilename();
            if(originalFileName.isEmpty()) {
                continue;
            }
            String mediaType=file.getContentType().split("/")[0];
            log.debug("file mime type:"+mediaType);
            if(!("video".equals(mediaType)) && !("image".equals(mediaType))) {
            	continue;
            }
            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = this.uploadPath + File.separator + today;
            File folder = new File(saveFolder);
            if (!folder.exists())
                folder.mkdirs();
            String extension=originalFileName.substring(originalFileName.lastIndexOf('.')+1).toLowerCase();
            String saveFileName = UUID.randomUUID().toString()
                    +"."+extension;
            log.debug("saveFolder: "+folder);
            file.transferTo(new File(folder, saveFileName));
            
            mediaList.add(new MediaDto(conceptNo,today,originalFileName,saveFileName,mediaType));
            fileSize+=file.getSize();
        }
        log.debug("total saved size"+fileSize);
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        log.debug("end time:"+afterTime);
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        log.debug("시간차이(m) : "+secDiffTime);
        
        return mediaList;
	}
	@Override
	public void generateVideoThumbnail(MediaDto media) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateImageThumbnail(MediaDto media) throws Exception {
		String thumbSaveFolder = this.imageThumbPath + File.separator + media.getMediaSaveFolder();
        File folder = new File(thumbSaveFolder);
        if (!folder.exists())
            folder.mkdirs();
        log.debug(folder.toString());
        
		File thumbnailFile = new File(folder,media.getMediaSaveFile());
		File saveFile=new File(uploadPath+File.separator+media.getSavePath());
		
        BufferedImage bo_img = ImageIO.read(saveFile);
        
        double ratio = 3;
        int width = (int) (bo_img.getWidth() / ratio);
        int height = (int) (bo_img.getHeight() / ratio);

        Thumbnails.of(saveFile)
                .size(width, height)
                .toFile(thumbnailFile);
	}
}
