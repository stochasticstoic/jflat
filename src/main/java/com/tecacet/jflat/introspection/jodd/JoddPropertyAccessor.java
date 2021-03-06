package com.tecacet.jflat.introspection.jodd;

import com.tecacet.jflat.introspection.PropertyAccessor;

import jodd.bean.BeanUtil;

public class JoddPropertyAccessor<T> implements PropertyAccessor<T> {

	@Override
	public Object getProperty(T bean, String propertyName) {
		return BeanUtil.getPropertySilently(bean, propertyName);
	}

	@Override
	public void setProperty(T bean, String propertyName, Object value) {
		BeanUtil.setPropertyForcedSilent(bean, propertyName, value);
	}

}
