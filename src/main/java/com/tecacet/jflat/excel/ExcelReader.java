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
package com.tecacet.jflat.excel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.tecacet.jflat.AbstractTabularDataReader;
import com.tecacet.jflat.BeanReaderRowMapper;
import com.tecacet.jflat.ReaderRowMapper;
import com.tecacet.jflat.TabularDataReaderCallback;

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
public class ExcelReader<T> extends AbstractTabularDataReader<T> {

	private NumberFormat numberFormat = new DecimalFormat("#.#####");
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public ExcelReader(ReaderRowMapper<T> mapper) throws IOException {
		super(mapper);
	}
	
	public ExcelReader(Class<T> type, String[] properties, String[] columns) {
		super(new BeanReaderRowMapper<>(type, properties, columns));
	}

	/**
	 * Navigate the flat file and invoke the callback for each row.
	 * 
	 * @param callback
	 * @throws IOException
	 */
	@Override
	public void readWithCallback(InputStream is, TabularDataReaderCallback<T> callback) throws IOException {
		Workbook wb;
		try {
			wb = WorkbookFactory.create(is);
		} catch (InvalidFormatException e) {
			throw new IOException(e);
		}
		Sheet currentSheet = wb.getSheetAt(0);
		readSheet(currentSheet, callback);

	}

	protected void readSheet(Sheet sheet, TabularDataReaderCallback<T> callback) {
		for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (rowIndex < skipLines + sheet.getFirstRowNum()) {
				continue;
			}
			String[] tokens = readRow(row);
			T bean = rowMapper.getRow(tokens, rowIndex + 1);
			callback.processRow(rowIndex, tokens, bean);
		}
	}

	private String[] readRow(Row row) {
		List<String> tokens = new ArrayList<String>();
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			String cellValue = "";
			if (cell != null) {
				cellValue = getCellContentAsString(cell);
			}
			tokens.add(cellValue);
		}
		return tokens.toArray(new String[tokens.size()]);
	}

	// TODO toString conversions should respect registered converters
	private String getCellContentAsString(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().getString();
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				return dateFormat.format(date);
			} else {
				double d = cell.getNumericCellValue();
				// TODO find a flexible enough format for all numeric types
				return numberFormat.format(d);
				// return Double.toString(d);
			}
		case Cell.CELL_TYPE_BOOLEAN:
			boolean b = cell.getBooleanCellValue();
			return Boolean.toString(b);
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_ERROR:
			byte bt = cell.getErrorCellValue();
			return Byte.toString(bt);
		default:
			return cell.getStringCellValue();

		}
	}

	@Override
	protected void readWithCallback(BufferedReader br, TabularDataReaderCallback<T> callback) throws IOException {
		throw new UnsupportedOperationException();
	}



}
