package com.mljr.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GpsDeviceInfo {
	@JSONField(name = "gps_id")
	private String gpsId;
	private String city;
	private String[] status;
	@JSONField(name = "black_area")
	private String blackArea;
}
