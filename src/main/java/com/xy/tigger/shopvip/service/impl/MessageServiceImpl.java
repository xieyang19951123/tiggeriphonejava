package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.MessageDao;
import com.xy.tigger.shopvip.entity.MessageEntity;
import com.xy.tigger.shopvip.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {
}
