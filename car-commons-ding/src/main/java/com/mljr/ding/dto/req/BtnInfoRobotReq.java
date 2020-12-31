package com.mljr.ding.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @Date : 2019/3/21$ 11:17$
 * @Author : liht
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BtnInfoRobotReq {
    private String title;
    private String actionURL;
}
