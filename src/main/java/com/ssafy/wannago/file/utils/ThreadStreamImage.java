package com.ssafy.wannago.file.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
public class ThreadStreamImage implements Runnable{
	private File savePath;
	private File source;
	private int time;
	private int width;
	private int height;
	private String format;
	
	public ThreadStreamImage(int time,File savePath,File source,int width,int height,String format) {
		this.time=time;
		this.savePath=savePath;
		this.source=source;
		this.width=width;
		this.height=height;
		this.format=format;
	}
	@Override
	public void run() {
		log.debug("=============time: "+time+" processing Streaming thumbnail start");
		FrameGrab grab;
		try {
			grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(this.source));
			grab.seekToSecondPrecise(this.time);
			Picture thumbnail=grab.getNativeFrame();
        	BufferedImage bufferedImage = AWTUtil.toBufferedImage(thumbnail);
        	Thumbnails.of(bufferedImage)
            .size(this.width,this.height)
            .outputFormat(this.format)
            .toFile(savePath);
		} catch (IOException | JCodecException e) {
			e.printStackTrace();
		}
		log.debug("time: "+time+" processing Streaming thumbnail end===============");
   
		
	}

}
