package com.cocos.assets.api.domain.entity;

public record AssetSearchCriteria(
        Integer limit,
        Integer offset,
        String name,
        String ticker
) {
    public AssetSearchCriteria(
            Integer limit,
            Integer offset,
            String name,
            String ticker
    ){
       this.limit = limit == null? 10:limit;
       this.offset = offset == null? 0:offset;
       this.name = name == null? "%%": "%" + name + "%";
       this.ticker =  ticker == null? "%%": "%" + ticker + "%";
    }
}
