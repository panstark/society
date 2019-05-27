package com.pan.society.Common;

import com.yonyou.ocm.common.entity.ChildClass;
import com.yonyou.ocm.common.entity.FieldDisplay;
import com.yonyou.ocm.common.enums.ExcelRedisKeyEnums;
import com.yonyou.ocm.common.exception.BusinessException;
import com.yonyou.ocm.common.service.dto.BaseDto;
import com.yonyou.ocm.common.service.dto.POIExcelColumnDto;
import com.yonyou.ocm.common.service.dto.POIExcelSheetDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 导出数据Excel工具类
 *
 * @description 导入导出Excel
 * @author gaopengf
 * @date 2017-07-31
 */
public class ExcelExportImportor {

	public static Logger logger = Logger.getLogger(ExcelExportImportor.class);

//	/**
//	 * 根据数据，导出excel
//	 * @param dataMap 前两个个元素ListTitle 后续的都是数据（页签为key）
//	 * @param sheetMap 页签属性名称
//	 * @throws Exception
//	 */
//	public static void writeExcelAli(Map<String, List<List<Object>>> dataMap, Map<String,String> sheetMap, ExcelWriter excelWriter) throws Exception {
//		//数据全是List<String> 无模型映射关系
//		int sheetNum = 1;
//		for(Map.Entry<String, List<List<Object>>> entry: dataMap.entrySet()){
////			List<List<String>> head = new ArrayList<>();
////			List<List<Object>> body = new ArrayList<>();
////			for(int i=0;i<entry.getValue().size();i++){
////				List<Object> list = entry.getValue().get(i);
////				if(i==1){
////					for(Object obj: list){
////						List<String> headList = new ArrayList<>();
////						headList.add((String) obj);
////						head.add(headList);
////					}
////				}else{
////					body.add(list);
////				}
////			}
////			Table table = new Table(2);
////			table.setHead(head);
//			Sheet sheet = new Sheet(sheetNum,1);
//			sheet.setSheetName(sheetMap.get(entry.getKey()));
////			excelWriter.write1(body, sheet, table);
//			excelWriter.write1(entry.getValue(), sheet);
//			sheetNum++;
//		}
//	}

	/**
	 * 根据数据，导出excel
	 * @param dataMap 前两个个元素ListTitle 后续的都是数据（页签为key）
	 * @param sheetMap 页签属性名称
	 * @throws Exception
	 */
	public static void writeExcel(Map<String, List<List<Object>>> dataMap, Map<String,String> sheetMap, Workbook wb, String redisKey) throws Exception {
		try {
            CellStyle cellStylehead = null;
            CellStyle noteStyle = null;
            if(wb == null) {
				throw new BusinessException("Excel文件未创建。");
			}
			Font font = wb.createFont();//字体
			font.setBold(true);//加粗
			cellStylehead = wb.createCellStyle();
			cellStylehead.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStylehead.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStylehead.setFont(font);//设置字体样式
			cellStylehead.setBorderRight(BorderStyle.THIN);//右边框
			cellStylehead.setBorderBottom(BorderStyle.THIN);
			cellStylehead.setAlignment(HorizontalAlignment.LEFT);//水平对齐
			cellStylehead.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
			cellStylehead.setWrapText(false);//自动换行
			Font notefont = wb.createFont();//字体
			notefont.setBold(true);//加粗
			notefont.setColor(Font.COLOR_RED);//红色
			noteStyle = wb.createCellStyle();
			noteStyle.setFont(notefont);//设置字体样式
			noteStyle.setAlignment(HorizontalAlignment.LEFT);//水平对齐
			noteStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
			noteStyle.setWrapText(false);//自动换行
			// 导出的文字编码
			//fileName = new String(fileName.getBytes("GB2312"), "UTF-8");
			//说明文字

			//表头样式

			/*//表体样式
			CellStyle cellStyleBody = wb.createCellStyle();//表体单元格样式
			cellStyleBody.setAlignment(CellStyle.ALIGN_LEFT);//水平对齐
			cellStyleBody.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直对齐
			cellStyleBody.setWrapText(false);//自动换行*/
			BigDecimal totalNum = BigDecimal.ZERO;
			for (Map.Entry<String, List<List<Object>>> entry : dataMap.entrySet()) {
				List<List<Object>> dataList = entry.getValue();
				totalNum = totalNum.add(new BigDecimal(dataList.size()));
			}
			//导出数据
			BigDecimal handleNum = BigDecimal.ZERO;
//			Map<String, String> exportInfoMap = new HashMap<>();
			for (Map.Entry<String, List<List<Object>>> entry : dataMap.entrySet()) {
				String key = entry.getKey();
				String sheetName = sheetMap.get(key);
				List<List<Object>> dataList = entry.getValue();
                Sheet sheet = wb.getSheet(sheetName);
                int initRownum = 0;
                if(sheet == null){
                    sheet = wb.createSheet(sheetName);
                    initRownum = 0;
                }else {//追加数据
                    initRownum = sheet.getLastRowNum()+1;
					dataList.remove(0);
					dataList.remove(0);
                }

				for(int index = 0; index < dataList.size(); index++){
					/*Row row = sh.createRow(rownum);*/
					int realRowNum = index + initRownum;
					sheet.createRow(realRowNum);
					List<Object> rowData = dataList.get(index);
					for(int cellnum = 0; cellnum < rowData.size(); cellnum++){
						if(cellnum == 0 && initRownum != 0){
							int no = (Integer)rowData.get(cellnum);
							rowData.set(cellnum,no+initRownum-2);
						}
						CellFactory(sheet.getRow(realRowNum).createCell(cellnum),rowData.get(cellnum));
						/*sh.trackAllColumnsForAutoSizing();//设置列宽必须的代码
						//设置单元格列宽
//						int width = 18;
//						sh.setColumnWidth(cellnum, 256*width+184);
						//单元格列宽自适应
						sh.autoSizeColumn(cellnum);
						Cell cell = row.createCell(cellnum);
						Object cellData = rowData.get(cellnum);
						CellFactory(cell, cellData);*/

						if(cellnum==0&&realRowNum==0){
							sheet.getRow(realRowNum).getCell(cellnum).setCellStyle(noteStyle);
						}else if((realRowNum==0||realRowNum==1)){
								if("id".equals(sheet.getRow(0).getCell(cellnum).getStringCellValue())){
									sheet.getRow(realRowNum).getCell(cellnum).setCellStyle(noteStyle);
								}else if(cellStylehead != null){
									sheet.getRow(realRowNum).getCell(cellnum).setCellStyle(cellStylehead);
								}
						}
					}
					if(null!=redisKey){
						handleNum = handleNum.add(BigDecimal.ONE);
						BigDecimal percent = handleNum.divide(totalNum, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0);
//						exportInfoMap.put(ExcelRedisKeyEnums.EXPORT_QUERY.getCode(), "finish");
//						exportInfoMap.put(ExcelRedisKeyEnums.EXPORT_STATUS.getCode(), percent.toString());
						if((percent.intValue())% 20 == 0){
							CacheUtil.getInstance().setMapStr(redisKey, ExcelRedisKeyEnums.EXPORT_STATUS.getCode(), percent.toString(), 1800);
						}
//						setInfo(redisKey, exportInfoMap);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 向redis中存放信息
	 */
	private static void setInfo(String redisKey, Map<String, String> map) {

		try {
			CacheUtil.getInstance().setMap(redisKey, map, 1800);
		} catch (Exception e) {
			throw new BusinessException("向redis中存放信息失败");
		}
	}


	/**
	 * 根据数据类型不同，建立相应的对象
	 * @param cell
	 * @param columnVal
	 * @return
	 * @throws Exception
	 */
	private static void CellFactory(Cell cell, Object columnVal) throws Exception{
		if(null == columnVal){
			cell.setCellValue("");
		}else{
			if(columnVal.getClass().equals(Long.class)){
				cell.setCellValue(Long.parseLong(columnVal.toString()));
				cell.setCellType(CellType.NUMERIC);
			}else if(columnVal.getClass().equals(Integer.class)){
				cell.setCellValue(Integer.parseInt(columnVal.toString()));
				cell.setCellType(CellType.NUMERIC);
			}else if(columnVal.getClass().equals(Double.class)){
				cell.setCellValue(Double.parseDouble(columnVal.toString()));
				cell.setCellType(CellType.NUMERIC);
			}else if(columnVal.getClass().equals(Float.class)){
				cell.setCellValue(Float.parseFloat(columnVal.toString()));
				cell.setCellType(CellType.NUMERIC);
			}else if(columnVal.getClass().equals(String.class)){
				cell.setCellValue(columnVal.toString());
				cell.setCellType(CellType.STRING);
			}else if(columnVal.getClass().equals(Date.class)){
				if(((Date)columnVal).getHours() == 0 && ((Date)columnVal).getMinutes() == 0 && ((Date)columnVal).getSeconds() == 0){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String str = sdf.format((Date)columnVal);
					cell.setCellValue(str);
				}else{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String str = sdf.format((Date)columnVal);
					cell.setCellValue(str);
				}
			}else if(columnVal.getClass().equals(BigDecimal.class)){
				cell.setCellValue(((BigDecimal)columnVal).doubleValue());
				cell.setCellType(CellType.NUMERIC);
			}else if(columnVal.getClass().equals(Timestamp.class)){
				if(((Timestamp)columnVal).getHours() == 0 && ((Timestamp)columnVal).getMinutes() == 0 && ((Timestamp)columnVal).getSeconds() == 0){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String str = sdf.format((Date)columnVal);
					cell.setCellValue(str);
				}else{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String str = sdf.format((Timestamp)columnVal);
					cell.setCellValue(str);
				}
			}else{
				cell.setCellValue("");
			}
		}
	}


	/**
	 * 导出PDF模版
	 * @param map title标题列
	 * @param sheetMap 页签属性名称
	 * @throws Exception
	 */
	public static void downloadPdfTemplate(Map<String,Map<String, String>> map, Map<String, String> sheetMap, HSSFWorkbook wb) throws Exception {
		writeExcelNew(createResultMap(map,sheetMap), sheetMap, wb);
	}

	/**
	 * 导出excel模版
	 * @param map title标题列
	 * @param sheetMap 页签属性名称
	 * @throws Exception
	 */
	public static void downloadExcelTemplate(Map<String,Map<String, String>> map, Map<String, String> sheetMap, XSSFWorkbook wb) throws Exception {
		writeExcel(createResultMap(map,sheetMap), sheetMap, wb, null);
	}

	public static Map<String, List<List<Object>>> createResultMap(Map<String,Map<String, String>> map, Map<String, String> sheetMap){
		//数据导出容器
		Map<String, List<List<Object>>> resultMap = new LinkedHashMap<>();
		for(String key: map.keySet()){
			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> listTitleCode = new LinkedList<Object>();
			List<Object> listTitle = new LinkedList<Object>();
			List<Object> listRow = new LinkedList<Object>();
			Map<String, String> infoMap = map.get(key);
			listTitle.add("序号");
			listTitleCode.add(key);
			listRow.add(0);
			for(String infoKey: infoMap.keySet()){
//				add by daikai3  如果是子表页签，则不作为字段放入
//				start
				if(sheetMap.keySet().contains(infoKey)){
					continue;
				}
//				end
				listTitle.add(infoMap.get(infoKey));
				listTitleCode.add(infoKey);
			}
			list.add(listTitleCode);
			list.add(listTitle);
			list.add(listRow);
			resultMap.put(key, list);
		}
		return resultMap;
	}

	public static Map<String, List<List<Object>>> createResultMapToData(Map<String,Map<String, String>> map, Map<String, Map<Integer, List<Map<String, Object>>>> dataMap){
		//数据导出容器
		Map<String, List<List<Object>>> resultMap = new LinkedHashMap<>();
		for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> listTitleCode = new LinkedList<Object>();
			List<Object> listTitle = new LinkedList<Object>();

			Map<String, String> infoMap = entry.getValue();
			listTitle.add("序号");
			listTitleCode.add(key);
			for (Map.Entry<String, String> infoEntry : infoMap.entrySet()) {
				listTitle.add(infoEntry.getValue());
				listTitleCode.add(infoEntry.getKey());
			}
			list.add(listTitleCode);
			list.add(listTitle);

			if(dataMap.containsKey(key)){
				Map<Integer, List<Map<String, Object>>> dataList = dataMap.get(key);
				for (Map.Entry<Integer, List<Map<String, Object>>> dataEntry : dataList.entrySet()) {
					Integer i = dataEntry.getKey();
					for(Map<String, Object> info: dataEntry.getValue()){
						List<Object> listRow = new LinkedList<Object>();
						listRow.add(i+1);
						for (Map.Entry<String, String> infoEntry : infoMap.entrySet()) {
							if(info.containsKey(infoEntry.getKey())){
								listRow.add(info.get(infoEntry.getKey()));
							}
						}
						list.add(listRow);
					}
				}
			}
			resultMap.put(key, list);
		}
		return resultMap;
	}

	/**
	 * 导出excel数据
	 * @param map title标题列
	 * @param sheetMap 页签属性名称
	 * @param dataMap 数据集
	 * @param
	 * @throws Exception
	 */
	public static void exportExcelData(Map<String,Map<String, String>> map, Map<String,String> sheetMap, Map<String, Map<Integer, List<Map<String, Object>>>> dataMap, XSSFWorkbook wb) throws Exception {
		writeExcel(createResultMapToData(map,dataMap), sheetMap, wb, null);
	}

	/**
	 * 导出excel数据
	 * @param map title标题列
	 * @param sheetMap 页签属性名称
	 * @param dataMap 数据集
	 * @param
	 * @throws Exception
	 */
	public static void exportExcelData(Map<String,Map<String, String>> map, Map<String,String> sheetMap, Map<String, Map<Integer, List<Map<String, Object>>>> dataMap, Workbook wb, String redisKey) throws Exception {
		writeExcel(createResultMapToData(map,dataMap), sheetMap, wb, redisKey);
	}


//	/**
//	 * 导出excel数据-ali组件
//	 * @param map title标题列
//	 * @param sheetMap 页签属性名称
//	 * @param dataMap 数据集
//	 * @param
//	 * @throws Exception
//	 */
//	public static void exportExcelDataAli(Map<String,Map<String, String>> map, Map<String,String> sheetMap, Map<String, Map<Integer, List<Map<String, Object>>>> dataMap, ExcelWriter excelWriter) throws Exception {
//		writeExcelAli(createResultMapToData(map,dataMap), sheetMap, excelWriter);
//	}

	/**
	 * 导出PDF数据
	 * @param map title标题列
	 * @param sheetMap 页签属性名称
	 * @param dataMap 数据集
	 * @param
	 * @throws Exception
	 */
	public static void exportPdfData(Map<String,Map<String, String>> map, Map<String,String> sheetMap, Map<String, Map<Integer, List<Map<String, Object>>>> dataMap, HSSFWorkbook wb) throws Exception {
		writeExcelNew(createResultMapToData(map,dataMap), sheetMap, wb);
	}

	/**
	 * excel导入数据
	 * @param excelStream
	 * @param clazz 实体类型
	 * @return List<TDto> 由excel转换过来
	 */
	public static <TDto extends BaseDto> Map<String, Object> loadExcel(boolean isEdit, InputStream excelStream, Class<?> clazz, List<POIExcelSheetDto> selectedColumns, Map<String, String> sheetCodeMap) throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {
			List<String> errorList = new LinkedList<>();
			//头数据
			Map<String, List<TDto>> headmap = new LinkedHashMap<>();
			//体数据
			Map<String, Map<String, Collection<Object>>> bodymap = new LinkedHashMap<>();
			//主键list
			List<String> idList = new LinkedList<>();
			//修改的列
			Map<String, List<String>> editColumn = new LinkedHashMap<>();

//			//如果有选中列，则依据选中的列进行修改
//			if(selectedColumns!=null&&selectedColumns.size()!=0){
//				boolean hasHead = false;
//				for(POIExcelSheetDto sheet: selectedColumns) {
//					if ("head".equals(sheet.getSheetCode())) {
//						hasHead = true;
//					}
//				}
//				if(!hasHead){
//					POIExcelSheetDto sheetDto = new POIExcelSheetDto();
//					List<POIExcelColumnDto> columnDtos = new ArrayList<>();
//					POIExcelColumnDto column = new POIExcelColumnDto();
//					column.setColumnCode("id");
//					column.setColumnName("主键,不要改动或复制");
//					sheetDto.setColumnDtos(columnDtos);
//					sheetDto.setSheetCode("head");
//					sheetDto.setSheetName("单据主体");
//					selectedColumns.add(0, sheetDto);
//				}
//				for(POIExcelSheetDto sheet: selectedColumns){
//					String sheetCode = sheet.getSheetCode();
//					List<String> columns = new ArrayList<>();
//					for(POIExcelColumnDto column: sheet.getColumnDtos()){
//						columns.add(column.getColumnCode());
//					}
//					if(isEdit){
//						columns.add("id");
//					}
//					editColumn.put(sheetCode, columns);
//				}
//			}

			//子表页签第一个字段为主实体中子实体的字段名
			String sheetAttr = null;
			//解析所有sheet的内容
			Map<String, List<String[]>> sheetMap = XLSXCovertCSVReader.readerExcelByInputStream(excelStream);

			//如果导入时未选择导入列，则默认当前excel中的列为所有修改列
			if(isEdit&&(selectedColumns==null||selectedColumns.size()==0)){
				for(String sheetName: sheetMap.keySet()){
					String sheetCode = sheetCodeMap.get(sheetName);
					//当前sheet所有行
					List<String> columns = new ArrayList<>();
					List<String[]> rowlist = sheetMap.get(sheetName);
					for(int i=0;i<rowlist.size();i++){
						if(i>0){
							break;
						}
						String[] attrs = rowlist.get(i);
						for(int j=0;j<attrs.length;j++){
							if(j==0){
								continue;
							}
							columns.add(attrs[j].replace("\"", ""));
						}
					}
					editColumn.put(sheetCode, columns);
				}
			}else if(isEdit&&selectedColumns!=null&&selectedColumns.size()!=0){
				boolean hasHead = false;
				for(POIExcelSheetDto sheet: selectedColumns) {
					if ("head".equals(sheet.getSheetCode())) {
						hasHead = true;
					}
				}
				if(!hasHead){
					POIExcelSheetDto sheetDto = new POIExcelSheetDto();
					List<POIExcelColumnDto> columnDtos = new ArrayList<>();
					POIExcelColumnDto column = new POIExcelColumnDto();
					column.setColumnCode("id");
					column.setColumnName("主键,不要改动或复制");
					sheetDto.setColumnDtos(columnDtos);
					sheetDto.setSheetCode("head");
					sheetDto.setSheetName("单据主体");
					selectedColumns.add(0, sheetDto);
				}
				String errorMsg = "";
				for(POIExcelSheetDto sheet: selectedColumns){
					String sheetCode = sheet.getSheetCode();
					String sheetName = sheet.getSheetName();
					List<String> columns = new ArrayList<>();//修改的列
					List<String> notHascolumns = new ArrayList<>();//选择列不存在excel的字段
					List<String[]> rowlist = sheetMap.get(sheetName);
					for(POIExcelColumnDto column: sheet.getColumnDtos()){
						for(int i=0;i<rowlist.size();i++){
							int notHasSize = 0;//如果长度等于excel，则选择的列不存在excel中
							if(i>0){
								break;
							}
							String[] attrs = rowlist.get(i);
							for(int j=0;j<attrs.length;j++){
								if(j==0){
									continue;
								}
								if(column.getColumnCode().equals(attrs[j].replace("\"", ""))){
									columns.add(column.getColumnCode());
									break;
								}
								notHasSize++;
							}
							if(notHasSize == attrs.length - 1){
								notHascolumns.add(column.getColumnName());
							}
						}
					}
					columns.add("id");//Excel修改，补充id字段
					if(notHascolumns!=null&&notHascolumns.size()!=0){
						errorMsg = errorMsg + "Excel页签【"+ sheetName +"】"+"中不存在勾选列："+ notHascolumns.toString()+"。";
					}
					editColumn.put(sheetCode, columns);
				}
				if(errorMsg!=""){
					throw new BusinessException(errorMsg);
				}
			}

			//子表集合
			for(String sheetName: sheetMap.keySet()){
				Map<String, Collection<Object>> body = new LinkedHashMap<>();
//				String sheetCode = sheetDto.getSheetCode();
//				String sheetName = sheetDto.getSheetName();
				String sheetCode = sheetCodeMap.get(sheetName);
				//当前sheet所有行
				List<String[]> rowlist = sheetMap.get(sheetName);
				int sheetRows = rowlist.size();
				List<String> nameAttrs = new LinkedList<String>();
				Set<String> numCheck = new HashSet<>();//序号检查
				if("head".equals(sheetCode)){
					for(int i=0;i<sheetRows;i++){
						if(i==0||i==1){
							continue;
						}
						List<TDto> headlist = new LinkedList<>();
						String content = rowlist.get(i)[0].replace("\"", "");
						if(content==null||content.length()==0)
							continue;
						if(numCheck.contains(content)){
							String error = "< 解析excel值 >：页签【单据主体】，序号【"+content+"】有重复，请修改";
							errorList.add(error);
						}
						numCheck.add(content);
						headmap.put(content, headlist);
					}
				}else{
					sheetAttr = rowlist.get(0)[0].replace("\"", "");
					Field field = ReflectUtils.getFieldName(clazz, sheetAttr);
					//其余为表体
					for(int i=0;i<sheetRows;i++){
						if(i==0||i==1){
							continue;
						}
						//子表集合
						Collection<Object> cle = null;
						if(field.getType().equals(List.class)){
							cle = new ArrayList<>();
						}
						if(field.getType().equals(Set.class)){
							cle = new HashSet<>();
						}
						body.put(rowlist.get(i)[0].replace("\"", ""), cle);
					}
				}

				//页签内所有行
				for(int i=0;i<sheetRows;i++){
					if(i==1 && rowlist.get(i).length>0){
						continue;
					}else if(i==0 && rowlist.get(i).length>0){
						for(int j=0; j < rowlist.get(i).length; j++){
							String var = rowlist.get(i)[j].replace("\"", "");
							nameAttrs.add(var);
						}
						//第一页签为主表页签
					}else if("head".equals(sheetCode) && rowlist.get(i).length>0){
						//主表页签
						String num = null;
						boolean notNullRowflag = false;
						TDto dto = (TDto)clazz.newInstance();
						for(int j=0; j<rowlist.get(i).length; j++){
							if(j==0){
								num = rowlist.get(i)[j].replace("\"", "");
								continue;
							}
							String attr = nameAttrs.get(j);
							//修改时，如果选择的修改列中没有这个字段就不检查格式是否正确
							if(isEdit){
								boolean hasAttr = false;
								for(String col: editColumn.get(sheetCode)){
									if(attr.equals(col)){
										hasAttr = true;
									}
								}
								if(!hasAttr){
									continue;
								}
							}
							String columnVal = rowlist.get(i)[j].replace("\"", "");//值
							String showname = rowlist.get(1)[j].replace("\"", "");//中文名
							//获取主表id用于查询要修改的数据
							if(isEdit&&"id".equals(attr)){
								if(StringUtils.isNotEmpty(columnVal)){
									idList.add(columnVal);
									ReflectUtils.setProperty(dto, attr, columnVal);
								}
							}

							if(!StringUtils.isEmpty(columnVal)){
								try {
									Field field = ReflectUtils.getFieldName(dto.getClass(), attr);
									//字段是否存在名称注解
									boolean fieldHasFieldDisplay = field.isAnnotationPresent(FieldDisplay.class);
									if(fieldHasFieldDisplay){
										FieldDisplay fieldAnno = field.getAnnotation(FieldDisplay.class);
										String enumClass = fieldAnno.enumClass();
										if(null!=enumClass && !"".equals(enumClass)){
											Object enumCode = ReflectUtils.getEnumCode(Class.forName(enumClass), columnVal);
											if(Integer.class == enumCode.getClass()){
												ReflectUtils.setProperty(dto, attr, enumCode);
											}else if(String.class == enumCode.getClass()){
												ReflectUtils.setProperty(dto, attr, enumCode);
											}
										}else{
											ReflectUtils.setProperty(dto, attr, columnVal);
										}
									}
									notNullRowflag = true;
								} catch (Exception e) {
									String error = "< 解析excel值 >：页签【单据主体】，序号【"+num+"】行的【"+showname+"】字段的值【"+columnVal+"】，类型或格式错误";
									Field f = ReflectUtils.getFieldName(dto.getClass(), attr);
									Class<?> fieldType = f.getType();
									if(Date.class == fieldType){
										error = error + " 正确的日期格式为yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss";
									}
									logger.error(error);
									errorList.add(error);
								}
							}

						}
						if(notNullRowflag){
							headmap.get(num).add(dto);
						}
					}else{
						//其余子表页签
						//对应行序号
						String num = null;
						//获取子实体类型
						Field field = ReflectUtils.getFieldName(clazz, sheetAttr);
						//是否有子实体注解
						boolean fieldHasChildClass = field.isAnnotationPresent(ChildClass.class);
						Object chdDto = null;
						if(fieldHasChildClass){
							//子实体注解实体类型
							ChildClass fieldAnno = field.getAnnotation(ChildClass.class);
							Class<?> chdClass = fieldAnno.classType();
							chdDto = chdClass.newInstance();
						}
						for(int j=0; j<rowlist.get(i).length; j++){
							if(j==0){
								num = rowlist.get(i)[j].replace("\"", "");
								continue;
							}
							String attr = nameAttrs.get(j);
							//修改时，如果选择的修改列中没有这个字段就不检查格式是否正确
							if(isEdit){
								boolean hasAttr = false;
								for(String col: editColumn.get(sheetCode)){
									if(attr.equals(col)){
										hasAttr = true;
									}
								}
								if(!hasAttr){
									continue;
								}
							}
							String columnVal = rowlist.get(i)[j].replace("\"", "");//值
							String showname = rowlist.get(1)[j].replace("\"", "");//中文名
							//获取主表id用于查询要修改的数据
							if(isEdit&&"id".equals(attr)){
								if(StringUtils.isNotEmpty(columnVal)){
									ReflectUtils.setProperty(chdDto, attr, columnVal);
								}
							}
							if(!StringUtils.isEmpty(columnVal)){
								try {
									Field fieldchd = ReflectUtils.getFieldName(chdDto.getClass(), attr);
									//字段是否存在名称注解
									boolean fieldchdHasFieldDisplay = fieldchd.isAnnotationPresent(FieldDisplay.class);
									if(fieldchdHasFieldDisplay){
										FieldDisplay fieldchdAnno = fieldchd.getAnnotation(FieldDisplay.class);
										String enumClass = fieldchdAnno.enumClass();
										if(null!=enumClass && !"".equals(enumClass)){
											Object enumCode = ReflectUtils.getEnumCode(Class.forName(enumClass), columnVal);
											if(Integer.class == enumCode.getClass()){
												ReflectUtils.setProperty(chdDto, attr, enumCode);
											}else if(String.class == enumCode.getClass()){
												ReflectUtils.setProperty(chdDto, attr, enumCode);
											}
										}else{
											ReflectUtils.setProperty(chdDto, attr, columnVal);
										}
									}
								} catch (Exception e) {
									String error = "< 解析excel值 >：页签【"+sheetName+"】，序号【"+num+"】行的【"+showname+"】字段的值【"+columnVal+"】，类型或格式错误";
									Field f = ReflectUtils.getFieldName(chdDto.getClass(), attr);
									Class<?> fieldType = f.getType();
									if(Date.class == fieldType){
										error = error + " 正确的日期格式为yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss";
									}
									logger.error(error);
									errorList.add(error);
								}
							}
						}
						body.get(num).add(chdDto);
					}
				}
				if(sheetAttr!=null){
					bodymap.put(sheetAttr, body);
				}
			}

			Map<String, TDto> map = new LinkedHashMap<>();
			for(String num: headmap.keySet()){
				if(bodymap==null||bodymap.size()==0){
					if(num==null||num.length()==0)
						continue;
					map.put(num, headmap.get(num).get(0));
					continue;
				}else{
					for(String bodykey: bodymap.keySet()){
						ReflectUtils.setProperty(headmap.get(num).get(0), bodykey, bodymap.get(bodykey).get(num));
					}
					map.put(num, headmap.get(num).get(0));
				}
			}
			if(isEdit){
				result.put("idList", idList);
				result.put("editColumn", editColumn);
			}
			result.put("result", map);
			result.put("error", errorList);
			excelStream.close();
		} catch (Exception e) {
			if (excelStream != null) {
				excelStream.close();
			}
			logger.error("excel解析失败，模板错误", e);
			throw new BusinessException("excel解析失败，模板错误" );
		}
		return result;
	}
	/**
	 * 根据数据，导出excel
	 * @param dataMap 前两个个元素ListTitle 后续的都是数据（页签为key）
	 * @param sheetMap 页签属性名称
	 * @throws Exception
	 */
	public static void writeExcelNew(Map<String, List<List<Object>>> dataMap, Map<String,String> sheetMap, HSSFWorkbook wb) throws Exception {
		try {
			// 导出的文字编码
			/*Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			fileName = new String(fileName.getBytes("GB2312"), "GB2312");
			fileName = fileName.substring(0,fileName.indexOf("_")+1)+simpleDateFormat.format(date);*/
			if(wb == null){
               throw new BusinessException("未创建Excel文件。");
            }

			//说明文字
			Font notefont = wb.createFont();//字体
			notefont.setBold(true);//加粗
			notefont.setColor(Font.COLOR_RED);//红色
			CellStyle noteStyle = wb.createCellStyle();
			noteStyle.setFont(notefont);//设置字体样式
			noteStyle.setAlignment(HorizontalAlignment.LEFT);//水平对齐
			noteStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
			noteStyle.setWrapText(false);//自动换行
			//表头样式
			Font font = wb.createFont();//字体
			font.setBold(true);//加粗
			CellStyle cellStylehead = wb.createCellStyle();
			cellStylehead.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStylehead.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStylehead.setFont(font);//设置字体样式
			cellStylehead.setBorderRight(BorderStyle.THIN);//右边框
			cellStylehead.setBorderBottom(BorderStyle.THIN);
			cellStylehead.setAlignment(HorizontalAlignment.LEFT);//水平对齐
			cellStylehead.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
			cellStylehead.setWrapText(false);//自动换行
			//表体样式
			CellStyle cellStyleBody = wb.createCellStyle();//表体单元格样式
			cellStyleBody.setAlignment(HorizontalAlignment.LEFT);//水平对齐
			cellStyleBody.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
			cellStyleBody.setWrapText(false);//自动换行

			/*//导出数据
			String filePath = System.getProperty("user.dir")+"/pdf/";
			File file = new File(filePath);
			if (!file.exists()){
				file.mkdirs();
			}
			*//*os = new FileOutputStream(new File(filePath+fileName+".xlsx"));*/
			for (Map.Entry<String, List<List<Object>>> entry : dataMap.entrySet()) {
				String key = entry.getKey();
				String sheetName = sheetMap.get(key);
				HSSFSheet sh = wb.getSheet(sheetName);
				List<List<Object>> dataList = entry.getValue();
				int initRownum = 0;
				if(sh == null){
					sh = wb.createSheet(sheetName);
					initRownum = 0;
				}else {//追加数据
					initRownum = sh.getLastRowNum()+1;
					dataList.remove(0);
					dataList.remove(0);
				}
				for(int index = 0; index < dataList.size(); index++){
					int realRowNum = index + initRownum;
					Row row = sh.createRow(realRowNum);
					List<Object> rowData = dataList.get(index);
					for(int cellnum = 0; cellnum < rowData.size(); cellnum++){
						if(cellnum == 0 && initRownum != 0){
							int no = (Integer)rowData.get(cellnum);
							rowData.set(cellnum,no+initRownum-2);
						}
						//单元格列宽自适应
						sh.autoSizeColumn(cellnum);
						Cell cell = row.createCell(cellnum);
						Object cellData = rowData.get(cellnum);
						CellFactory(cell, cellData);
						if(cellnum==0&&realRowNum==0){
							sh.getRow(realRowNum).getCell(cellnum).setCellStyle(noteStyle);
						}else if((realRowNum==0||realRowNum==1)){
							if("id".equals(sh.getRow(0).getCell(cellnum).getStringCellValue())){
								sh.getRow(realRowNum).getCell(cellnum).setCellStyle(noteStyle);
							}else if(cellStylehead != null){
								sh.getRow(realRowNum).getCell(cellnum).setCellStyle(cellStylehead);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
