package yx.graduation.elec.pojo.vo;

import lombok.Data;

@Data
public class MessageVo {
    private String host;
    private String deviceId;
    private String msg;
    private String parameter;
}
