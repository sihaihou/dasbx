package com.reyco.dasbx.actuator.tools;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

public class Tools {
	private String name;
	private String version;
	private String description;
	private Map<String,Object> tools;
	
	private Tools(Builder builder) {
		Assert.notNull(builder, "Builder must not be null");
		this.name = builder.name;
		this.version = builder.version;
		this.description = builder.description;
		this.tools = builder.tools;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Map<String, Object> getTools() {
		return tools;
	}
	public void setTools(Map<String, Object> tools) {
		this.tools = tools;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public static class Builder{
		private String name;
		private String version;
		private String description;
		private Map<String,Object> tools;
		public Builder() {
			this.name = "root";
			this.version = "v1.0.0";
			this.tools = new LinkedHashMap<>();
		}

		public Builder(String name) {
			Assert.notNull(name, "name must not be null");
			this.name = name;
			this.version = "v1.0.0";
			this.description = "root";
			this.tools = new LinkedHashMap<>();
		}
		public Builder(String name, Map<String, ?> tools) {
			Assert.notNull(name, "name must not be null");
			Assert.notNull(tools, "tools must not be null");
			this.name = name;
			this.version = "v1.0.0";
			this.description = "root";
			this.tools = new LinkedHashMap<>(tools);
		}
		public Builder(String name,String version, Map<String, ?> tools) {
			Assert.notNull(name, "name must not be null");
			Assert.notNull(version, "version must not be null");
			Assert.notNull(tools, "tools must not be null");
			this.name = name;
			this.version = version;
			this.description = "root";
			this.tools = new LinkedHashMap<>(tools);
		}
		public Builder(String name,String version, String description, Map<String, ?> tools) {
			Assert.notNull(name, "name must not be null");
			Assert.notNull(version, "version must not be null");
			Assert.notNull(description, "description must not be null");
			Assert.notNull(tools, "tools must not be null");
			this.name = name;
			this.version = version;
			this.description = description;
			this.tools = new LinkedHashMap<>(tools);
		}
		public Builder withDetail(String key, Object value) {
			Assert.notNull(key, "Key must not be null");
			Assert.notNull(value, "Value must not be null");
			this.tools.put(key, value);
			return this;
		}
		public Builder withDetails(Map<String, ?> tools) {
			Assert.notNull(tools, "tools must not be null");
			this.tools.putAll(tools);
			return this;
		}
		public Builder unknown() {
			name("unKnown");
			version("unKnown");
			description("unKnown");
			return this;
		}
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		public Builder version(String version) {
			this.version = version;
			return this;
		}
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		public Builder tools(Map<String, ?> tools) {
			this.tools = new LinkedHashMap<>(tools);
			return this;
		}
		public Tools build() {
			return new Tools(this);
		}
	} 
}
