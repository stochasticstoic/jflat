package com.tecacet.jflat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.tecacet.jflat.conversion.ConverterRegistry;

public abstract class AbstractTabularDataReader<T> implements TabularDataReader<T> {

	protected int skipLines = 0;

	protected ReaderRowMapper<T> rowMapper;

	protected AbstractTabularDataReader(ReaderRowMapper<T> rowMapper) {
		super();
		this.rowMapper = rowMapper;
	}
	
	protected AbstractTabularDataReader(Class<T> type, String[] properties, String[] columns) {
		this(new BeanReaderRowMapper<>(type, properties, columns));
	}
	
	@Override
	public List<T> readAll(InputStream is) throws IOException {
		final List<T> beans = new ArrayList<>();
		readWithCallback(is, new TabularDataReaderCallback<T>() {
			@Override
			public void processRow(int rowIndex, String[] tokens, T bean) {
				if (bean != null) {
					beans.add(bean);
				}
			}
		});

		return beans;
	}

	@Override
	public void readWithCallback(InputStream is, TabularDataReaderCallback<T> callback) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		try {
			readWithCallback(br, callback);
		} finally {
			isr.close();
			br.close();
		}
	}
	
	public List<T> readAll(Reader reader) throws IOException {
		BufferedReader br = new BufferedReader(reader);
		final List<T> beans = new ArrayList<>();
		try {
			readWithCallback(br, new TabularDataReaderCallback<T>() {
				@Override
				public void processRow(int rowIndex, String[] tokens, T bean) {
					if (bean != null) {
						beans.add(bean);
					}
				}
			});

			return beans;
		} finally {
			br.close();
		}
		
	}
	
	protected abstract void readWithCallback(BufferedReader br, TabularDataReaderCallback<T> callback) throws IOException;

	public int getSkipLines() {
		return skipLines;
	}

	public void setSkipLines(int skipLines) {
		this.skipLines = skipLines;
	}

	public void skipHeader() {
		setSkipLines(1);
	}

	public ReaderRowMapper<T> getRowMapper() {
		return rowMapper;
	}

	public void setRowMapper(ReaderRowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}

	@Override
	public ConverterRegistry getConverterRegistry() {
		return rowMapper.getConverterRegistry();
	}

}
