package com.example.gav.flickrclient.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Photos{

	@SerializedName("perpage")
	private int perpage;

	@SerializedName("total")
	private int total;

	@SerializedName("pages")
	private int pages;

	@SerializedName("photo")
	private List<PhotoItem> photo;

	@SerializedName("page")
	private int page;

	public void setPerpage(int perpage){
		this.perpage = perpage;
	}

	public int getPerpage(){
		return perpage;
	}

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setPages(int pages){
		this.pages = pages;
	}

	public int getPages(){
		return pages;
	}

	public void setPhoto(List<PhotoItem> photo){
		this.photo = photo;
	}

	public List<PhotoItem> getPhoto(){
		return photo;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	@Override
 	public String toString(){
		return 
			"Photos{" + 
			"perpage = '" + perpage + '\'' + 
			",total = '" + total + '\'' + 
			",pages = '" + pages + '\'' + 
			",photo = '" + photo + '\'' + 
			",page = '" + page + '\'' + 
			"}";
		}
}