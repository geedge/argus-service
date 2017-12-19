package com.nis.web.dao;

import com.nis.domain.restful.EncryptProtoRandom;
@MyBatisDao
public interface EncryptProtoRandomDao extends CrudDao<EncryptProtoRandom>{
    int delete(Long id);
    int insert(EncryptProtoRandom record);
    int update(EncryptProtoRandom record);
}