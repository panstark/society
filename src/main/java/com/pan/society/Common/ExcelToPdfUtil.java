package com.pan.society.Common;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelToPdfUtil {
    public static void excelToPdf(String filePath, String name, HttpServletResponse response) throws Exception{

            Document document = new Document(PageSize.A4, 0, 0, 50, 0);
            response.setContentType("application/pdf");
            String fileName = new String(name.getBytes("GB2312"), "ISO-8859-1");
			response.addHeader("Content-Disposition", "attachment;filename="+fileName+".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            //创建BaseFont对象，指明字体，编码方式,是否嵌入STSong-Light
            String fontType = filePath+"simsun.ttf";
            File fontFile = new File(fontType);
            BaseFont baseFont = null;
             if (fontFile.exists()){
                baseFont = BaseFont.createFont(fontType, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            }else{
                baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            }

            //创建Font对象，将基础字体对象，字体大小，字体风格
            Font font = new Font(baseFont, 13,Font.NORMAL);
            int rowNum = 0;
            int colNum = 0;
            Workbook workbook = null;
            int baseCount = 6;
            try {
                workbook = Workbook.getWorkbook(new File(filePath+name+".xlsx"));
                if (workbook!=null)
                    for (Sheet sheet : workbook.getSheets()) {
                        int column = sheet.getColumns();
                        int sum = column/baseCount;
                        int count = column%baseCount;
                        //下面是找出表格中的空行和空列
                        List<Integer> nullCol = new ArrayList<>();
                        List<Integer> nullRow = new ArrayList<>();
                        for (int j = 0; j < sheet.getColumns(); j++) {
                            int nullColNum = 0;
                            for (int i = 0; i < sheet.getRows(); i++) {
                                Cell cell = sheet.getCell(j, i);
                                String str = cell.getContents();
                                if (str == null || "".equals(str)) {
                                    nullColNum++;
                                }
                            }
                            if (nullColNum == sheet.getRows()) {
                                nullCol.add(j);
                                column--;
                            }
                        }


                        for (int i = 0; i < sheet.getRows(); i++) {
                            int nullRowNum = 0;
                            for (int j = 0; j < sheet.getColumns(); j++) {
                                Cell cell = sheet.getCell(j, i);
                                String str = cell.getContents();
                                if (str == null || "".equals(str)) {
                                    nullRowNum++;
                                }
                            }
                            if (nullRowNum == sheet.getColumns()) {
                                nullRow.add(i);
                            }
                        }
                        //PdfPTable table = new PdfPTable(column);
                        float[] widths = {90, 90, 90, 90, 90, 90};
                        PdfPTable table = new PdfPTable(widths);
                        table.setLockedWidth(true);
                        table.setTotalWidth(540);
                        Range[] ranges = sheet.getMergedCells();
                        PdfPCell cell1 = new PdfPCell();
                        if (sum==0&&count!=0){
                            for (int i = 0; i < sheet.getRows(); i++) {
                                if (nullRow.contains(i)) {    //如果这一行是空行，这跳过这一行
                                    continue;
                                }
                                //通过sum和count进行控制

                                for (int j = 0; j < count; j++) {
                                    if (nullCol.contains(j)) {    //如果这一列是空列，则跳过一列
                                        continue;
                                    }
                                    boolean flag = true;
                                    Cell cell = sheet.getCell(j, i);
                                    String str = cell.getContents();
                                    for (Range range : ranges) {    //合并的单元格判断和处理
                                        if (j >= range.getTopLeft().getColumn() && j <= range.getBottomRight().getColumn()
                                                && i >= range.getTopLeft().getRow() && i <= range.getBottomRight().getRow()) {
                                            if (str == null || "".equals(str)) {
                                                flag = false;
                                                break;
                                            }
                                            rowNum = range.getBottomRight().getRow() - range.getTopLeft().getRow() + 1;
                                            colNum = range.getBottomRight().getColumn() - range.getTopLeft().getColumn() + 1;
                                            if (rowNum > colNum) {
                                                cell1 = mergeRow(str, font, rowNum);
                                                cell1.setColspan(colNum);
                                                table.addCell(cell1);
                                            } else {
                                                cell1 = mergeCol(str, font, colNum);
                                                cell1.setRowspan(rowNum);
                                                table.addCell(cell1);
                                            }
                                            int n = baseCount-count;
                                            if (j+1==count){
                                                for (int ss=0;ss<n;ss++){
                                                    table.addCell(getPDFCell("", font));
                                                }
                                            }
                                            flag = false;
                                            break;
                                        }
                                    }
                                    if (flag) {
                                        table.addCell(getPDFCell(str, font));
                                        int n = baseCount-count;
                                        if (j+1==count){
                                            for (int ss=0;ss<n;ss++){
                                                table.addCell(getPDFCell("", font));
                                            }
                                        }
                                    }
                                }


                            }

                            document.add(table);
                        }else if (count==0&&sum!=0){
                            for (int k = 0; k < sum; k++) {
                                for (int i = 0; i < sheet.getRows(); i++) {
                                    if (nullRow.contains(i)) {    //如果这一行是空行，这跳过这一行
                                        continue;
                                    }
                                    for (int j = 0+(k*baseCount); j < baseCount+(k*baseCount); j++) {
                                        if (nullCol.contains(j)) {    //如果这一列是空列，则跳过一列
                                            continue;
                                        }
                                        boolean flag = true;
                                        Cell cell = sheet.getCell(j, i);
                                        String str = cell.getContents();
                                        for (Range range : ranges) {    //合并的单元格判断和处理
                                            if (j >= range.getTopLeft().getColumn() && j <= range.getBottomRight().getColumn()
                                                    && i >= range.getTopLeft().getRow() && i <= range.getBottomRight().getRow()) {
                                                if (str == null || "".equals(str)) {
                                                    flag = false;
                                                    break;
                                                }
                                                rowNum = range.getBottomRight().getRow() - range.getTopLeft().getRow() + 1;
                                                colNum = range.getBottomRight().getColumn() - range.getTopLeft().getColumn() + 1;
                                                if (rowNum > colNum) {
                                                    cell1 = mergeRow(str, font, rowNum);
                                                    cell1.setColspan(colNum);
                                                    table.addCell(cell1);
                                                } else {
                                                    cell1 = mergeCol(str, font, colNum);
                                                    cell1.setRowspan(rowNum);
                                                    table.addCell(cell1);
                                                }
                                                flag = false;
                                                break;
                                            }
                                        }
                                        if (flag) {
                                            table.addCell(getPDFCell(str, font));
                                        }
                                    }


                                }
                            }
                            document.add(table);
                        }else if (sum!=0&&count!=0){
                            sum = sum+1;
                            for (int k = 0; k < sum; k++) {
                                if (k==sum-1){
                                    for (int i = 0; i < sheet.getRows(); i++) {
                                        if (nullRow.contains(i)) {    //如果这一行是空行，这跳过这一行
                                            continue;
                                        }
                                        for (int j = 0+(k*baseCount); j < count+(k*baseCount); j++) {
                                            if (nullCol.contains(j)) {    //如果这一列是空列，则跳过一列
                                                continue;
                                            }
                                            boolean flag = true;
                                            Cell cell = sheet.getCell(j, i);
                                            String str = cell.getContents();
                                            for (Range range : ranges) {    //合并的单元格判断和处理
                                                if (j >= range.getTopLeft().getColumn() && j <= range.getBottomRight().getColumn()
                                                        && i >= range.getTopLeft().getRow() && i <= range.getBottomRight().getRow()) {
                                                    if (str == null || "".equals(str)) {
                                                        flag = false;
                                                        break;
                                                    }
                                                    rowNum = range.getBottomRight().getRow() - range.getTopLeft().getRow() + 1;
                                                    colNum = range.getBottomRight().getColumn() - range.getTopLeft().getColumn() + 1;
                                                    if (rowNum > colNum) {
                                                        cell1 = mergeRow(str, font, rowNum);
                                                        cell1.setColspan(colNum);
                                                        table.addCell(cell1);
                                                    } else {
                                                        cell1 = mergeCol(str, font, colNum);
                                                        cell1.setRowspan(rowNum);
                                                        table.addCell(cell1);
                                                    }
                                                    int n = (baseCount+(k*baseCount))-(count+(k*baseCount));
                                                    if (j+1==count+(k*baseCount)){
                                                        for (int ss=0;ss<n;ss++){
                                                            table.addCell(getPDFCell("", font));
                                                        }
                                                    }
                                                    flag = false;
                                                    break;
                                                }
                                            }
                                            if (flag) {
                                                table.addCell(getPDFCell(str, font));
                                                int n = (baseCount+(k*baseCount))-(count+(k*baseCount));
                                                if (j+1==count+(k*baseCount)){
                                                    for (int ss=0;ss<n;ss++){
                                                        table.addCell(getPDFCell("", font));
                                                    }
                                                }
                                            }
                                        }


                                    }

                                }else{
                                    for (int i = 0; i < sheet.getRows(); i++) {
                                        if (nullRow.contains(i)) {    //如果这一行是空行，这跳过这一行
                                            continue;
                                        }
                                        for (int j = 0+(k*baseCount); j < baseCount+(k*baseCount); j++) {

                                            if (nullCol.contains(j)) {    //如果这一列是空列，则跳过一列
                                                continue;
                                            }
                                            boolean flag = true;
                                            Cell cell = sheet.getCell(j, i);
                                            String str = cell.getContents();
                                            for (Range range : ranges) {    //合并的单元格判断和处理
                                                if (j >= range.getTopLeft().getColumn() && j <= range.getBottomRight().getColumn()
                                                        && i >= range.getTopLeft().getRow() && i <= range.getBottomRight().getRow()) {
                                                    if (str == null || "".equals(str)) {
                                                        flag = false;
                                                        break;
                                                    }
                                                    rowNum = range.getBottomRight().getRow() - range.getTopLeft().getRow() + 1;
                                                    colNum = range.getBottomRight().getColumn() - range.getTopLeft().getColumn() + 1;
                                                    if (rowNum > colNum) {
                                                        cell1 = mergeRow(str, font, rowNum);
                                                        cell1.setColspan(colNum);
                                                        table.addCell(cell1);
                                                    } else {
                                                        cell1 = mergeCol(str, font, colNum);
                                                        cell1.setRowspan(rowNum);
                                                        table.addCell(cell1);
                                                    }
                                                    flag = false;
                                                    break;
                                                }
                                            }
                                            if (flag) {

                                                table.addCell(getPDFCell(str, font));
                                            }
                                        }


                                    }
                                }
                            }
                            document.add(table);
                        }
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                workbook.close();
                document.close();
                writer.close();
            }

    }
    //合并行的静态函数
    public static PdfPCell mergeRow(String str, Font font, int i) {
        //创建单元格对象，将内容及字体传入
        PdfPCell cell = new PdfPCell(new Paragraph(str, font));
        //设置单元格内容居中
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //将该单元格所在列包括该单元格在内的i行单元格合并为一个单元格
        cell.setRowspan(i);
        return cell;
    }
    //合并列的静态函数
    public static PdfPCell mergeCol(String str, Font font, int i) {
        PdfPCell cell = new PdfPCell(new Paragraph(str, font));
        cell.setMinimumHeight(25);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //将该单元格所在行包括该单元格在内的i列单元格合并为一个单元格
        cell.setColspan(i);
        return cell;
    }
    //获取指定内容与字体的单元格
    public static PdfPCell getPDFCell(String string, Font font){
        //创建单元格对象，将内容与字体放入段落中作为单元格内容
        PdfPCell cell = new PdfPCell(new Paragraph(string, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //设置最小单元格高度
        cell.setMinimumHeight(25);
        return cell;
    }
}
