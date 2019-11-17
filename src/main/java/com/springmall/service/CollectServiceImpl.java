package com.springmall.service;

import com.springmall.bean.Collect;
import com.springmall.bean.CollectExample;
import com.springmall.mapper.CollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户管理——会员收藏——service层
 */
@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    CollectMapper collectMapper;

    @Override
    public List<Collect> queryCollectList(Integer userId, Integer valueId) {
        CollectExample collectExample = new CollectExample();
        List<Collect> collects = null;
        //进行判断userId和valueId的值是否为null
        if (userId == null && valueId == null) {
            collects = collectMapper.selectByExample(collectExample);
        } else if (userId != null &&valueId == null) {
            collectExample.createCriteria().andUserIdEqualTo(userId);
            collects = collectMapper.selectByExample(collectExample);
        } else if (valueId != null && userId == null) {
            collectExample.createCriteria().andValueIdEqualTo(valueId);
            collects = collectMapper.selectByExample(collectExample);
        }else {
            collectExample.createCriteria().andUserIdEqualTo(userId).andValueIdEqualTo(valueId);
            collects=collectMapper.selectByExample(collectExample);
        }
        return collects;
    }
}
