package com.xy.tigger.uitls;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAcessTokenUtils{

    @Autowired
    private  WxMpService wxMpService;

    public String getJsapiTicket() throws WxErrorException {
        String jsapiTicket = wxMpService.getJsapiTicket(true);
        if (jsapiTicket==null){
            //throw new BusinessException("jsapiTicket为空，检查小程序配置",-1);
        }
        return jsapiTicket;
    }
}
