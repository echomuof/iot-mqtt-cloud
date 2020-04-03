package yx.graduation.elec.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import yx.graduation.elec.mapper.DataRecordMapper;
import yx.graduation.elec.pojo.DataRecord;
import yx.graduation.elec.pojo.vo.MessageVo;
import yx.graduation.elec.service.DataRecordService;
import yx.graduation.enums.RedisKeyEnum;
import yx.graduation.utils.RedisOperator;

import java.util.Date;

@Service
public class DataRecordServiceImpl implements DataRecordService {

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private DataRecordMapper dataRecordMapper;

    /**
     * 将接收到的消息存储到db中
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void storeMessage(MessageVo messageVo) throws Exception {
        String deviceId = messageVo.getDeviceId();
        int value = Integer.parseInt(messageVo.getMsg());
        String parameterName = messageVo.getParameter();
        DataRecord dataRecord = new DataRecord();

        String deviceName = (String) this.redisOperator.get(RedisKeyEnum.DEVICE_KV.value + ":" + deviceId);
        String unit = (String) this.redisOperator.get(RedisKeyEnum.PARAM_KV.value + ":" + parameterName);

        if (StringUtils.isBlank(deviceName) || StringUtils.isBlank(unit)) {
            throw new Exception();
        }

        dataRecord.setDeviceId(deviceId);
        dataRecord.setDeviceName(deviceName);
        dataRecord.setValue(value);
        dataRecord.setParamName(parameterName);
        dataRecord.setParamUnit(unit);
        dataRecord.setTime(new Date());

        this.dataRecordMapper.insert(dataRecord);
    }
}