package com.cheng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolrData {

	private String id;
	private String title;
	private long price;
	private String sellPoint;
	private String image;
	private String catName;
	private String itemDesc;
}
