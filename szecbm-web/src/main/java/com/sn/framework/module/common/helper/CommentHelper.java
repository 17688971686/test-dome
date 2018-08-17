package com.sn.framework.module.common.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sn.framework.core.common.Validate;
import com.sn.framework.core.util.BeanCopierUtils;
import com.sn.framework.module.common.domain.Comment;
import com.sn.framework.module.common.model.CommentDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/12 0012.
 */
public class CommentHelper {

    private List<Comment> commentList;

    protected CommentHelper(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public static CommentHelper create(List<Comment> commentList) {
        return new CommentHelper(commentList);
    }

    public List<CommentDto> listTransToDto(){
        List<CommentDto> commentDtoList = Lists.newArrayList();
        if (Validate.isList(commentList)) {
            commentDtoList = commentList.stream().map(item->{
                CommentDto commentDto = new CommentDto();
                BeanCopierUtils.copyPropertiesIgnoreProps(item,commentDto);
                return commentDto;
            }).collect(Collectors.toList());
        }
        return commentDtoList;
    }

    public CommentHelper filterNew() {
        List<Comment> newCommentList = Lists.newArrayList();
        if (Validate.isList(commentList)) {
            Map<String,String> cacheMap = Maps.newHashMap();
            for(Comment cm : commentList){
                if("1".equals(cm.getSingle())){     //单个处理环节
                    if(cacheMap.containsKey(cm.getNodeKey())){
                        continue;
                    }else{
                        newCommentList.add(cm);
                        cacheMap.put(cm.getNodeKey(),cm.getNodeKey());
                    }
                }else if("0".equals(cm.getSingle())){   //多人处理环节
                    String key = cm.getNodeKey();
                    if("1".equals(cm.getAgent())){      //如果是代签收，则取实际签收人
                        key += cm.getSignUserName();
                    }else{
                        key += cm.getCreateName();
                    }
                    if(cacheMap.containsKey(key)){
                        continue;
                    }else{
                        newCommentList.add(cm);
                        cacheMap.put(key,cm.getNodeKey());
                    }
                }
            }
            commentList = newCommentList;
        }
        return this;
    }
}
