package com.trueid.elasticsearch.model;

import lombok.Data;

@Data
public class Root {
	
	public int took;
    public boolean timed_out;
    public Shards _shards;
    public Hits hits;

}
