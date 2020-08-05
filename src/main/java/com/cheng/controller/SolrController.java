package com.cheng.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;

import com.mysql.jdbc.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cheng.pojo.SolrData;
import com.cheng.service.ISolrService;

@RestController
@RequestMapping("/path")
public class SolrController {

    @Resource
    private ISolrService solrService;

    @GetMapping("/SolrDatas")
    public List<SolrData> getAllData() {
        System.out.println("12312312");
        return solrService.getAllData();
    }

    @GetMapping("/dataFromDB2Solr")
    public boolean dataFromDB2Solr() {
        return solrService.dataFromDB2Solr();
    }

    @GetMapping("/search")
    public List<SolrData> getDatasFromSolr(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(value = "page", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) throws UnsupportedEncodingException {

        if (!StringUtils.isNullOrEmpty(keyword)) {
            keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
        }

        return solrService.getDataFromSolrByKeyWord(keyword, pageNum, pageSize);
    }
}
