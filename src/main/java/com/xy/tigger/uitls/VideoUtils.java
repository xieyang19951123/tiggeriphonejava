package com.xy.tigger.uitls;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class VideoUtils {

    @Autowired
    private DefaultAcsClient defaultAcsClient;

    //获取视频凭证
    public   String getVideoVoucher(String videoId){
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            response =defaultAcsClient.getAcsResponse(request);
            if(!response.getPlayAuth().equals("")){
                return response.getPlayAuth();
            }
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
        return "";
    }
}
