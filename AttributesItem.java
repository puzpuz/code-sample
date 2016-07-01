package com.puz.salestakingorder.models;

public class AttributesItem {
	private long id;
	private String name,type,status,description;
	public AttributesItem(){}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public boolean equals(Object object) {
		boolean result = false;
	    if (object == null || object.getClass() != getClass()) {
	      result = false;
	    } else {
	      AttributesItem item = (AttributesItem) object;
	      if (this.getId() == item.getId()) {
	        result = true;
	      }
	    }
	    return result;
	}
	@Override
	public int hashCode() {
		int hash = 3;
	    hash = 7 * hash + String.valueOf(this.id).hashCode();
	    return hash;
	}
	
}
