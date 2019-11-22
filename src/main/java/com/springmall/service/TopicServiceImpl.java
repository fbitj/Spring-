package com.springmall.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springmall.bean.PageRequest;
import com.springmall.bean.DataForPage;
import com.springmall.bean.Topic;
import com.springmall.bean.TopicExample;
import com.springmall.exception.DbException;
import com.springmall.mapper.TopicMapper;
import com.springmall.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    TopicMapper topicMapper;

    @Override
    public DataForPage<Topic> showListUserByPage(PageRequest request) {
        PageHelper.startPage(request.getPage(), request.getLimit());
        String sort = request.getSort();
        String order = request.getOrder();

        TopicExample example = new TopicExample();
        if (order != null && sort != null) {
            example.setOrderByClause(sort + " " + order);
        }
        TopicExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(request.getTitle())) {
            criteria.andTitleLike("%" + request.getTitle() + "%");
        }

        if (!StringUtils.isEmpty(request.getSubtitle())) {
            criteria.andSubtitleLike("%" + request.getSubtitle() + "%");
        }

        criteria.andDeletedEqualTo(false);
        List<Topic> topics = topicMapper.selectByExampleWithBLOBs(example);

        //获取总数
        PageInfo<Topic> pageInfo = new PageInfo<>(topics);
        long total = pageInfo.getTotal();
        return new DataForPage<>(total,topics);
    }

    /**
     * 新增专题
     * @param topic
     * @return
     */
    @Override
    public Topic addTopic(Topic topic) {
        //添加更新时间
        Date date = new Date();
        topic.setAddTime(date);
        topic.setUpdateTime(date);

        int i = topicMapper.insertSelective(topic);
        if (i == 0) throw new DbException();
        return topic;
    }

    /**
     * 更新信息
     * @param topic
     * @return
     */
    @Override
    public Topic updatedTopic(Topic topic) {
        topic.setUpdateTime(new Date());
        int update = topicMapper.updateByPrimaryKeySelective(topic);
        if (update == 0) throw new DbException();
        return topic;
    }

    /**
     * 删除某条专题信息
     * @param topic
     * @return
     */
    @Override
    public int deleteTopicById(Topic topic) {
        int i = topicMapper.deleteById(topic.getId(), new Date());
        if (i == 0) throw new DbException();
        return i;
    }
}
