package io.choerodon.devops.infra.persistence.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.core.convertor.ConvertHelper;
import io.choerodon.devops.domain.application.entity.DevopsCheckLogE;
import io.choerodon.devops.domain.application.repository.DevopsCheckLogRepository;
import io.choerodon.devops.infra.dataobject.DevopsCheckLogDO;
import io.choerodon.devops.infra.mapper.DevopsCheckLogMapper;

@Service
public class DevopsCheckLogRepositoryImpl implements DevopsCheckLogRepository {

    @Autowired
    private DevopsCheckLogMapper devopsCheckLogMapper;

    @Override
    public void create(DevopsCheckLogE devopsCheckLogE) {
        devopsCheckLogMapper.insert(ConvertHelper.convert(devopsCheckLogE, DevopsCheckLogDO.class));
    }
}
