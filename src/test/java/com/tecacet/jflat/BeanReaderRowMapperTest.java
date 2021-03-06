/*
 Copyright 2005  TecaceT Ltd.

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

package com.tecacet.jflat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.tecacet.jflat.conversion.DataConverter;
import com.tecacet.jflat.introspection.DefaultBeanFactory;
import com.tecacet.jflat.introspection.jodd.JoddPropertyAccessor;
import com.tecacet.jflat.om.Customer;
import com.tecacet.jflat.om.Order;

public class BeanReaderRowMapperTest {

	@Test
	public void testGetRow() {

		ReaderRowMapper<Customer> rowMapper = new BeanReaderRowMapper<Customer>(Customer.class,
				new String[] { "firstName", "lastName", "identifier" });
		String[] row = { "John", "Doh", "123" };
		Customer customer = rowMapper.getRow(row, 1);
		assertEquals(123, customer.getIdentifier());
		assertEquals("John", customer.getFirstName());
		assertEquals("Doh", customer.getLastName());
	}

	@Test
	public void testGetSimpleRow() {

		BeanReaderRowMapper<Order> mapper = new BeanReaderRowMapper<Order>(Order.class,
				new String[] { "number", "price", "quantity" }, new String[] { "Number", "Price", "Quantity" });
		assertEquals(DefaultBeanFactory.class, mapper.getBeanFactory().getClass());
		assertEquals(HeaderColumnMapping.class, mapper.getColumnMapping().getClass());
		assertEquals(JoddPropertyAccessor.class, mapper.getPropertyAccessor().getClass());
		assertEquals(1, mapper.getHeaderRowIndex());
		
		// First row gets the header
		Order order = mapper.getRow(new String[] { "Number", "Price", "Quantity" }, 1);
		assertNull(order);
		String[] row = new String[] { "911", "12.5", "100" };
		order = mapper.getRow(row, 2);
		assertEquals(100, order.getQuantity());
		assertEquals("911", order.getNumber());
		assertEquals(12.5, order.getPrice(), 0.0001);
		assertNull(order.getCustomer());

	}

	@Test
	public void testGetNestetRow() {
		BeanReaderRowMapper<Order> mapper = new BeanReaderRowMapper<Order>(Order.class,
				new String[] { "number", "price", "quantity", "customer.name" },
				new String[] { "Number", "Price", "Quantity", "Customer" });

		// First row gets the header
		Order order = mapper.getRow(new String[] { "Number", "Price", "Quantity", "Customer" }, 1);
		assertNull(order);
		String[] row = new String[] { "911", "12.5", "100", "Jack" };
		order = mapper.getRow(row, 2);
		assertEquals(100, order.getQuantity());
		assertEquals("911", order.getNumber());
		assertEquals(12.5, order.getPrice(), 0.0001);
		assertNotNull(order.getCustomer());
	}

	@Test
	public void testConverter() {
		BeanReaderRowMapper<Order> mapper = new BeanReaderRowMapper<Order>(Order.class,
				new String[] { "number", "price", "quantity", "customer.name" },
				new String[] { "Number", "Price", "Quantity", "Customer" });

		mapper.getConverterRegistry().registerConverter("price", new DataConverter<String, Double>() {

			@Override
			public Double convert(String value) {
				value = value.substring(value.indexOf("$") + 1);
				return Double.parseDouble(value);
			}
		});

		// First row gets the header
		Order order = mapper.getRow(new String[] { "Number", "Price", "Quantity", "Customer" }, 1);
		assertNull(order);
		String[] row = new String[] { "911", "$12.5", "100", "Jack" };
		order = mapper.getRow(row, 2);
		assertEquals(100, order.getQuantity());
		assertEquals("911", order.getNumber());
		assertEquals(12.5, order.getPrice(), 0.0001);
		assertNotNull(order.getCustomer());
	}

}
