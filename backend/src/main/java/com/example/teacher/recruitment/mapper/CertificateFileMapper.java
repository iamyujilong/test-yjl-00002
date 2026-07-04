
package com.example.teacher.recruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.teacher.recruitment.entity.CertificateFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CertificateFileMapper extends BaseMapper<CertificateFile> {

    @Select("SELECT cf.* FROM certificate_file cf WHERE cf.item_id = #{itemId}")
    List<CertificateFile> selectByItemId(@Param("itemId") Long itemId);
}
