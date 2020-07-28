package com.pan.society.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * POI CSV方式处理大数据excel文件，XLSX转CSV
 *
 * @author gaopengf
 * @date 2018-01-05 11:28:42
 * <p>
 * excel2007的底层数据结构是xml文件，采用SAX的事件驱动的方法解析
 * xml，可以大大降低内存的耗费，特别使用于大数据量的文件。
 */
public class XLSXCovertCSVReader {
    /**
     * The type of the data value is indicated by an attribute on the cell. The
     * value is usually in a "v" element within the cell.
     */
    enum xssfDataType {
        BOOL,
        ERROR,
        FORMULA,
        INLINESTR,
        SSTINDEX,
        NUMBER
    }

    /**
     * 使用xssf_sax_API处理Excel,请参考： http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api
     * <p/>
     * Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at
     * http://www.ecma-international.org/publications/standards/Ecma-376.htm
     * <p/>
     * A web-friendly version is http://openiso.org/Ecma/376/Part4
     */
    class MyXSSFSheetHandler extends DefaultHandler {
        /**
         * Table with styles
         */
        private StylesTable stylesTable;

        /**
         * Table with unique strings
         */
        private ReadOnlySharedStringsTable sharedStringsTable;

        /**
         * Destination for data
         */
        private final PrintStream output;

        /**
         * Number of columns to read starting with leftmost
         */
        private final int minColumnCount;

        // Set when V start element is seen
        private boolean vIsOpen;

        // Set when cell start element is seen;
        // used when cell close element is seen.
        private xssfDataType nextDataType;

        // Used to format numeric cell values.
        private short formatIndex;
        private String formatString;
        private final DataFormatter formatter;

        private int thisColumn = -1;
        // The last column printed to the output stream
        private int lastColumnNumber = -1;

        // Gathers characters as they are seen.
        private StringBuffer value;
        //        private String[] record;
        private List<String> record;
        private List<String[]> rows = new ArrayList<String[]>();
        private boolean isCellNull = false;

        /**
         * Accepts objects needed while parsing.
         *
         * @param styles Table of styles
         * @param strings Table of shared strings
         * @param cols Minimum number of columns to show
         * @param target Sink for output
         */
        public MyXSSFSheetHandler(StylesTable styles, ReadOnlySharedStringsTable strings, int cols,
                                  PrintStream target) {
            this.stylesTable = styles;
            this.sharedStringsTable = strings;
            this.minColumnCount = cols;
            this.output = target;
            this.value = new StringBuffer();
            this.nextDataType = xssfDataType.NUMBER;
            this.formatter = new DataFormatter();
//            record = new String[this.minColumnCount];
            record = new ArrayList<>();
            rows.clear();// 每次读取都清空行集合
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
         * java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if ("inlineStr".equals(name) || "v".equals(name)) {
                vIsOpen = true;
                // Clear contents cache
                value.setLength(0);
            }
            // c => cell
            else if ("c".equals(name)) {
                // Get the cell reference
                String r = attributes.getValue("r");
                int firstDigit = -1;
                for (int c = 0; c < r.length(); ++c) {
                    if (Character.isDigit(r.charAt(c))) {
                        firstDigit = c;
                        break;
                    }
                }
                thisColumn = nameToColumn(r.substring(0, firstDigit));

                // Set up defaults.
                this.nextDataType = xssfDataType.NUMBER;
                this.formatIndex = -1;
                this.formatString = null;
                String cellType = attributes.getValue("t");
                String cellStyleStr = attributes.getValue("s");
                if ("b".equals(cellType)) {
                    nextDataType = xssfDataType.BOOL;
                } else if ("e".equals(cellType)) {
                    nextDataType = xssfDataType.ERROR;
                } else if ("inlineStr".equals(cellType)) {
                    nextDataType = xssfDataType.INLINESTR;
                } else if ("s".equals(cellType)) {
                    nextDataType = xssfDataType.SSTINDEX;
                } else if ("str".equals(cellType)) {
                    nextDataType = xssfDataType.FORMULA;
                } else if (cellStyleStr != null) {
                    // It's a number, but almost certainly one
                    // with a special style or format
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null) {
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                    }
                }
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
         * java.lang.String, java.lang.String)
         */
        public void endElement(String uri, String localName, String name) throws SAXException {
            String thisStr = null;

            // v => contents of a cell
            if ("v".equals(name)) {
                // Process the value contents as required.
                // Do now, as characters() may be called more than once
                switch (nextDataType) {
                    case BOOL:
                        char first = value.charAt(0);
                        thisStr = first == '0' ? "FALSE" : "TRUE";
                        break;

                    case ERROR:
                        thisStr = "\"ERROR:" + value.toString() + '"';
                        break;

                    case FORMULA:
                        // A formula could result in a string value,
                        // so always add double-quote characters.
                        thisStr = '"' + value.toString() + '"';
                        break;

                    case INLINESTR:
                        // TODO: have seen an example of this, so it's untested.
                        XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                        thisStr = '"' + rtsi.toString() + '"';
                        break;

                    case SSTINDEX:
                        String sstIndex = value.toString();
                        try {
                            int idx = Integer.parseInt(sstIndex);
                            XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
                            thisStr = '"' + rtss.toString() + '"';
                        } catch (NumberFormatException ex) {
                            output.println("Failed to parse SST index '" + sstIndex + "': " + ex.toString());
                        }
                        break;

                    case NUMBER:
                        String n = value.toString();
                        // 判断是否是日期格式
                        if (HSSFDateUtil.isADateFormat(this.formatIndex, n)) {
                            Double d = Double.parseDouble(n);
                            Date date = HSSFDateUtil.getJavaDate(d);
                            thisStr = formateDateToString(date);
                        } else if (this.formatString != null) {
                            thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex,
                                    this.formatString);
                        } else {
                            double d = Double.valueOf(n);
                            NumberFormat nf = NumberFormat.getInstance();
                            thisStr = nf.format(d);
                        }
                        break;

                    default:
                        thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
                        break;
                }

                // Output after we've seen the string contents
                // Emit commas for any fields that were missing on this row
                if (lastColumnNumber == -1) {
                    lastColumnNumber = 0;
                }
                //判断单元格的值是否为空
                if (thisStr == null || "".equals(isCellNull)) {
                    isCellNull = true;// 设置单元格是否为空值
                }
                while (record.size() < thisColumn) {
                    record.add("");
                }
//                record[thisColumn] = thisStr;
                record.add(thisColumn, thisStr);
                // Update column
                if (thisColumn > -1) {
                    lastColumnNumber = thisColumn;
                }
            } else if ("row".equals(name)) {
                // Print out any missing commas if needed
                if (minColumns > 0) {
                    if (lastColumnNumber == -1) {
                        lastColumnNumber = 0;
                    }
                    // 判断是否空行
                    if (isCellNull == false && CollectionUtils.isNotEmpty(record) && record.get(0) != null &&
                            (record.size() >= 2 ? record.get(1) != null : true)) {
                        rows.add(record.toArray(new String[0]).clone());
                        isCellNull = false;
                        record.clear();
                    }
                }
                lastColumnNumber = -1;
            }
        }

        public List<String[]> getRows() {
            return rows;
        }

        public void setRows(List<String[]> rows) {
            this.rows = rows;
        }

        /**
         * Captures characters only if a suitable element is open. Originally
         * was just "v"; extended for inlineStr also.
         */
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (vIsOpen) {
                value.append(ch, start, length);
            }
        }

        /**
         * Converts an Excel column name like "C" to a zero-based index.
         *
         * @param name
         * @return Index corresponding to the specified name
         */
        private int nameToColumn(String name) {
            int column = -1;
            for (int i = 0; i < name.length(); ++i) {
                int c = name.charAt(i);
                column = (column + 1) * 26 + c - 'A';
            }
            return column;
        }

        private String formateDateToString(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化日期
            return sdf.format(date);
        }
    }

    private OPCPackage xlsxPackage;
    private int minColumns;
    private PrintStream output;
    private String sheetName;

    /**
     * Creates a new XLSX -> CSV converter
     *
     * @param pkg The XLSX package to process
     * @param output The PrintStream to output the CSV to
     * @param minColumns The minimum number of columns to output, or -1 for no minimum
     */
    public XLSXCovertCSVReader(OPCPackage pkg, PrintStream output, String sheetName, int minColumns) {
        this.xlsxPackage = pkg;
        this.output = output;
        this.minColumns = minColumns;
        this.sheetName = sheetName;
    }

    /**
     * Parses and shows the content of one sheet using the specified styles and
     * shared-strings tables.
     */
    public List<String[]> processSheet(StylesTable styles, ReadOnlySharedStringsTable strings,
                                       InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {
        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        MyXSSFSheetHandler handler = new MyXSSFSheetHandler(styles, strings, this.minColumns, this.output);
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);
        return handler.getRows();
    }

    /**
     * 初始化这个处理程序 将
     */
    public Map<String, List<String[]>> process()
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        Map<String, List<String[]>> map = new LinkedHashMap<>();

        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            List<String[]> list;
            InputStream stream = iter.next();
            try {
                String sheetNameTemp = iter.getSheetName();
//            if (this.sheetName.equals(sheetNameTemp)) {
//            	list = processSheet(styles, strings, stream);
//                ++index;
//            }
                list = processSheet(styles, strings, stream);
                map.put(sheetNameTemp, list);
                stream.close();
            } catch (Exception e) {
                if (stream != null) {
                    stream.close();
                }
                throw e;
            }
        }
        return map;
    }

    /**
     * 读取Excel
     *
     * @param path 文件路径
     * @param sheetName sheet名称
     * @param minColumns 列总数
     * @return
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws OpenXML4JException
     * @throws IOException
     */
    public static Map<String, List<String[]>> readerExcel(String path, String sheetName, int minColumns)
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        OPCPackage p = OPCPackage.open(path, PackageAccess.READ);
        XLSXCovertCSVReader xlsx2csv = new XLSXCovertCSVReader(p, System.out, sheetName, minColumns);
        Map<String, List<String[]>> map = xlsx2csv.process();
        p.close();
        return map;
    }

    /**
     * 读取Excel
     */
    public static Map<String, List<String[]>> readerExcelByInputStream(InputStream excelStream)
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        int minColumns = 300;
        String sheetName = null;
        OPCPackage p = OPCPackage.open(excelStream);
        XLSXCovertCSVReader xlsx2csv = new XLSXCovertCSVReader(p, System.out, sheetName, minColumns);
        Map<String, List<String[]>> map = xlsx2csv.process();
        p.close();
        return map;
    }

//    public static void main(String[] args) throws Exception {
//    	 List<String[]> list = XLSXCovertCSVReader.readerExcelByInputStream(new FileInputStream("D:\\sxssfxx.xlsx"),
//    	 "0");
////        List<String[]> list = XLSXCovertCSVReader
////                .readerExcel(
////                        "D:\\默认模板.xlsx",
////                        "表体Two", 5);
//        for (String[] record : list) {  
//            for (String cell : record) {  
//                System.out.print(cell + "  ");  
//            }  
//            System.out.println();  
//        }  
//    }  
}
