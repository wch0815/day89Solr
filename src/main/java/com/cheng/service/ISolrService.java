package com.cheng.service;

import java.util.List;

import com.cheng.pojo.SolrData;

public interface ISolrService {

	List<SolrData> getAllData();
	
	boolean dataFromDB2Solr();


	List<SolrData> getDataFromSolrByKeyWord(String keyword, int pageNum, int pageSize);
}
