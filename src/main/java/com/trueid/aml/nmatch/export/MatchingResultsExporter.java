package com.trueid.aml.nmatch.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVWriter;
import com.trueid.aml.nmatch.data.MatchedData;
import com.trueid.aml.nmatch.data.PosidexData;
import com.trueid.aml.nmatch.data.PythonJaroData;
import com.trueid.aml.nmatch.data.PythonLevenData;
import com.trueid.aml.nmatch.data.PythonQratioData;
import com.trueid.aml.nmatch.data.PythonSetRatioData;
import com.trueid.aml.nmatch.data.PythonSortRationData;
import com.trueid.aml.nmatch.data.RosetteData;

import lombok.Data;

public class MatchingResultsExporter {

	public String[] rosette(List<RosetteData> rData, HashMap<Long, ColumnData> colDataMap,
			ArrayList<DataRank> dataOrder, String name) {
		if (rData != null) {
			for (RosetteData p : rData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.rData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.rData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}
		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		String rosetteRow = printRow(dataOrder, colDataMap, "ROSETTE");

		rosetteRow = "ROSETTE," + name + "," + rosetteRow;
		String[] arrayRosette = rosetteRow.split(",");
		return arrayRosette;
	}

	public String[] rosetteList1(List<RosetteData> rData, HashMap<Long, ColumnData> colDataMap,
			ArrayList<DataRank> dataOrder, String name) {
		if (rData != null) {
			for (RosetteData p : rData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.rData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.rData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}
		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		String rosetteRow = printRow(dataOrder, colDataMap, "ROSETTE");

		rosetteRow = "ROSETTE-ENG," + name + "," + rosetteRow;
		String[] arrayRosette = rosetteRow.split(",");
		return arrayRosette;
	}

	public List<PosidexData> posidex(List<PosidexData> pData, HashMap<Long, ColumnData> colDataMap,
			ArrayList<DataRank> dataOrder, String name) {

		if (pData != null) {
			for (PosidexData p : pData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.pData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}
		Collections.sort(pData, new Comparator<PosidexData>() {

			@Override
			public int compare(PosidexData o1, PosidexData o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		/*
		 * String posidexRow = printRow(dataOrder, colDataMap, "POSIDEX"); // posidexRow
		 * = "POSIDEX," + name + "," + posidexRow; // posidexRow = name + "," +
		 * posidexRow; String[] arrayPosidex = posidexRow.split(","); String[] finalArry
		 * = new String[arrayPosidex.length];
		 * 
		 * String[][] arrays = new String[finalArry.length][];
		 * 
		 * 
		 * List<String[]> listArr = new ArrayList<>(); for (int i = 0; i <
		 * arrayPosidex.length; i++) { // finalArry[i] = arrayPosidex[i]; arrays[i] =
		 * new String[] { "POSIDEX," + name + "," + arrayPosidex[i] }; }
		 * 
		 * 
		 * //listArr.add(finalArry);
		 * 
		 * String[] tArry = new String[arrayPosidex.length / 3];
		 * 
		 * for (int i = 0, j = 0; i < arrayPosidex.length && j < arrayPosidex.length /
		 * 3; j++) { int c = 0; String temp = "POSIDEX,"; while (c < 3 && i <
		 * arrayPosidex.length) { c++; temp = temp+" "+ arrayPosidex[i]+" "; i++; }
		 * tArry[j] = temp;
		 * 
		 * arrays[j] = new String[] {tArry[j] }; }
		 */

		// listArr.add(tArry);

		// System.out.println("Temp array -- " + Arrays.toString(tArry));

		/*
		 * String[] str = new String[arrayPosidex.length];
		 * 
		 * for(int i=0;i<pData.size();i++) { str[i] = arrayPosidex[i]; }
		 * 
		 * for (String string : str) { System.out.println("Column Data "+string); }
		 */

		return pData;
	}

	public String[] jaro(List<PythonJaroData> pJaroData, HashMap<Long, ColumnData> colDataMap,
			ArrayList<DataRank> dataOrder, String name) {

		if (pJaroData != null) {
			for (PythonJaroData p : pJaroData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pJaro = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pJaro = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}
		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		String jaroRow = printRow(dataOrder, colDataMap, "JARO");
		jaroRow = "JARO," + name + "," + jaroRow;
		String[] arrayJaro = jaroRow.split(",");

		return arrayJaro;
	}

	public String[] leven(List<PythonLevenData> pLevenData, HashMap<Long, ColumnData> colDataMap,
			ArrayList<DataRank> dataOrder, String name) {

		if (pLevenData != null) {
			for (PythonLevenData p : pLevenData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pLenen = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pLenen = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}
		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		String levenRow = printRow(dataOrder, colDataMap, "LEVENSHTEIN");

		levenRow = "LEVENSHTEIN," + name + "," + levenRow;
		String[] arrayLeven = levenRow.split(",");

		return arrayLeven;
	}

	public String[] qRatio(List<PythonQratioData> pQrData, HashMap<Long, ColumnData> colDataMap,
			ArrayList<DataRank> dataOrder, String name) {

		if (pQrData != null) {
			for (PythonQratioData p : pQrData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pQr = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pQr = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}
		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		String qRationRow = printRow(dataOrder, colDataMap, "QRATIO");
		qRationRow = "QRATIO," + name + "," + qRationRow;
		String[] arrayQratio = qRationRow.split(",");
		return arrayQratio;
	}

	// return listData;

	public List<String[]> rosetteDataProcess(List<RosetteData> rData, List<PosidexData> pData,
			List<PythonJaroData> pJaroData, List<PythonLevenData> pLevenData, List<PythonQratioData> pQrData,
			HashMap<Long, ColumnData> colDataMap, ArrayList<DataRank> dataOrder, String name) {

		if (rData != null) {
			for (RosetteData p : rData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.rData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.rData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pData != null) {
			for (PosidexData p : pData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.pData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pJaroData != null) {
			for (PythonJaroData p : pJaroData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pJaro = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pJaro = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pLevenData != null) {
			for (PythonLevenData p : pLevenData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pLenen = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pLenen = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pQrData != null) {
			for (PythonQratioData p : pQrData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pQr = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pQr = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		List<String[]> listData = setDataRosette(dataOrder, colDataMap, name);

		return listData;

	}

	/*
	 * public List<String[]> rosetteDataProcess1(List<RosetteData> rData,
	 * List<PosidexData> pData, List<PythonJaroData> pJaroData,
	 * List<PythonLevenData> pLevenData, List<PythonQratioData> pQrData,
	 * HashMap<Long, ColumnData> colDataMap, ArrayList<DataRank> dataOrder, String
	 * name) {
	 * 
	 * if (rData != null) { for (RosetteData p : rData) { ColumnData cData =
	 * colDataMap.get(p.getUid()); if (cData != null) { cData.rData = p; continue; }
	 * 
	 * // if uuid is not found in the map, create new column and add find its cData
	 * = new ColumnData(); cData.rData = p; colDataMap.put(p.getUid(), cData);
	 * DataRank dRank = new DataRank(p.getUid(), p.getScore());
	 * dataOrder.add(dRank); } }
	 * 
	 * if (pData != null) { for (PosidexData p : pData) { ColumnData cData =
	 * colDataMap.get(p.getUid()); if (cData != null) { cData.pData = p; continue; }
	 * 
	 * // if uuid is not found in the map, create new column and add find its cData
	 * = new ColumnData(); cData.pData = p; colDataMap.put(p.getUid(), cData);
	 * DataRank dRank = new DataRank(p.getUid(), p.getScore());
	 * dataOrder.add(dRank); } }
	 * 
	 * if (pJaroData != null) { for (PythonJaroData p : pJaroData) { ColumnData
	 * cData = colDataMap.get(p.getUid()); if (cData != null) { cData.pJaro = p;
	 * continue; }
	 * 
	 * // if uuid is not found in the map, create new column and add find its
	 * position cData = new ColumnData(); cData.pJaro = p;
	 * colDataMap.put(p.getUid(), cData); DataRank dRank = new DataRank(p.getUid(),
	 * p.getScore()); dataOrder.add(dRank); } }
	 * 
	 * if (pLevenData != null) { for (PythonLevenData p : pLevenData) { ColumnData
	 * cData = colDataMap.get(p.getUid()); if (cData != null) { cData.pLenen = p;
	 * continue; }
	 * 
	 * // if uuid is not found in the map, create new column and add find its
	 * position cData = new ColumnData(); cData.pLenen = p;
	 * colDataMap.put(p.getUid(), cData); DataRank dRank = new DataRank(p.getUid(),
	 * p.getScore()); dataOrder.add(dRank); } }
	 * 
	 * if (pQrData != null) { for (PythonQratioData p : pQrData) { ColumnData cData
	 * = colDataMap.get(p.getUid()); if (cData != null) { cData.pQr = p; continue; }
	 * 
	 * // if uuid is not found in the map, create new column and add find its
	 * position cData = new ColumnData(); cData.pQr = p; colDataMap.put(p.getUid(),
	 * cData); DataRank dRank = new DataRank(p.getUid(), p.getScore());
	 * dataOrder.add(dRank); } }
	 * 
	 * Collections.sort(dataOrder, new Comparator<DataRank>() {
	 * 
	 * @Override public int compare(DataRank o1, DataRank o2) {
	 * 
	 * if (o1.getScore() - o2.getScore() > 0) { return -1; } else return 1;
	 * 
	 * }
	 * 
	 * });
	 * 
	 * List<String[]> listData = setDataRosette1(dataOrder, colDataMap, name);
	 * 
	 * return listData;
	 * 
	 * }
	 */

	public List<String[]> posidexDataProcess(List<RosetteData> rData, List<PosidexData> pData,
			List<PythonJaroData> pJaroData, List<PythonLevenData> pLevenData, List<PythonQratioData> pQrData,
			HashMap<Long, ColumnData> colDataMap, ArrayList<DataRank> dataOrder, String name) {

		if (pData != null) {
			for (PosidexData p : pData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.pData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (rData != null) {
			for (RosetteData p : rData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.rData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.rData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pJaroData != null) {
			for (PythonJaroData p : pJaroData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pJaro = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pJaro = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pLevenData != null) {
			for (PythonLevenData p : pLevenData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pLenen = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pLenen = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pQrData != null) {
			for (PythonQratioData p : pQrData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pQr = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pQr = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		List<String[]> listData = setData(dataOrder, colDataMap, name);

		return listData;

	}

	public List<String[]> jaroDataProcess(List<RosetteData> rData, List<PosidexData> pData,
			List<PythonJaroData> pJaroData, List<PythonLevenData> pLevenData, List<PythonQratioData> pQrData,
			HashMap<Long, ColumnData> colDataMap, ArrayList<DataRank> dataOrder, String name) {

		/*
		 * if (pJaroData != null) { for (PythonJaroData r : pJaroData) { DataRank dRank
		 * = new DataRank(r.getUid(), r.getScore()); dataOrder.add(dRank); ColumnData
		 * cData = new ColumnData(); cData.pJaro = r; colDataMap.put(r.getUid(), cData);
		 * } }
		 */

		if (pJaroData != null) {
			for (PythonJaroData p : pJaroData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pJaro = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.pJaro = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (rData != null) {
			for (RosetteData p : rData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.rData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.rData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pData != null) {
			for (PosidexData p : pData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pLevenData != null) {
			for (PythonLevenData p : pLevenData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pLenen = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pLenen = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pQrData != null) {
			for (PythonQratioData p : pQrData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pQr = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pQr = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		List<String[]> listData = setDataJaro(dataOrder, colDataMap, name);

		return listData;
	}

	public List<String[]> levenDataProcess(List<RosetteData> rData, List<PosidexData> pData,
			List<PythonJaroData> pJaroData, List<PythonLevenData> pLevenData, List<PythonQratioData> pQrData,
			HashMap<Long, ColumnData> colDataMap, ArrayList<DataRank> dataOrder, String name) {

		/*
		 * if (pLevenData != null) { for (PythonLevenData r : pLevenData) { DataRank
		 * dRank = new DataRank(r.getUid(), r.getScore()); dataOrder.add(dRank);
		 * ColumnData cData = new ColumnData(); cData.pLenen = r;
		 * colDataMap.put(r.getUid(), cData); } }
		 */

		if (pLevenData != null) {
			for (PythonLevenData p : pLevenData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pLenen = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.pLenen = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (rData != null) {
			for (RosetteData p : rData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.rData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.rData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pData != null) {
			for (PosidexData p : pData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pJaroData != null) {
			for (PythonJaroData p : pJaroData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pJaro = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pJaro = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pQrData != null) {
			for (PythonQratioData p : pQrData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pQr = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pQr = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		List<String[]> listData = setDataLeven(dataOrder, colDataMap, name);

		return listData;
	}

	public List<String[]> qRatioDataProcess(List<RosetteData> rData, List<PosidexData> pData,
			List<PythonJaroData> pJaroData, List<PythonLevenData> pLevenData, List<PythonQratioData> pQrData,
			HashMap<Long, ColumnData> colDataMap, ArrayList<DataRank> dataOrder, String name) {

		/*
		 * if (pQrData != null) { for (PythonQratioData r : pQrData) { DataRank dRank =
		 * new DataRank(r.getUid(), r.getScore()); dataOrder.add(dRank); ColumnData
		 * cData = new ColumnData(); cData.pQr = r; colDataMap.put(r.getUid(), cData); }
		 * }
		 */

		if (pQrData != null) {
			for (PythonQratioData p : pQrData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pQr = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.pQr = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (rData != null) {
			for (RosetteData p : rData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.rData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its
				cData = new ColumnData();
				cData.rData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pData != null) {
			for (PosidexData p : pData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pData = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pData = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pJaroData != null) {
			for (PythonJaroData p : pJaroData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pJaro = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pJaro = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		if (pLevenData != null) {
			for (PythonLevenData p : pLevenData) {
				ColumnData cData = colDataMap.get(p.getUid());
				if (cData != null) {
					cData.pLenen = p;
					continue;
				}

				// if uuid is not found in the map, create new column and add find its position
				cData = new ColumnData();
				cData.pLenen = p;
				colDataMap.put(p.getUid(), cData);
				DataRank dRank = new DataRank(p.getUid(), p.getScore());
				dataOrder.add(dRank);
			}
		}

		Collections.sort(dataOrder, new Comparator<DataRank>() {

			@Override
			public int compare(DataRank o1, DataRank o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});

		List<String[]> listData = setDataQratio(dataOrder, colDataMap, name);

		return listData;

	}

	public void export(HashMap dataMap, String requestedName,String arabicName) {

		ArrayList<DataRank> dataOrder = new ArrayList();
		HashMap<Long, ColumnData> colDataMap = new HashMap<>();

		List listOfArr = new ArrayList();

		List<RosetteData> rData = (List<RosetteData>) dataMap.get("ROSETTE");
		// List<RosetteData> rEngData = (List<RosetteData>) dataMap.get("ROSETTEENG");
		List<PosidexData> pData = (List<PosidexData>) dataMap.get("POSIDEX");
		List<PythonJaroData> pJaroData = (List<PythonJaroData>) dataMap.get("JARO");
		List<PythonLevenData> pLevenData = (List<PythonLevenData>) dataMap.get("LEVENSHTEIN");
		List<PythonQratioData> pQrData = (List<PythonQratioData>) dataMap.get("QRATIO");

		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		List<String[]> rosetteData = rosetteDataProcess(rData, pData, pJaroData, pLevenData, pQrData, colDataMap,
				dataOrder, requestedName);

		/*
		 * dataOrder = new ArrayList(); colDataMap = new HashMap<>(); List<String[]>
		 * rosetteData1 = rosetteDataProcess1(rEngData, pData, pJaroData, pLevenData,
		 * pQrData, colDataMap, dataOrder, requestedName);
		 */

		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		List<String[]> psxData = posidexDataProcess(rData, pData, pJaroData, pLevenData, pQrData, colDataMap, dataOrder,
				requestedName);

		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		List<String[]> jaroData = jaroDataProcess(rData, pData, pJaroData, pLevenData, pQrData, colDataMap, dataOrder,
				requestedName);

		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		List<String[]> levelData = levenDataProcess(rData, pData, pJaroData, pLevenData, pQrData, colDataMap, dataOrder,
				requestedName);

		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		List<String[]> qratioData = qRatioDataProcess(rData, pData, pJaroData, pLevenData, pQrData, colDataMap,
				dataOrder, requestedName);

		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		String[] rosette = rosette(rData, colDataMap, dataOrder, requestedName);

		/*
		 * dataOrder = new ArrayList(); colDataMap = new HashMap<>(); String[]
		 * rosetteEng = rosetteList1(rEngData, colDataMap, dataOrder, requestedName);
		 */
		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
	//	String[][] posidex = posidex(pData, colDataMap, dataOrder, requestedName);
		
		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		List<PosidexData> posidex = posidex(pData, colDataMap, dataOrder, requestedName);
		

		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		// String[] jaro = jaro(pJaroData, colDataMap, dataOrder, requestedName);

		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		// String[] leven = leven(pLevenData, colDataMap, dataOrder, requestedName);

		dataOrder = new ArrayList();
		colDataMap = new HashMap<>();
		// String[] qRatio = qRatio(pQrData, colDataMap, dataOrder, requestedName);

		System.out.println("\nFinal CSV Data  ");

		try {
			storeDataIntoExcel(posidex, requestedName,arabicName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String[]> setDataRosette(ArrayList<DataRank> dataOrder, HashMap<Long, ColumnData> colDataMap,
			String requestedName) {

		String rosetteRow = printRow(dataOrder, colDataMap, "ROSETTE");
		// String rosetteRow1 = printRow(dataOrder, colDataMap, "ROSETTEENG");
		String posidexRow = printRow(dataOrder, colDataMap, "POSIDEX");
		String jaroRow = printRow(dataOrder, colDataMap, "JARO");
		String levenRow = printRow(dataOrder, colDataMap, "LEVENSHTEIN");
		String qRationRow = printRow(dataOrder, colDataMap, "QRATIO");
		String setRatioRow = printRow(dataOrder, colDataMap, "SETRATIO");
		String sortRationRow = printRow(dataOrder, colDataMap, "SORTRATIO");

		// String fileName = requestedName;

		requestedName = requestedName + ",";

		posidexRow = "POSIDEX," + requestedName + posidexRow;
		rosetteRow = "ROSETTE," + requestedName + rosetteRow;
		// rosetteRow1 = "ROSETTE-ENG," + requestedName + rosetteRow1;
		jaroRow = "JARO," + requestedName + jaroRow;
		levenRow = "LEVENSHTEIN," + requestedName + levenRow;
		qRationRow = "QRATIO," + requestedName + qRationRow;
		setRatioRow = "SETRATIO," + requestedName + setRatioRow;
		sortRationRow = "SORTRATIO," + requestedName + sortRationRow;

		String[] arrayRosette = rosetteRow.split(",");
		// String[] arrayRosette1 = rosetteRow1.split(",");
		String[] arrayPosidex = posidexRow.split(",");
		String[] arrayJaro = jaroRow.split(",");
		String[] arrayLeven = levenRow.split(",");
		String[] arrayQratio = qRationRow.split(",");

		String[] columns = { "Algorithm", " Screened Name", " matched data" };
		String[] emptyVal = { "" };

		List<String[]> listData = new ArrayList<>();
		listData.add(columns);
		listData.add(emptyVal);
		listData.add(arrayRosette);
		// listData.add(arrayRosette1);
		listData.add(arrayPosidex);
		listData.add(arrayJaro);
		listData.add(arrayLeven);
		listData.add(arrayQratio);

		return listData;

	}

	public List<String[]> setDataRosette1(ArrayList<DataRank> dataOrder, HashMap<Long, ColumnData> colDataMap,
			String requestedName) {

		String rosetteRow1 = printRow(dataOrder, colDataMap, "ROSETTE");
		String posidexRow = printRow(dataOrder, colDataMap, "POSIDEX");
		String jaroRow = printRow(dataOrder, colDataMap, "JARO");
		String levenRow = printRow(dataOrder, colDataMap, "LEVENSHTEIN");
		String qRationRow = printRow(dataOrder, colDataMap, "QRATIO");
		String setRatioRow = printRow(dataOrder, colDataMap, "SETRATIO");
		String sortRationRow = printRow(dataOrder, colDataMap, "SORTRATIO");

		// String fileName = requestedName;

		requestedName = requestedName + ",";

		posidexRow = "POSIDEX," + requestedName + posidexRow;
		// rosetteRow = "ROSETTE," + requestedName + rosetteRow;
		rosetteRow1 = "ROSETTE-ENG," + requestedName + rosetteRow1;
		jaroRow = "JARO," + requestedName + jaroRow;
		levenRow = "LEVENSHTEIN," + requestedName + levenRow;
		qRationRow = "QRATIO," + requestedName + qRationRow;
		setRatioRow = "SETRATIO," + requestedName + setRatioRow;
		sortRationRow = "SORTRATIO," + requestedName + sortRationRow;

		// String[] arrayRosette = rosetteRow.split(",");
		String[] arrayRosette1 = rosetteRow1.split(",");
		String[] arrayPosidex = posidexRow.split(",");
		String[] arrayJaro = jaroRow.split(",");
		String[] arrayLeven = levenRow.split(",");
		String[] arrayQratio = qRationRow.split(",");

		String[] columns = { "Algorithm", " Screened Name", " matched data" };
		String[] emptyVal = { "" };

		List<String[]> listData = new ArrayList<>();
		listData.add(columns);
		listData.add(emptyVal);
		// listData.add(arrayRosette);
		listData.add(arrayRosette1);
		listData.add(arrayPosidex);
		listData.add(arrayJaro);
		listData.add(arrayLeven);
		listData.add(arrayQratio);

		return listData;

	}

	public List<String[]> setData(ArrayList<DataRank> dataOrder, HashMap<Long, ColumnData> colDataMap,
			String requestedName) {

		String rosetteRow = printRow(dataOrder, colDataMap, "ROSETTE");
		String posidexRow = printRow(dataOrder, colDataMap, "POSIDEX");
		String jaroRow = printRow(dataOrder, colDataMap, "JARO");
		String levenRow = printRow(dataOrder, colDataMap, "LEVENSHTEIN");
		String qRationRow = printRow(dataOrder, colDataMap, "QRATIO");
		String setRatioRow = printRow(dataOrder, colDataMap, "SETRATIO");
		String sortRationRow = printRow(dataOrder, colDataMap, "SORTRATIO");

		// String fileName = requestedName;

		requestedName = requestedName + ",";

		posidexRow = "POSIDEX," + requestedName + posidexRow;
		rosetteRow = "ROSETTE," + requestedName + rosetteRow;
		jaroRow = "JARO," + requestedName + jaroRow;
		levenRow = "LEVENSHTEIN," + requestedName + levenRow;
		qRationRow = "QRATIO," + requestedName + qRationRow;
		setRatioRow = "SETRATIO," + requestedName + setRatioRow;
		sortRationRow = "SORTRATIO," + requestedName + sortRationRow;

		String[] arrayRosette = rosetteRow.split(",");
		String[] arrayPosidex = posidexRow.split(",");
		String[] arrayJaro = jaroRow.split(",");
		String[] arrayLeven = levenRow.split(",");
		String[] arrayQratio = qRationRow.split(",");
		// String[] arraySet = setRatioRow.split(",");
		// String[] arraySort = sortRationRow.split(",");

		// String[] columns = { "Algorithm", " Screened Name", " matched data" };
		String[] emptyVal = { "" };

		List<String[]> listData = new ArrayList<>();
		//
		// listData.add(columns);
		listData.add(emptyVal);
		listData.add(arrayPosidex);
		listData.add(arrayRosette);
		listData.add(arrayJaro);
		listData.add(arrayLeven);
		listData.add(arrayQratio);

		return listData;

	}

	public List<String[]> setDataDef(ArrayList<DataRank> dataOrder, HashMap<Long, ColumnData> colDataMap,
			String requestedName) {

		String rosetteRow = printRow(dataOrder, colDataMap, "ROSETTE");
		String posidexRow = printRow(dataOrder, colDataMap, "POSIDEX");
		String jaroRow = printRow(dataOrder, colDataMap, "JARO");
		String levenRow = printRow(dataOrder, colDataMap, "LEVENSHTEIN");
		String qRationRow = printRow(dataOrder, colDataMap, "QRATIO");
		String setRatioRow = printRow(dataOrder, colDataMap, "SETRATIO");
		String sortRationRow = printRow(dataOrder, colDataMap, "SORTRATIO");

		// String fileName = requestedName;

		requestedName = requestedName + ",";

		posidexRow = "POSIDEX," + requestedName + posidexRow;
		rosetteRow = "ROSETTE," + requestedName + rosetteRow;
		jaroRow = "JARO," + requestedName + jaroRow;
		levenRow = "LEVENSHTEIN," + requestedName + levenRow;
		qRationRow = "QRATIO," + requestedName + qRationRow;
		setRatioRow = "SETRATIO," + requestedName + setRatioRow;
		sortRationRow = "SORTRATIO," + requestedName + sortRationRow;

		String[] arrayRosette = rosetteRow.split(",");
		String[] arrayPosidex = posidexRow.split(",");
		String[] arrayJaro = jaroRow.split(",");
		String[] arrayLeven = levenRow.split(",");
		String[] arrayQratio = qRationRow.split(",");
		// String[] arraySet = setRatioRow.split(",");
		// String[] arraySort = sortRationRow.split(",");

		String[] emptyVal = { "" };

		List<String[]> listData = new ArrayList<>();
		// listData.add(arrayRosette);
		listData.add(emptyVal);
		listData.add(emptyVal);
		listData.add(arrayRosette);
		listData.add(arrayPosidex);
		listData.add(arrayJaro);
		listData.add(arrayLeven);
		listData.add(arrayQratio);

		return listData;

	}

	public List<String[]> setDataJaro(ArrayList<DataRank> dataOrder, HashMap<Long, ColumnData> colDataMap,
			String requestedName) {

		String rosetteRow = printRow(dataOrder, colDataMap, "ROSETTE");
		String posidexRow = printRow(dataOrder, colDataMap, "POSIDEX");
		String jaroRow = printRow(dataOrder, colDataMap, "JARO");
		String levenRow = printRow(dataOrder, colDataMap, "LEVENSHTEIN");
		String qRationRow = printRow(dataOrder, colDataMap, "QRATIO");
		String setRatioRow = printRow(dataOrder, colDataMap, "SETRATIO");
		String sortRationRow = printRow(dataOrder, colDataMap, "SORTRATIO");

		// String fileName = requestedName;

		requestedName = requestedName + ",";

		posidexRow = "POSIDEX," + requestedName + posidexRow;
		rosetteRow = "ROSETTE," + requestedName + rosetteRow;
		jaroRow = "JARO," + requestedName + jaroRow;
		levenRow = "LEVENSHTEIN," + requestedName + levenRow;
		qRationRow = "QRATIO," + requestedName + qRationRow;
		setRatioRow = "SETRATIO," + requestedName + setRatioRow;
		sortRationRow = "SORTRATIO," + requestedName + sortRationRow;

		String[] arrayRosette = rosetteRow.split(",");
		String[] arrayPosidex = posidexRow.split(",");
		String[] arrayJaro = jaroRow.split(",");
		String[] arrayLeven = levenRow.split(",");
		String[] arrayQratio = qRationRow.split(",");
		// String[] arraySet = setRatioRow.split(",");
		// String[] arraySort = sortRationRow.split(",");

		String[] emptyVal = { "" };

		List<String[]> listData = new ArrayList<>();
		listData.add(emptyVal);
		listData.add(emptyVal);
		listData.add(arrayJaro);
		listData.add(arrayRosette);
		listData.add(arrayPosidex);
		listData.add(arrayLeven);
		listData.add(arrayQratio);

		return listData;

	}

	public List<String[]> setDataLeven(ArrayList<DataRank> dataOrder, HashMap<Long, ColumnData> colDataMap,
			String requestedName) {

		String rosetteRow = printRow(dataOrder, colDataMap, "ROSETTE");
		String posidexRow = printRow(dataOrder, colDataMap, "POSIDEX");
		String jaroRow = printRow(dataOrder, colDataMap, "JARO");
		String levenRow = printRow(dataOrder, colDataMap, "LEVENSHTEIN");
		String qRationRow = printRow(dataOrder, colDataMap, "QRATIO");
		String setRatioRow = printRow(dataOrder, colDataMap, "SETRATIO");
		String sortRationRow = printRow(dataOrder, colDataMap, "SORTRATIO");

		// String fileName = requestedName;

		requestedName = requestedName + ",";

		posidexRow = "POSIDEX," + requestedName + posidexRow;
		rosetteRow = "ROSETTE," + requestedName + rosetteRow;
		jaroRow = "JARO," + requestedName + jaroRow;
		levenRow = "LEVENSHTEIN," + requestedName + levenRow;
		qRationRow = "QRATIO," + requestedName + qRationRow;
		setRatioRow = "SETRATIO," + requestedName + setRatioRow;
		sortRationRow = "SORTRATIO," + requestedName + sortRationRow;

		String[] arrayRosette = rosetteRow.split(",");
		String[] arrayPosidex = posidexRow.split(",");
		String[] arrayJaro = jaroRow.split(",");
		String[] arrayLeven = levenRow.split(",");
		String[] arrayQratio = qRationRow.split(",");
		// String[] arraySet = setRatioRow.split(",");
		// String[] arraySort = sortRationRow.split(",");

		String[] emptyVal = { "" };

		List<String[]> listData = new ArrayList<>();
		listData.add(emptyVal);
		listData.add(emptyVal);
		listData.add(arrayLeven);
		listData.add(arrayRosette);
		listData.add(arrayPosidex);
		listData.add(arrayJaro);
		listData.add(arrayQratio);

		return listData;

	}

	public List<String[]> setDataQratio(ArrayList<DataRank> dataOrder, HashMap<Long, ColumnData> colDataMap,
			String requestedName) {

		String rosetteRow = printRow(dataOrder, colDataMap, "ROSETTE");
		String posidexRow = printRow(dataOrder, colDataMap, "POSIDEX");
		String jaroRow = printRow(dataOrder, colDataMap, "JARO");
		String levenRow = printRow(dataOrder, colDataMap, "LEVENSHTEIN");
		String qRationRow = printRow(dataOrder, colDataMap, "QRATIO");
		String setRatioRow = printRow(dataOrder, colDataMap, "SETRATIO");
		String sortRationRow = printRow(dataOrder, colDataMap, "SORTRATIO");

		// String fileName = requestedName;

		requestedName = requestedName + ",";

		posidexRow = "POSIDEX," + requestedName + posidexRow;
		rosetteRow = "ROSETTE," + requestedName + rosetteRow;
		jaroRow = "JARO," + requestedName + jaroRow;
		levenRow = "LEVENSHTEIN," + requestedName + levenRow;
		qRationRow = "QRATIO," + requestedName + qRationRow;
		setRatioRow = "SETRATIO," + requestedName + setRatioRow;
		sortRationRow = "SORTRATIO," + requestedName + sortRationRow;

		String[] arrayRosette = rosetteRow.split(",");
		String[] arrayPosidex = posidexRow.split(",");
		String[] arrayJaro = jaroRow.split(",");
		String[] arrayLeven = levenRow.split(",");
		String[] arrayQratio = qRationRow.split(",");
		// String[] arraySet = setRatioRow.split(",");
		// String[] arraySort = sortRationRow.split(",");

		String[] emptyVal = { "" };

		List<String[]> listData = new ArrayList<>();
		listData.add(emptyVal);
		listData.add(emptyVal);
		listData.add(arrayQratio);
		listData.add(arrayRosette);
		listData.add(arrayPosidex);
		listData.add(arrayJaro);
		listData.add(arrayLeven);

		return listData;

	}

	private void storeDataIntoCSV(List<String[]> listData, String requestedName) throws IOException {

	
		Date date = new Date();
		date.getMinutes();

		String filePath = "D:\\New\\FINAL\\" + requestedName + ".xlsx";

		File file = new File(filePath);
		FileWriter outputfile = new FileWriter(file);
		try {
			// create FileWriter object with file as parameter

			System.out.println("Final List Dtaa  -- " + listData);

			CSVWriter writer = new CSVWriter(outputfile);

			writer.writeAll(listData);

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			outputfile.close();
		}

	}
	
	private void storeDataIntoExcel(List<PosidexData> listData, String requestedName,String arabicName) throws IOException {


		//List<PosidexData[]> objectArrayList = new ArrayList<>();
		
		PosidexData[] objArray = new PosidexData[listData.size()];
		objArray = listData.toArray(objArray);
		
		Arrays.sort(objArray, new Comparator<PosidexData>() {

			@Override
			public int compare(PosidexData o1, PosidexData o2) {

				if (o1.getScore() - o2.getScore() > 0) {
					return -1;
				} else
					return 1;

			}

		});
		
		
		/*
		 * for (PosidexData object : listData) { PosidexData[] objectArray = { object };
		 * // create an array with the current object objectArrayList.add(objectArray);
		 * // add the array to the new list }
		 */
		
		System.out.println("Object Array Data "+Arrays.toString(objArray));
		
	
		// workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();
        
     // spreadsheet object
        XSSFSheet spreadsheet
            = workbook.createSheet(" WorldCheck Data ");
        
     // creating a row object
        XSSFRow row;
        
     //   Object[] obj = listData.toArray();
        
 
     // This data needs to be written (Object[])
        Map<String, PosidexData[]> wc = new TreeMap<String, PosidexData[]>();
        
       // wc.put(1, new PosidexData[] {"","",""});
        
       for(int i=0;i<objArray.length;i++) {
        	wc.put(objArray[i].getUid()+"", new PosidexData[] {objArray[i]});
        	
        }
       
        System.out.println("Map Data -- "+wc);
        
        
        Set<String> keyid = new HashSet<>();
        
        for(int i=0;i< wc.size();i++) {
        	keyid = wc.keySet();
        
            
       // Set<String> keyid = wc.keySet();
        System.out.println("Key Set "+keyid);
        
        int rowid = 0;
  
        // writing the data into the sheets...
        
        int cellid1 = 0;
        row = spreadsheet.createRow(rowid++);
        Cell cell0 = row.createCell(cellid1++);
        cell0.setCellValue("Input: Screened Name: "+requestedName);
        
        Cell cell01 = row.createCell(cellid1++);
        cell01.setCellValue("Arabic Name: "+arabicName);
        
       // int cellid21 = 0;
        row = spreadsheet.createRow(rowid++);
        
        
        int cellid2 = 0;
        row = spreadsheet.createRow(rowid++);
        Cell cell1 = row.createCell(cellid2++);
        cell1.setCellValue("Algorithm");
        
        Cell cell2 = row.createCell(cellid2++);
        cell2.setCellValue(" WorldCheckID");
        
        Cell cell3 = row.createCell(cellid2++);
        cell3.setCellValue("MatchedName");
         
        Cell cell4 = row.createCell(cellid2++);
        cell4.setCellValue("totalMatches");
        
        Cell cell5 = row.createCell(cellid2++);
        cell5.setCellValue("MatchingScore");
        
        Cell cell6 = row.createCell(cellid2++);
        cell6.setCellValue("Manual Verification Status (Correct/Incorrect)");
  
        int matchScore = keyid.size();
        
        for (String key : keyid) {
  
            row = spreadsheet.createRow(rowid++);
            PosidexData[] objectArr = wc.get(key);
            int cellid = 0;
            
           
  
            for (PosidexData obj : objectArr) {            	
                Cell cel = row.createCell(cellid++);
                cel.setCellValue("POSIDEX");
                Cell cell = row.createCell(cellid++);
                cell.setCellValue(obj.getUid());
                Cell cell7 = row.createCell(cellid++);
                cell7.setCellValue(obj.getMatchedName());
                Cell cell8 = row.createCell(cellid++);
                cell8.setCellValue(matchScore);
                Cell cell9 = row.createCell(cellid++);
                cell9.setCellValue(obj.getScore());
            }
        }
        
        FileOutputStream out = new FileOutputStream(
                new File("D:\\WC\\"+requestedName+".xlsx"));
      
            workbook.write(out);
            out.close();
        }
	}

	private String printRowPSX(List<PosidexData> dataOrderList, HashMap<Long, ColumnData> dataMap, String algoName) {
		Boolean isFirst = true;
		String dataStr = "";
		for (PosidexData dr : dataOrderList) {
			ColumnData cData = dataMap.get(dr.getUid());
			Object o = cData.getData(algoName);
			if (isFirst) {
				isFirst = false;
			} else {
				dataStr += ",";
			}
			if (o != null) {
				dataStr += o;
			} else {
				dataStr += getBlankData();
			}

		}

		return dataStr;

	}

	private String printRowJaro(List<PythonJaroData> dataOrderList, HashMap<Long, ColumnData> dataMap,
			String algoName) {
		Boolean isFirst = true;
		String dataStr = "";
		for (PythonJaroData dr : dataOrderList) {
			ColumnData cData = dataMap.get(dr.getUid());
			Object o = cData.getData(algoName);
			if (isFirst) {
				isFirst = false;
			} else {
				dataStr += ",";
			}
			if (o != null) {
				dataStr += o;
			} else {
				dataStr += getBlankData();
			}

		}

		return dataStr;

	}

	private String printRowLeven(List<PythonLevenData> dataOrderList, HashMap<Long, ColumnData> dataMap,
			String algoName) {
		Boolean isFirst = true;
		String dataStr = "";
		for (PythonLevenData dr : dataOrderList) {
			ColumnData cData = dataMap.get(dr.getUid());
			Object o = cData.getData(algoName);
			if (isFirst) {
				isFirst = false;
			} else {
				dataStr += ",";
			}
			if (o != null) {
				dataStr += o;
			} else {
				dataStr += getBlankData();
			}

		}

		return dataStr;

	}

	private String printRowQratio(List<PythonQratioData> dataOrderList, HashMap<Long, ColumnData> dataMap,
			String algoName) {
		Boolean isFirst = true;
		String dataStr = "";
		for (PythonQratioData dr : dataOrderList) {
			ColumnData cData = dataMap.get(dr.getUid());
			Object o = cData.getData(algoName);
			if (isFirst) {
				isFirst = false;
			} else {
				dataStr += ",";
			}
			if (o != null) {
				dataStr += o;
			} else {
				dataStr += getBlankData();
			}

		}

		return dataStr;

	}

	private String printRow(List<DataRank> dataOrderList, HashMap<Long, ColumnData> dataMap, String algoName) {
		Boolean isFirst = true;
		String dataStr = "";
		for (DataRank dr : dataOrderList) {
			ColumnData cData = dataMap.get(dr.getUuid());
			Object o = cData.getData(algoName);
			if (isFirst) {
				isFirst = false;
			} else {
				dataStr += ",";
			}
			if (o != null) {
				dataStr += o;
			} else {
				dataStr += getBlankData();
			}

		}

		return dataStr;

	}

	private String[] printRowPosidex(List<DataRank> dataOrderList, HashMap<Long, ColumnData> dataMap, String algoName) {
		Boolean isFirst = true;
		String dataStr = "";
		String[] dataArr = new String[dataOrderList.size()];

		for (int i = 0; i < dataOrderList.size(); i++) {
			for (DataRank dr : dataOrderList) {
				ColumnData cData = dataMap.get(dr.getUuid());
				Object o = cData.getData(algoName);
				if (isFirst) {
					isFirst = false;
				} else {
					dataStr += ",";
				}
				if (o != null) {
					dataStr += o;
				} else {
					dataStr += getBlankData();
				}
			}
			dataArr[i] = dataStr;
		}

		return dataArr;

	}

	private String printRowDef(List<DataRank> dataOrderList, HashMap<Long, ColumnData> dataMap, String algoName) {
		Boolean isFirst = true;
		String dataStr = "";
		for (DataRank dr : dataOrderList) {
			ColumnData cData = dataMap.get(dr.getUuid());
			Object o = cData.getData(algoName);
			if (!isFirst) {
				isFirst = false;
			} else {
				dataStr += ",";
			}
			if (o != null) {
				dataStr += o + " ,";
			}

		}

		return dataStr;

	}

	String getBlankData() {
		return ",,";
	}

	// private List insertDataRank(ArrayList<DataRank> dataOrderList, DataRank
	// dataRank){

	// for()

	// return dataOrderList;
	// }

	@Data
	class DataRank {
		Long uuid;
		Float score;

		DataRank(Long uuid, Float score) {
			this.uuid = uuid;
			this.score = score;
		}
	}

	@Data
	class ColumnData {
		RosetteData rData;
		PosidexData pData;
		PythonJaroData pJaro;
		PythonLevenData pLenen;
		PythonQratioData pQr;
		PythonSetRatioData pSr;
		PythonSortRationData pSor;

		MatchedData getData(String type) {
			if (rData != null && type.equals(rData.getALOG_NAME()))
				return rData;
			if (pData != null && type.equals(pData.getALOG_NAME()))
				return pData;
			if (pJaro != null && type.equals(pJaro.getALOG_NAME()))
				return pJaro;
			if (pLenen != null && type.equals(pLenen.getALOG_NAME()))
				return pLenen;
			if (pQr != null && type.equals(pQr.getALOG_NAME()))
				return pQr;
			if (pSr != null && type.equals(pSr.getALOG_NAME()))
				return pSr;
			if (pSor != null && type.equals(pSor.getALOG_NAME()))
				return pSor;

			return null;
		}
	}

}
