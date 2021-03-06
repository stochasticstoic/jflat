/*
 Copyright 2008 TecAceT Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.tecacet.jflat.introspection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.tecacet.jflat.introspection.jodd.JoddPropertyAccessor;
import com.tecacet.jflat.om.Order;
import com.tecacet.jflat.om.StockPrice;

public class JoddPropertyAccessorTest {

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void testGetterOnly() {
		PropertyAccessor<StockPrice> accessor = new JoddPropertyAccessor<StockPrice>();
		StockPrice stockPrice = new StockPrice();
		accessor.setProperty(stockPrice, "openPrice", 12.0);
		accessor.setProperty(stockPrice, "closePrice", 10.0);
		assertEquals(11.0, accessor.getProperty(stockPrice, "averagePrice"));
	}

	@Test
	public void testJoddAccessor() {
		PropertyAccessor<Order> accessor = new JoddPropertyAccessor<Order>();
		testSetProperty(accessor);
		testSetNullProperty(accessor);
	}

	private void testSetProperty(PropertyAccessor<Order> accessor)  {
		Order bean = new Order();

		accessor.setProperty(bean, "number", "1234");
		assertEquals("1234", bean.getNumber());
		assertEquals("1234", accessor.getProperty(bean, "number"));
		accessor.setProperty(bean, "quantity", "10");
		assertEquals(10, bean.getQuantity());
		assertEquals(10, accessor.getProperty(bean, "quantity"));
		accessor.setProperty(bean, "price", "15.99");
		assertEquals(15.99, bean.getPrice(), 0.0001);
		assertEquals(15.99, (Double) accessor.getProperty(bean, "price"), 0.001);
		accessor.setProperty(bean, "active", "true");
		assertTrue(bean.isActive());
		assertEquals(true, (Boolean) accessor.getProperty(bean, "active"));
		GregorianCalendar c = new GregorianCalendar(2007, Calendar.JULY, 4);
		accessor.setProperty(bean, "date", dateFormat.format(c.getTime()));
		assertEquals(c.getTime(), bean.getDate());
		assertEquals(c.getTime(), accessor.getProperty(bean, "date"));

		accessor.setProperty(bean, "customer.firstName", "James");

		assertNotNull(accessor.getProperty(bean, "customer.firstName"));

		accessor.setProperty(bean, "customer.firstName", "James");
		assertEquals("James", bean.getCustomer().getFirstName());
		assertEquals("James", accessor.getProperty(bean, "customer.firstName"));
	}

	private void testSetNullProperty(PropertyAccessor<Order> accessor)  {
		Order bean = new Order();

		accessor.setProperty(bean, "account", null);
		assertNull(bean.getNumber());
		assertNull(accessor.getProperty(bean, "account"));
		accessor.setProperty(bean, "date", null);
		assertNull(bean.getDate());

		accessor.setProperty(bean, "quantity", null);
		assertEquals(0, bean.getQuantity());
		assertEquals(0, accessor.getProperty(bean, "quantity"));
		accessor.setProperty(bean, "price", null);
		assertEquals(0.0, bean.getPrice(), 0.001);
		assertEquals(0.0, (Double) accessor.getProperty(bean, "price"), 0.001);
		accessor.setProperty(bean, "active", null);
		assertFalse(bean.isActive());
		assertEquals(false, accessor.getProperty(bean, "active"));

	}
}
