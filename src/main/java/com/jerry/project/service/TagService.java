package com.jerry.project.service;


import com.jerry.project.vo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> ListTag();

    List<Tag> ListTag(String ids);

    List<Tag> ListTagTop(Integer size);

    Tag updateTag(Long id, Tag tag);

    void delete(Long id);

    void setPublishedCount(Long id);

    int hasBlogs(Long id);
}
