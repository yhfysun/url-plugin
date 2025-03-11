CREATE TABLE t_node_url_and_api_relation (
     node_url VARCHAR(255) NOT NULL,
     api VARCHAR(255) NOT NULL,
     description TEXT,
     server_name VARCHAR(255),
     PRIMARY KEY (node_url, api)
);
