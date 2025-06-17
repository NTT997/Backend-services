package com.salesmanager.shop.store.facade.category;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class CategoryListRequest {
	@ApiModelProperty(value = "List of filters, e.g., featured, visible", required = false)
    private List<String> filter;

    @ApiModelProperty(value = "Category name", required = false)
    private String name;

    @ApiModelProperty(value = "Page number, default is 0", required = false)
    private Integer page;

    @ApiModelProperty(value = "Number of items per page, default is 10", required = false)
    private Integer count;

    public List<String> getFilter() {
        return filter;
    }

    public void setFilter(List<String> filter) {
        this.filter = filter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
