
package com.example.teacher.recruitment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.teacher.recruitment.entity.CertificateItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CertificateItemMapper extends BaseMapper<CertificateItem> {

    @Select("SELECT ci.* FROM certificate_item ci WHERE ci.registration_id = #{registrationId}")
    List<CertificateItem> selectByRegistrationId(@Param("registrationId") Long registrationId);

    @Select("SELECT ci.* FROM certificate_item ci WHERE ci.registration_id = #{registrationId} AND ci.type = #{type}")
    List<CertificateItem> selectByRegistrationIdAndType(@Param("registrationId") Long registrationId, @Param("type") String type);
}
