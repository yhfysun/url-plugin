package com.ziyan.urlplugin.mapper;


import com.ziyan.urlplugin.entity.NodeUrlAndApiRelation;
import org.apache.ibatis.annotations.Insert;

public interface NodeUrlAndApiRelationMapper {
	
	@Insert("INSERT INTO t_node_url_and_api_relation (node_url, api, description, server_name) VALUES (#{nodeUrl}, #{api}, #{description}, #{serverName})")
	int insert(NodeUrlAndApiRelation entity);
}
