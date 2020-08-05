package com.cheng.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cheng.dao.ISolrDao;
import com.cheng.pojo.SolrData;

@Service
public class SolrServiceImpl implements ISolrService {
	
	@Resource
	private ISolrDao solrDao;
	
	@Value("${solr_server_url}")
	private String baseURL;

	@Override
	public List<SolrData> getAllData() {
		return solrDao.getAllData();
	}

    /**
     * 将数据库中的数据存入solr服务器中
     * @return
     */
	@Override
	public boolean dataFromDB2Solr() {
		List<SolrData> list = getAllData();

		System.out.println(baseURL);
		HttpSolrServer server = new  HttpSolrServer(baseURL);

		SolrInputDocument document = null;

		try {
			for (SolrData sd : list) {
				document = new SolrInputDocument();

				document.setField("id", sd.getId());
				document.setField("item_title", sd.getTitle());
				document.setField("item_price", sd.getPrice());
				document.setField("item_sell_point", sd.getSellPoint());
				document.setField("item_image", sd.getImage());
				document.setField("item_category_name", sd.getCatName());
				document.setField("item_desc", sd.getItemDesc());

//				System.out.println(document);
				server.add(document);
			}
			System.out.println(document);

			server.commit();

			return true;
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public List<SolrData> getDataFromSolrByKeyWord(String keyword, int pageNum, int pageSize) {
		System.out.println(keyword + "\t" + pageNum + "\t" + pageSize);

		HttpSolrServer server = new HttpSolrServer(baseURL);

		SolrQuery query = new SolrQuery();

		try {
			if(StringUtils.isEmpty(keyword)){
				query.setQuery("*:*");
			}else{
				query.setQuery(keyword);
			}

			//	设定默认查询的搜索域
			query.set("df", "item_title");
			query.set("df", "item_sell_point");
//			query.set("df", "itemDesc");
//			query.set("df", "item_category_name");

			int start = (pageNum - 1) * pageSize;
			int rows = pageSize;

			query.set("start", start);
			query.set("rows", rows);

			QueryResponse response = server.query(query);

			SolrDocumentList list = response.getResults();

			System.out.println(list.size() + "\\\\\\\\\\\\\\");

			List<SolrData> datas = new ArrayList<>();

			SolrData s = null;

			for (SolrDocument sd : list) {

				System.out.println(sd + "00000000000");

				s = new SolrData();

				s.setId((String) sd.getFieldValue("id"));
				s.setCatName((String) sd.getFieldValue("item_category_name"));

				Object priceTemp = sd.getFieldValue("item_price");
				s.setPrice(priceTemp == null ? 0: (long)priceTemp);

				s.setImage((String) sd.getFieldValue("item_image"));
				s.setItemDesc((String) sd.getFieldValue("item_desc"));
				s.setTitle((String) sd.getFieldValue("item_title"));
				s.setSellPoint((String) sd.getFieldValue("item_sell_point"));

				datas.add(s);
			}
			return datas;
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		return null;
	}

}
