package com.ssafy.wannago.file.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.ssafy.wannago.media.model.MediaDto;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;


@Slf4j
public class FileUtilImpl implements FileUtil{
	
	private MultipartFile[] files;
	private String uploadPath;
	private String videoResizePath;
	private String imageThumbPath;
	private int imageThumbRatio;
	
	public FileUtilImpl(MultipartFile[] files,String uploadPath,String videoResizePath,String imageThumbPath,int imageThumbRatio) {
		this.files=files;
		this.uploadPath=uploadPath;
		this.videoResizePath=videoResizePath;
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
	public void resizeVideo(MediaDto media) throws Exception {
		IVCompressor compressor = new IVCompressor();
		String videoSaveFolder = this.videoResizePath + File.separator + media.getMediaSaveFolder();
        File folder = new File(videoSaveFolder);
        if (!folder.exists())
            folder.mkdirs();
        log.debug(folder.toString());
        
		File resizeFile = new File(folder,media.getMediaSaveFile());
		File saveFile=new File(uploadPath+File.separator+media.getSavePath());
		
		
		long beforeTime = System.currentTimeMillis(); 
		log.debug("==========================resize video start=======================");
        byte[] result=compressor.reduceVideoSize(Files.readAllBytes(saveFile.toPath()), VideoFormats.MP4, ResizeResolution.R480P);
        Files.write(resizeFile.toPath(), result);
        
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        log.debug("end time:"+afterTime);
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        log.debug("시간차이(m) : "+secDiffTime);     
        
        String thumbSaveFolder = this.imageThumbPath + File.separator + media.getMediaSaveFolder();
        File thumbnailfolder = new File(thumbSaveFolder);
        if (!folder.exists())
        	thumbnailfolder.mkdirs();
        log.debug(folder.toString());
        String IMAGE_PNG_FORMAT="png";
        String FileName=media.getMediaSaveFile();
		File thumbnailFile = new File(thumbnailfolder,(FileName.substring(0,FileName.lastIndexOf('.'))+"."+IMAGE_PNG_FORMAT));
		beforeTime = System.currentTimeMillis(); 
		log.debug("==========================resize video start=======================");
        generateVideoThumbnail(resizeFile,thumbnailFile,IMAGE_PNG_FORMAT);
        afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        log.debug("end time:"+afterTime);
        secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        log.debug("시간차이(m) : "+secDiffTime);     
       
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
        int width = (int) (bo_img.getWidth() / this.imageThumbRatio);
        int height = (int) (bo_img.getHeight() / this.imageThumbRatio);

        Thumbnails.of(saveFile)
                .size(width, height)
                .toFile(thumbnailFile);
	}
	
	private void generateVideoThumbnail(File source, File thumbnail,String IMAGE_PNG_FORMAT) throws Exception {
		log.debug("extracting thumbnail from video");
		int frameNumber = 0;
		Picture picture = FrameGrab.getFrameFromFile(source, frameNumber);
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
		ImageIO.write(bufferedImage, IMAGE_PNG_FORMAT, thumbnail);
	}
}