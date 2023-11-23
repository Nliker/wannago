package com.ssafy.wannago.file.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.ssafy.wannago.media.model.MediaDto;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVAudioAttributes;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.IVVideoAttributes;
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
	private String thumbnailImageFormat;
	private String streamingThumbnailPath;
	
	public FileUtilImpl(MultipartFile[] files
			,String uploadPath
			,String videoResizePath
			,String imageThumbPath
			,int imageThumbRatio
			,String thumbnailImageFormat
			,String streamingThumbnailPath) {
		this.files=files;
		this.uploadPath=uploadPath;
		this.videoResizePath=videoResizePath;
		this.imageThumbPath=imageThumbPath;
		this.imageThumbRatio=imageThumbRatio;
		this.thumbnailImageFormat=thumbnailImageFormat;
		this.streamingThumbnailPath=streamingThumbnailPath;
	}
	
	@Override
	public List<MediaDto> saveFiles(int conceptNo) throws Exception {
		log.debug("===================save files start===========================");
		long beforeTime = System.currentTimeMillis(); 
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
            File folder = new File(this.uploadPath + File.separator + today);
            
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
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        log.debug("start:"+beforeTime+" | "+"end: "+afterTime+" | "+" processTime: "+ secDiffTime+" | "+" total save size: "+fileSize);
        log.debug("===================save files end===========================");
        return mediaList;
	}
	@Override
	public void resizeVideo(MediaDto media) throws Exception {
		log.debug("==========================resize video start=======================");
		long beforeTime = System.currentTimeMillis(); 
		
		IVCompressor compressor = new IVCompressor();
        File folder = new File(this.videoResizePath + File.separator + media.getMediaSaveFolder());
        if (!folder.exists()) {
        	folder.mkdirs();
        }
        
		File saveFile=new File(uploadPath+File.separator+media.getSavePath());
		IVSize customRes = new IVSize();
		customRes.setWidth(480);
		customRes.setHeight(640);
		IVVideoAttributes videoAttribute = new IVVideoAttributes();
		videoAttribute.setFrameRate(24);
		videoAttribute.setSize(customRes);
		
		byte[] result=compressor.encodeVideoWithAttributes(Files.readAllBytes(saveFile.toPath()), VideoFormats.MP4,null, videoAttribute);
		
		File resizeFile = new File(folder,media.getMediaSaveFile());
        Files.write(resizeFile.toPath(), result);  

        File thumbNailFolder = new File(this.imageThumbPath + File.separator + media.getMediaSaveFolder());
        
        if (!thumbNailFolder.exists())
        	thumbNailFolder.mkdirs();

		File thumbnailFile = new File(thumbNailFolder,media.getFileNameWithoutExtension()+"."+this.thumbnailImageFormat);
		log.debug(thumbnailFile.toString());
        generateVideoThumbnail(resizeFile,thumbnailFile);
        generateVideoStreamingThumbnail(saveFile,media);
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        log.debug("start:"+beforeTime+" | "+"end: "+afterTime+" | "+" processTime: "+ secDiffTime);
        log.debug("===================resize video end===========================");
       
	}

	@Override
	public void generateImageThumbnail(MediaDto media) throws Exception {
		log.debug("==========================generateImageThumbnail start=======================");
		long beforeTime = System.currentTimeMillis(); 
		
        File folder = new File(this.imageThumbPath + File.separator + media.getMediaSaveFolder());
        if (!folder.exists())
            folder.mkdirs();
        
		File saveFile=new File(uploadPath+File.separator+media.getSavePath());
		File thumbnailFile = new File(folder,media.getFileNameWithoutExtension()+"."+this.thumbnailImageFormat);
		
        BufferedImage bo_img = ImageIO.read(saveFile);
        
        int width = (int) (bo_img.getWidth() / this.imageThumbRatio);
        int height = (int) (bo_img.getHeight() / this.imageThumbRatio);

        Thumbnails.of(saveFile)
                .size(width, height)
                .outputFormat(this.thumbnailImageFormat)
                .toFile(thumbnailFile);
        
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        log.debug("start:"+beforeTime+" | "+"end: "+afterTime+" | "+" processTime: "+ secDiffTime);
        log.debug("===================generateImageThumbnail end===========================");
	}
	
	private void generateVideoThumbnail(File source, File thumbnail) throws Exception {
		int frameNumber = 0;
		Picture picture = FrameGrab.getFrameFromFile(source, frameNumber);
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
		ImageIO.write(bufferedImage,this.thumbnailImageFormat, thumbnail);
	}
	
	private void generateVideoStreamingThumbnail(File source,MediaDto media) throws Exception{
		log.debug("==============generateVideoStreamingThumbnail===============");
		long beforeTime = System.currentTimeMillis(); 
		
		FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(source));
        int durationInSeconds = (int)grab.getVideoTrack().getMeta().getTotalDuration();

        Picture thumbnail=grab.getNativeFrame();
        BufferedImage bufferedImage = AWTUtil.toBufferedImage(thumbnail);
        
        int width= (int) (bufferedImage.getWidth()/this.imageThumbRatio);
        int height= (int) (bufferedImage.getHeight()/this.imageThumbRatio);
        
        File folder=new File(streamingThumbnailPath+File.separator+media.getFileNameWithoutExtension());
        if (!folder.exists())
            folder.mkdirs();
        
        ArrayList<Thread> threadList=new ArrayList<Thread>();
        log.info("Video length: {} seconds", durationInSeconds); 
        for(int time=0;time<=durationInSeconds;time++) {
        	File saveThumbnailPath=new File(folder,time+"."+this.thumbnailImageFormat);
            Runnable r = new ThreadStreamImage(time,saveThumbnailPath,source,width,height,this.thumbnailImageFormat);
            Thread thread = new Thread(r);   
            threadList.add(thread);
            thread.start();
        }
        for(Thread thread:threadList) {
        	thread.join();
        }
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        log.debug("start:"+beforeTime+" | "+"end: "+afterTime+" | "+" processTime: "+ secDiffTime);
        log.debug("===================generateImageThumbnail end===========================");
	}
}
